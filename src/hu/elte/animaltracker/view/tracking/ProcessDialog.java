package hu.elte.animaltracker.view.tracking;

import hu.elte.animaltracker.model.CustomisableProcess;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.JComboBox;

import java.awt.Insets;

public class ProcessDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 670009490395343587L;
	private JPanel contentPanel = new JPanel();
	private JPanel northPanel = new JPanel();
	JScrollPane scrollPane;
	List<CustomisableProcess> availableProcess;
	List<CustomisableProcess> currentProcess;
	private JComboBox comboBox;

	/**
	 * Create the frame.
	 */
	public ProcessDialog(String title,
			List<? extends CustomisableProcess> availableProcess,
			List<? extends CustomisableProcess> currentProcess) {
		this.availableProcess = (List<CustomisableProcess>) availableProcess;
		this.currentProcess = (List<CustomisableProcess>) currentProcess;
		setTitle(title);
		setBounds(100, 100, 400, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		getContentPane().add(northPanel, BorderLayout.NORTH);
		GridBagLayout gbl_northPanel = new GridBagLayout();
		gbl_northPanel.columnWidths = new int[] { 100, 50 };
		gbl_northPanel.rowHeights = new int[] { 23 };
		gbl_northPanel.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gbl_northPanel.rowWeights = new double[] { 0.0 };
		northPanel.setLayout(gbl_northPanel);

		comboBox = new JComboBox(availableProcess.toArray());
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.insets = new Insets(10, 0, 0, 5);
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 0;
		northPanel.add(comboBox, gbc_comboBox);

		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addFilter();
			}
		});
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAdd.insets = new Insets(10, 0, 0, 5);
		gbc_btnAdd.gridx = 2;
		gbc_btnAdd.gridy = 0;
		northPanel.add(btnAdd, gbc_btnAdd);

		scrollPane = new JScrollPane();

		mainPanelUpdate();

		contentPanel.add(scrollPane);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		{
			JButton okButton = new JButton("OK");
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
				}
			});
			buttonPane.add(okButton);
			getRootPane().setDefaultButton(okButton);
		}
		{
			JButton cancelButton = new JButton("Cancel");
			buttonPane.add(cancelButton);
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
				}
			});
		}

	}

	protected void addFilter() {
		CustomisableProcess filter = (CustomisableProcess) comboBox
				.getSelectedItem();
		filter = filter.getNewInstance();
		currentProcess.add(filter);
		mainPanelUpdate();
	}

	private void mainPanelUpdate() {
		JPanel panelmain = new JPanel();
		GridBagLayout gbl_panelmain = new GridBagLayout();

		gbl_panelmain.columnWidths = new int[] { 0, 0 };
		gbl_panelmain.rowHeights = new int[currentProcess.size() + 1];
		gbl_panelmain.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		double[] rowWeights = new double[currentProcess.size() + 1];
		rowWeights[currentProcess.size()] = Double.MIN_VALUE;
		gbl_panelmain.rowWeights = rowWeights;
		panelmain.setLayout(gbl_panelmain);
		int r = 0;
		for (CustomisableProcess filter : currentProcess) {
			JPanel panel = getRow(filter);
			GridBagConstraints gbc_row = new GridBagConstraints();
			gbc_row.gridx = 0;
			gbc_row.gridy = r++;
			gbc_row.anchor = GridBagConstraints.EAST;
			panelmain.add(panel, gbc_row);

		}
		contentPanel.setLayout(new GridLayout(0, 1, 0, 0));
		scrollPane.setViewportView(panelmain);
	}

	private JPanel getRow(final CustomisableProcess filter) {
		JPanel rowPanel = new JPanel();
		rowPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		JLabel label = new JLabel(filter.getName());
		JButton btnSettings = new JButton("Settings");
		rowPanel.add(label);
		rowPanel.add(btnSettings);

		btnSettings.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				filter.showGUI();
			}
		});

		JPanel buttonPane = new JPanel();
		BasicArrowButton btnUpButton = new BasicArrowButton(
				BasicArrowButton.NORTH);
		btnUpButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int idx = currentProcess.indexOf(filter);
				if (idx == 0)
					return;
				CustomisableProcess z = currentProcess.get(idx);
				currentProcess.set(idx, currentProcess.set(idx - 1, z));
				mainPanelUpdate();
			}
		});
		buttonPane.add(btnUpButton);
		BasicArrowButton btnDownButton = new BasicArrowButton(
				BasicArrowButton.SOUTH);
		btnDownButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int idx = currentProcess.indexOf(filter);
				if (idx == currentProcess.size() - 1)
					return;
				CustomisableProcess z = currentProcess.get(idx);
				currentProcess.set(idx, currentProcess.set(idx + 1, z));
				mainPanelUpdate();
			}
		});
		buttonPane.add(btnDownButton);

		JButton btnRemove = new JButton("x");
		btnRemove.setPreferredSize(new Dimension(20, 15));
		btnRemove.setMargin(new Insets(1, 1, 1, 1));
		btnRemove.setToolTipText("Remove this filter");
		btnRemove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int reply = JOptionPane.showConfirmDialog(null,
						"Do you want to remove this filter?", "Remove filter",
						JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION) {
					currentProcess.remove(filter);
					mainPanelUpdate();
				}
			}
		});
		buttonPane.add(btnRemove);

		rowPanel.add(buttonPane);

		return rowPanel;
	}

}
