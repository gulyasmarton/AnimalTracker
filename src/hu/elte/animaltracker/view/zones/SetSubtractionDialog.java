package hu.elte.animaltracker.view.zones;

import hu.elte.animaltracker.model.zones.ZoneUnit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicArrowButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;

import java.awt.GridLayout;

public class SetSubtractionDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	JScrollPane scrollPane;
	private List<ZoneUnit> units;

	public SetSubtractionDialog(List<ZoneUnit> units) {
		this.units = units;
		setTitle("Subtraction order");
		setBounds(100, 100, 200, 300);
		getContentPane().setLayout(new BorderLayout());
		setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		scrollPane = new JScrollPane();

		// panelmain.setLayout(new BoxLayout(panelmain, BoxLayout.Y_AXIS));

		//
		// JPanel panel1 = new JPanel();
		// panel1.setBackground(Color.red);
		// panelmain.add(panel1);
		//
		//
		// scrollPane.setViewportView(panelmain);

		// scrollPane.setVisibleRowCount(2);

		mainPanelUpdate();

		contentPanel.add(scrollPane);

		{
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
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	private void mainPanelUpdate() {
		JPanel panelmain = new JPanel();
		GridBagLayout gbl_panelmain = new GridBagLayout();

		gbl_panelmain.columnWidths = new int[] { 0, 0 };
		gbl_panelmain.rowHeights = new int[units.size() + 1];
		gbl_panelmain.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		double[] rowWeights = new double[units.size() + 1];
		rowWeights[units.size()] = Double.MIN_VALUE;
		gbl_panelmain.rowWeights = rowWeights;
		panelmain.setLayout(gbl_panelmain);
		int r = 0;
		for (ZoneUnit zoneUnit : units) {
			JPanel panel = getRow(zoneUnit);
			GridBagConstraints gbc_row = new GridBagConstraints();
			gbc_row.gridx = 0;
			gbc_row.gridy = r++;
			gbc_row.anchor = GridBagConstraints.EAST;
			panelmain.add(panel, gbc_row);

		}
		contentPanel.setLayout(new GridLayout(0, 1, 0, 0));
		scrollPane.setViewportView(panelmain);
	}

	private JPanel getRow(final ZoneUnit zoneUnit) {
		JPanel rowPanel = new JPanel();
		rowPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		JLabel label = new JLabel(zoneUnit.toString());
		rowPanel.add(label);

		JPanel buttonPane = new JPanel();
		BasicArrowButton btnUpButton = new BasicArrowButton(
				BasicArrowButton.NORTH);
		btnUpButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				int idx = units.indexOf(zoneUnit);
				if (idx == 0)
					return;
				ZoneUnit z = units.get(idx);
				units.set(idx, units.set(idx - 1, z));
				mainPanelUpdate();
			}
		});
		buttonPane.add(btnUpButton);
		BasicArrowButton btnDownButton = new BasicArrowButton(
				BasicArrowButton.SOUTH);
		btnDownButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				int idx = units.indexOf(zoneUnit);
				if (idx == units.size()-1)
					return;
				ZoneUnit z = units.get(idx);
				units.set(idx, units.set(idx + 1, z));
				mainPanelUpdate();
			}
		});
		buttonPane.add(btnDownButton);
		rowPanel.add(buttonPane);

		return rowPanel;
	}

}
