package hu.elte.animaltracker.examples.watermaze;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import java.awt.GridBagLayout;

import javax.swing.JButton;

import java.awt.Checkbox;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class WaterMazeView extends JFrame {

	private static final long serialVersionUID = 1032632304752701483L;
	protected JPanel contentPane;

	public WaterMazeView(final WaterMazeController controller) {
		setTitle("Water Maze");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 210, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 120 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0 };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0 };
		contentPane.setLayout(gbl_contentPane);

		JButton btnSetImage = new JButton("Set image");
		btnSetImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.setActiveImage();
			}
		});
		GridBagConstraints gbc_btnSetImage = new GridBagConstraints();
		gbc_btnSetImage.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSetImage.insets = new Insets(0, 0, 5, 0);
		gbc_btnSetImage.gridx = 0;
		gbc_btnSetImage.gridy = 0;
		contentPane.add(btnSetImage, gbc_btnSetImage);

		JButton btnSetPool = new JButton("Set pool");
		btnSetPool.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.setPool();
			}
		});
		GridBagConstraints gbc_btnSetPool = new GridBagConstraints();
		gbc_btnSetPool.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSetPool.insets = new Insets(0, 0, 5, 0);
		gbc_btnSetPool.gridx = 0;
		gbc_btnSetPool.gridy = 1;
		contentPane.add(btnSetPool, gbc_btnSetPool);

		JButton btnSetPlatform = new JButton("Set platform");
		btnSetPlatform.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.setPlatform();
			}
		});
		GridBagConstraints gbc_btnSetPlatform = new GridBagConstraints();
		gbc_btnSetPlatform.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSetPlatform.insets = new Insets(0, 0, 5, 0);
		gbc_btnSetPlatform.gridx = 0;
		gbc_btnSetPlatform.gridy = 2;
		contentPane.add(btnSetPlatform, gbc_btnSetPlatform);

		JButton btnShowTracker = new JButton("Show tracker");
		btnShowTracker.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.showTracker();
			}
		});
		GridBagConstraints gbc_btnShowTracker = new GridBagConstraints();
		gbc_btnShowTracker.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnShowTracker.insets = new Insets(0, 0, 5, 0);
		gbc_btnShowTracker.gridx = 0;
		gbc_btnShowTracker.gridy = 3;
		contentPane.add(btnShowTracker, gbc_btnShowTracker);

		JButton btnShowSummary = new JButton("Show analyzer");
		btnShowSummary.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.showSummary();
			}
		});
		GridBagConstraints gbc_btnShowSummary = new GridBagConstraints();
		gbc_btnShowSummary.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnShowSummary.insets = new Insets(0, 0, 5, 0);
		gbc_btnShowSummary.gridx = 0;
		gbc_btnShowSummary.gridy = 4;
		contentPane.add(btnShowSummary, gbc_btnShowSummary);

		JPanel viewPanel = new JPanel(new GridLayout(5, 1));
		viewPanel.setBorder(new TitledBorder(null, "View",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));

		GridBagConstraints gbc_viewPanel = new GridBagConstraints();
		gbc_viewPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_viewPanel.insets = new Insets(0, 0, 15, 0);
		gbc_viewPanel.gridx = 0;
		gbc_viewPanel.gridy = 5;
		contentPane.add(viewPanel, gbc_viewPanel);

		final JCheckBox showPlatformChb = new JCheckBox("Show platform");
		showPlatformChb.setSelected(controller.isPlatformVisible());
		showPlatformChb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.setPlatformVisible(showPlatformChb.isSelected());

			}
		});
		viewPanel.add(showPlatformChb);

		final JCheckBox showPoolChb = new JCheckBox("Show pool");
		showPoolChb.setSelected(controller.isPoolVisible());
		showPoolChb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.setPoolVisible(showPoolChb.isSelected());

			}
		});
		viewPanel.add(showPoolChb);

		final JCheckBox showtrackChb = new JCheckBox("Show track");
		showtrackChb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.setTrackVisible(showtrackChb.isSelected());

			}
		});
		viewPanel.add(showtrackChb);

		final JCheckBox showVectorChb = new JCheckBox("Show vector");
		showVectorChb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.setVectorVisible(showVectorChb.isSelected());

			}
		});
		viewPanel.add(showVectorChb);

		final JCheckBox showPrefChb = new JCheckBox("Show pref. angle");
		showPrefChb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.setAngleVisible(showPrefChb.isSelected());

			}
		});
		viewPanel.add(showPrefChb);

		JButton btnLoad = new JButton("Load measurment");
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.load();
			}
		});
		GridBagConstraints gbc_btnLoad = new GridBagConstraints();
		gbc_btnLoad.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnLoad.insets = new Insets(0, 0, 5, 0);
		gbc_btnLoad.gridx = 0;
		gbc_btnLoad.gridy = 6;
		contentPane.add(btnLoad, gbc_btnLoad);

		JButton btnSave = new JButton("Save measurment");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.save();
			}
		});

		GridBagConstraints gbc_btnSave = new GridBagConstraints();
		gbc_btnSave.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSave.insets = new Insets(0, 0, 5, 0);
		gbc_btnSave.gridx = 0;
		gbc_btnSave.gridy = 7;
		contentPane.add(btnSave, gbc_btnSave);
	}
}
