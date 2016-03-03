package hu.elte.animaltracker.view.analyzing;

import hu.elte.animaltracker.controller.analyzing.TrackAnalyzerController;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JTable;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.border.TitledBorder;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

public class TrackAnalyzerView extends JFrame {

	private static final long serialVersionUID = 1246830330337052729L;
	private JPanel contentPane;
	private JTable table;

	/**
	 * Create the frame.
	 */
	public TrackAnalyzerView(final TrackAnalyzerController trController) {
		setTitle("Tracking Analyzer");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 483, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel_north = new JPanel(new GridLayout(1, 3));
		contentPane.add(panel_north, BorderLayout.NORTH);

		JButton btnImportTrack = new JButton("Load track file");
		btnImportTrack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				trController.loadTrack();
			}
		});

		JButton btnLoadZones = new JButton("Load zones file");
		btnLoadZones.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				trController.loadZones();
			}
		});
		
		JButton btnLoadZoneSettings = new JButton("Load zone settings");
		btnLoadZoneSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				trController.openZoneSettings();
			}
		});
		
		JButton btnLoadConfigParameters= new JButton("Load param. config");
		btnLoadConfigParameters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				trController.openParameterConfigs();
			}
		});

		JButton btnSetImage = new JButton("Set active image");
		btnSetImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				trController.setActiveImage();
			}
		});

		JPanel loadPanel = new JPanel();
		loadPanel.setBorder(new TitledBorder(null, "Loadings",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_north.add(loadPanel);
		GridBagLayout gbl_loadPanel = new GridBagLayout();
		gbl_loadPanel.columnWidths = new int[] { 120 };
		gbl_loadPanel.rowHeights = new int[] { 0, 0 };
		gbl_loadPanel.columnWeights = new double[] { 0.0 };
		gbl_loadPanel.rowWeights = new double[] { 0.0, 0, 0 };
		loadPanel.setLayout(gbl_loadPanel);

		GridBagConstraints gbc_btnImportTrack = new GridBagConstraints();
		gbc_btnImportTrack.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnImportTrack.insets = new Insets(0, 0, 5, 0);
		gbc_btnImportTrack.gridx = 0;
		gbc_btnImportTrack.gridy = 0;
		loadPanel.add(btnImportTrack, gbc_btnImportTrack);

		GridBagConstraints gbc_btnLoadZones = new GridBagConstraints();
		gbc_btnLoadZones.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnLoadZones.insets = new Insets(0, 0, 5, 0);
		gbc_btnLoadZones.gridx = 0;
		gbc_btnLoadZones.gridy = 1;
		loadPanel.add(btnLoadZones, gbc_btnLoadZones);

		GridBagConstraints gbc_btnLoadZoneSettings = new GridBagConstraints();
		gbc_btnLoadZoneSettings.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnLoadZoneSettings.insets = new Insets(0, 0, 5, 0);
		gbc_btnLoadZoneSettings.gridx = 0;
		gbc_btnLoadZoneSettings.gridy = 2;
		loadPanel.add(btnLoadZoneSettings, gbc_btnLoadZoneSettings);
		
		GridBagConstraints gbc_btnLoadConfigParameterse = new GridBagConstraints();
		gbc_btnLoadConfigParameterse.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnLoadConfigParameterse.insets = new Insets(0, 0, 5, 0);
		gbc_btnLoadConfigParameterse.gridx = 0;
		gbc_btnLoadConfigParameterse.gridy = 3;
		loadPanel.add(btnLoadConfigParameters, gbc_btnLoadConfigParameterse);
		
		GridBagConstraints gbc_btnSetImage = new GridBagConstraints();
		gbc_btnSetImage.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSetImage.insets = new Insets(0, 0, 5, 0);
		gbc_btnSetImage.gridx = 0;
		gbc_btnSetImage.gridy = 4;
		loadPanel.add(btnSetImage, gbc_btnSetImage);
		
		
		JPanel settingsPanel = new JPanel();
		settingsPanel.setBorder(new TitledBorder(null, "Settings",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_north.add(settingsPanel);
		GridBagLayout gbl_settingsPanel = new GridBagLayout();
		gbl_settingsPanel.columnWidths = new int[] { 120 };
		gbl_settingsPanel.rowHeights = new int[] { 0 };
		gbl_settingsPanel.columnWeights = new double[] {0 };
		gbl_settingsPanel.rowWeights = new double[] {0,0 };
		settingsPanel.setLayout(gbl_settingsPanel);


		JButton btnEditZones = new JButton("Zone settings");
		btnEditZones.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				
				trController.showParameterSelector(table.getSelectedRows());
			}
		});
		
		JButton btnSetParameters = new JButton("Config parameters");
		btnSetParameters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				trController.showParametersSettings();
			}
		});
		
		GridBagConstraints gbc_btnEditZones = new GridBagConstraints();
		gbc_btnEditZones.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnEditZones.insets = new Insets(0, 0, 5, 0);
		gbc_btnEditZones.gridx = 0;
		gbc_btnEditZones.gridy = 0;
		settingsPanel.add(btnEditZones, gbc_btnEditZones);
		
		GridBagConstraints gbc_btnSetParameters = new GridBagConstraints();
		gbc_btnSetParameters.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSetParameters.insets = new Insets(0, 0, 5, 0);
		gbc_btnSetParameters.gridx = 0;
		gbc_btnSetParameters.gridy = 1;
		settingsPanel.add(btnSetParameters, gbc_btnSetParameters);
		
		JPanel savingsPanel = new JPanel();
		savingsPanel.setBorder(new TitledBorder(null, "Savings",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagLayout gbl_savingsPanel = new GridBagLayout();
		gbl_savingsPanel.columnWidths = new int[] { 120 };
		gbl_savingsPanel.rowHeights = new int[] { 0 };
		gbl_savingsPanel.columnWeights = new double[] {0 };
		gbl_savingsPanel.rowWeights = new double[] {0,0 };
		savingsPanel.setLayout(gbl_savingsPanel);
		panel_north.add(savingsPanel);
		
		JButton btnSaveConfigTrack = new JButton("Save param. config");
		btnSaveConfigTrack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				trController.saveParameterConfigs();
			}
		});
		
		JButton btnZoneSettingsTrack = new JButton("Save zone settings");
		btnZoneSettingsTrack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				trController.saveZoneSettings();
			}
		});
		

		GridBagConstraints gbc_btnSaveConfigTrack = new GridBagConstraints();
		gbc_btnSaveConfigTrack.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSaveConfigTrack.insets = new Insets(0, 0, 5, 0);
		gbc_btnSaveConfigTrack.gridx = 0;
		gbc_btnSaveConfigTrack.gridy = 0;
		savingsPanel.add(btnSaveConfigTrack, gbc_btnSaveConfigTrack);
		
		GridBagConstraints gbc_btnZoneSettingsTrack = new GridBagConstraints();
		gbc_btnZoneSettingsTrack.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnZoneSettingsTrack.insets = new Insets(0, 0, 5, 0);
		gbc_btnZoneSettingsTrack.gridx = 0;
		gbc_btnZoneSettingsTrack.gridy = 1;
		savingsPanel.add(btnZoneSettingsTrack, gbc_btnZoneSettingsTrack);
		
		
		JPanel panel_south = new JPanel();
		contentPane.add(panel_south, BorderLayout.SOUTH);

		JButton btnShowResults = new JButton("show results");
		btnShowResults.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				trController.showResult();

			}
		});
		panel_south.add(btnShowResults);
		
		JButton btnDone = new JButton("Done");
		btnDone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				trController.done();
				close();
			}
		});
		panel_south.add(btnDone);

		table = new JTable(trController.getModel());
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setFillsViewportHeight(true);
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {

					@Override
					public void valueChanged(ListSelectionEvent arg0) {
						int row = table.getSelectedRow();
						if (row == -1)
							return;
						trController.setSelectedRow(row);
					}
				});
		contentPane.add(new JScrollPane(table), BorderLayout.CENTER);
	}

	protected void close() {
		this.dispose();
	}

}
