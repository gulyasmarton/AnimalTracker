package hu.elte.animaltracker.view.tracking;

import hu.elte.animaltracker.controller.tracking.TrackingEditorController;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.GridBagLayout;

import javax.swing.JButton;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JCheckBox;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TrackingEditorView extends JFrame {

	private JPanel contentPane;
	private JList interList;

	/**
	 * Create the frame.
	 */
	public TrackingEditorView(final TrackingEditorController controller) {
		setTitle("Track Editor");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 323, 247);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel_interpol_buttons = new JPanel();
		panel_interpol_buttons.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.add(panel_interpol_buttons, BorderLayout.WEST);
		GridBagLayout gbl_panel_interpol_buttons = new GridBagLayout();
		gbl_panel_interpol_buttons.columnWidths = new int[] { 120 };
		gbl_panel_interpol_buttons.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_panel_interpol_buttons.columnWeights = new double[] { 0.0 };
		gbl_panel_interpol_buttons.rowWeights = new double[] { 0.0, 0.0, 0.0,
				0.0, Double.MIN_VALUE };
		panel_interpol_buttons.setLayout(gbl_panel_interpol_buttons);

		JButton btnAddPoint = new JButton("Add point");
		btnAddPoint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.addPoint();
			}
		});
		GridBagConstraints gbc_btnAddPoint = new GridBagConstraints();
		gbc_btnAddPoint.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAddPoint.insets = new Insets(0, 0, 5, 0);
		gbc_btnAddPoint.gridx = 0;
		gbc_btnAddPoint.gridy = 0;
		panel_interpol_buttons.add(btnAddPoint, gbc_btnAddPoint);

		JButton btnRemovePoint = new JButton("Remove point");
		btnRemovePoint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.removePoint(interList.getSelectedValues());
			}
		});
		GridBagConstraints gbc_btnRemovePoint = new GridBagConstraints();
		gbc_btnRemovePoint.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnRemovePoint.insets = new Insets(0, 0, 5, 0);
		gbc_btnRemovePoint.gridx = 0;
		gbc_btnRemovePoint.gridy = 1;
		panel_interpol_buttons.add(btnRemovePoint, gbc_btnRemovePoint);

		JButton btnInterpolateTrack = new JButton("Interpolate track");
		btnInterpolateTrack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.interpolationTracking();
			}
		});
		GridBagConstraints gbc_btnInterpolateTrack = new GridBagConstraints();
		gbc_btnInterpolateTrack.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnInterpolateTrack.insets = new Insets(0, 0, 5, 0);
		gbc_btnInterpolateTrack.gridx = 0;
		gbc_btnInterpolateTrack.gridy = 2;
		panel_interpol_buttons
				.add(btnInterpolateTrack, gbc_btnInterpolateTrack);

		JButton btnDeleteTrack = new JButton("Delete track");
		btnDeleteTrack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.deleteTrack();
			}
		});
		GridBagConstraints gbc_btnDeleteTrack = new GridBagConstraints();
		gbc_btnDeleteTrack.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnDeleteTrack.gridx = 0;
		gbc_btnDeleteTrack.gridy = 3;
		panel_interpol_buttons.add(btnDeleteTrack, gbc_btnDeleteTrack);

		JPanel centerPanel = new JPanel(new GridLayout(1, 1));
		centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.add(centerPanel, BorderLayout.CENTER);

		JScrollPane scrollPane = new JScrollPane();

		interList = new JList(controller.getInterModel());
		interList.setVisibleRowCount(2);
		scrollPane.setViewportView(interList);
		centerPanel.add(scrollPane);

		JPanel southPanel = new JPanel();
		contentPane.add(southPanel, BorderLayout.SOUTH);

		final JCheckBox chckbxAllowManualTracking = new JCheckBox(
				"Allow manual tracking");
		chckbxAllowManualTracking.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				controller
						.setAllowManualTracking(chckbxAllowManualTracking
								.isSelected());
			}
		});
		southPanel.add(chckbxAllowManualTracking);

		JPanel northPanel = new JPanel();
		contentPane.add(northPanel, BorderLayout.NORTH);

		JButton btnInterpolateGaps = new JButton("Interpolate gaps");
		btnInterpolateGaps.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.interpolationGaps();
			}
		});
		northPanel.add(btnInterpolateGaps);
	}

}
