package hu.elte.animaltracker.view.analyzing;

import hu.elte.animaltracker.model.CustomisableProcess;

import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ZoneAnalyzeParameterView extends JFrame {

	private static final long serialVersionUID = -8173742401265318788L;
	protected JPanel contentPane;
	protected JScrollPane scrollPane;
	List<? extends CustomisableProcess> parametersSettings;
	
	public ZoneAnalyzeParameterView(
			List<? extends CustomisableProcess> parametersSettings) {
		this.parametersSettings = parametersSettings;
		setTitle("Set parameters");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 300, 300);
		
		contentPane = new JPanel(new GridLayout(1, 1));
		contentPane.setBorder(new EmptyBorder(10,10, 10, 10));
		
		scrollPane = new JScrollPane();

		mainPanelUpdate();

		contentPane.add(scrollPane);
		setContentPane(contentPane);
	}
	
	private void mainPanelUpdate() {
		JPanel panelmain = new JPanel();
		GridBagLayout gbl_panelmain = new GridBagLayout();

		gbl_panelmain.columnWidths = new int[] { 0, 0 };
		gbl_panelmain.rowHeights = new int[parametersSettings.size() + 1];
		gbl_panelmain.columnWeights = new double[] { 0.0, 0.0 };
		double[] rowWeights = new double[parametersSettings.size() + 1];
		rowWeights[parametersSettings.size()] = Double.MIN_VALUE;
		gbl_panelmain.rowWeights = rowWeights;
		panelmain.setLayout(gbl_panelmain);
		int r = 0;
		for (CustomisableProcess filter : parametersSettings) {
			JPanel panel = getRow(filter);
			GridBagConstraints gbc_row = new GridBagConstraints();
			gbc_row.gridx = 0;
			gbc_row.gridy = r++;
			gbc_row.anchor = GridBagConstraints.EAST;
			panelmain.add(panel, gbc_row);

		}
		contentPane.setLayout(new GridLayout(0, 1, 0, 0));
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
		
		return rowPanel;
	}

}
