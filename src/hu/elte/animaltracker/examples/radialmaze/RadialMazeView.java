package hu.elte.animaltracker.examples.radialmaze;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class RadialMazeView extends JFrame {
	
	private static final long serialVersionUID = -6998319161873615842L;
	private JPanel contentPane;
	
	
	public RadialMazeView(final RadialMazeController controller) {
		setTitle("Radial Maze");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 210, 350);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 120 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0 };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0 };
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

		JButton btnSetPool = new JButton("Set maze");
		btnSetPool.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.setMaze();
			}
		});
		GridBagConstraints gbc_btnSetPool = new GridBagConstraints();
		gbc_btnSetPool.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSetPool.insets = new Insets(0, 0, 5, 0);
		gbc_btnSetPool.gridx = 0;
		gbc_btnSetPool.gridy = 1;
		contentPane.add(btnSetPool, gbc_btnSetPool);

		

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
		gbc_btnShowTracker.gridy = 2;
		contentPane.add(btnShowTracker, gbc_btnShowTracker);

		JButton btnShowSummary = new JButton("Show analyzer");
		btnShowSummary.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.showAnalyzer();
			}
		});
		GridBagConstraints gbc_btnShowSummary = new GridBagConstraints();
		gbc_btnShowSummary.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnShowSummary.insets = new Insets(0, 0, 5, 0);
		gbc_btnShowSummary.gridx = 0;
		gbc_btnShowSummary.gridy = 3;
		contentPane.add(btnShowSummary, gbc_btnShowSummary);
		

		JPanel viewPanel = new JPanel(new GridLayout(3, 1));
		viewPanel.setBorder(new TitledBorder(null, "View",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));

		GridBagConstraints gbc_viewPanel = new GridBagConstraints();
		gbc_viewPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_viewPanel.insets = new Insets(0, 0, 15, 0);
		gbc_viewPanel.gridx = 0;
		gbc_viewPanel.gridy = 4;
		contentPane.add(viewPanel, gbc_viewPanel);

		final JCheckBox showMazeChb = new JCheckBox("Show maze");
		showMazeChb.setSelected(true);
		showMazeChb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.setMazeVisible(showMazeChb.isSelected());

			}
		});
		viewPanel.add(showMazeChb);
				
		
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
		gbc_btnLoad.gridy = 5;
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
		gbc_btnSave.gridy = 6;
		contentPane.add(btnSave, gbc_btnSave);
	}

}
