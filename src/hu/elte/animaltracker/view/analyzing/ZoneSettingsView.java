package hu.elte.animaltracker.view.analyzing;

import hu.elte.animaltracker.controller.analyzing.ParameterController;
import hu.elte.animaltracker.controller.analyzing.TrackAnalyzerController;
import hu.elte.animaltracker.controller.analyzing.ZoneSetting;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class ZoneSettingsView extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7196558221891742459L;
	private JPanel contentPane;
	private JPanel centerPanel;
	private JPanel southPane;
	private JButton btnClose;
	protected JScrollPane scrollPane;

	protected List<ParameterController> parameterControllers;
	protected List<ZoneSetting> list;

	/**
	 * Create the frame.
	 * 
	 * @param trackAnalyzerController
	 */
	public ZoneSettingsView(
			final TrackAnalyzerController trackAnalyzerController,
			final List<ZoneSetting> list) {

		if (list.size() == 0) {
			return;
		}

		this.list = list;
		this.parameterControllers = list.get(0).getParameterControllers();
		setTitle("Set parameters");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 448, 254);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		centerPanel = new JPanel(new GridLayout(1, 1));
		contentPane.add(centerPanel, BorderLayout.CENTER);
		southPane = new JPanel();
		contentPane.add(southPane, BorderLayout.SOUTH);
		btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				trackAnalyzerController.updateTable();
				close();
			}
		});
		southPane.add(btnClose);

		scrollPane = new JScrollPane();
		mainPanelUpdate();
		contentPane.add(scrollPane);
	}

	protected void close() {
		setVisible(false);
		dispose();
	}

	private JCheckBox setCheckBox(String name, int col, JPanel row, boolean state) {
		JCheckBox checkBox = new JCheckBox(name);
		checkBox.setSelected(state);
		GridBagConstraints gbc_checkBox = new GridBagConstraints();
		gbc_checkBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_checkBox.insets = new Insets(0, 0, 5, 0);
		gbc_checkBox.gridx = col;
		gbc_checkBox.gridy = 0;
		row.add(checkBox, gbc_checkBox);
		return checkBox;
	}

	private void mainPanelUpdate() {
		JPanel panelmain = new JPanel();
		GridBagLayout gbl_panelmain = new GridBagLayout();

		gbl_panelmain.columnWidths = new int[] { 0, 0 };
		gbl_panelmain.rowHeights = new int[parameterControllers.size() + 1];
		gbl_panelmain.columnWeights = new double[] { 0.0, 0.0 };
		double[] rowWeights = new double[parameterControllers.size() + 1];
		rowWeights[parameterControllers.size()] = Double.MIN_VALUE;
		gbl_panelmain.rowWeights = rowWeights;
		panelmain.setLayout(gbl_panelmain);
		int r = 0;
		for (ParameterController paramter : parameterControllers) {
			JPanel panel = getRow(paramter, r);
			GridBagConstraints gbc_row = new GridBagConstraints();
			gbc_row.gridx = 0;
			gbc_row.gridy = r++;
			gbc_row.anchor = GridBagConstraints.EAST;
			panelmain.add(panel, gbc_row);

		}
		centerPanel.setLayout(new GridLayout(0, 1, 0, 0));
		scrollPane.setViewportView(panelmain);
	}

	private JPanel getRow(ParameterController paramter, final int idx) {
		JPanel rowPanel = new JPanel();

		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 120, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0 };
		gbl_contentPane.columnWeights = new double[] { 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.rowWeights = new double[] { 0 };
		rowPanel.setLayout(gbl_contentPane);

		int gridXIdx = 0;

		JLabel label = new JLabel(paramter.getParameter().getName());
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.fill = GridBagConstraints.HORIZONTAL;
		gbc_label.insets = new Insets(0, 0, 5, 0);
		gbc_label.gridy = 0;
		gbc_label.gridx = gridXIdx++;
		rowPanel.add(label, gbc_label);

		final JCheckBox checkBox1 = setCheckBox("Raw Data", gridXIdx++,rowPanel,
				paramter.needRaw);
		checkBox1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				for (ZoneSetting selector : list)
					selector.getParameterControllers().get(idx).needRaw = checkBox1
							.isSelected();
			}
		});

		final JCheckBox checkBox2 = setCheckBox("Sum", gridXIdx++, rowPanel,
				paramter.needSum);
		checkBox2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				for (ZoneSetting selector : list)
					selector.getParameterControllers().get(idx).needSum = checkBox2
							.isSelected();
			}
		});

		final JCheckBox checkBox3 = setCheckBox("Mean", gridXIdx++, rowPanel,
				paramter.needMean);
		checkBox3.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				for (ZoneSetting selector : list)
					selector.getParameterControllers().get(idx).needMean = checkBox3
							.isSelected();
			}
		});

		final JCheckBox checkBox4 = setCheckBox("SD", gridXIdx++, rowPanel,
				paramter.needSd);
		checkBox4.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				for (ZoneSetting selector : list)
					selector.getParameterControllers().get(idx).needSd = checkBox4
							.isSelected();
			}
		});

		final JCheckBox checkBox5 = setCheckBox("Max", gridXIdx++, rowPanel,
				paramter.needMax);
		checkBox5.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				for (ZoneSetting selector : list)
					selector.getParameterControllers().get(idx).needMax = checkBox5
							.isSelected();
			}
		});

		final JCheckBox checkBox6 = setCheckBox("Min", gridXIdx++, rowPanel,
				paramter.needMin);
		checkBox6.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				for (ZoneSetting selector : list)
					selector.getParameterControllers().get(idx).needMin = checkBox6
							.isSelected();
			}
		});

		return rowPanel;
	}

}
