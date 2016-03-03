package hu.elte.animaltracker.view.tracking;

import hu.elte.animaltracker.controller.tracking.TrackerController;
import ij.IJ;

import java.awt.Desktop;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridBagLayout;

import javax.swing.JButton;

import java.awt.GridBagConstraints;

import javax.swing.JLabel;

import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JSpinner;
import javax.swing.JCheckBox;
import javax.swing.border.TitledBorder;

public class TrackerView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6675353572104859722L;
	private JPanel contentPane;
	JLabel lblFile;
	JSpinner maxSpinner;
	JSpinner minSpinner;
	JCheckBox chckbxAllowManualTracking;

	public TrackerView(final TrackerController controller) {
		setTitle("Tracker");
		final String link = "http://animaltracker.elte.hu/";

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 250, 650);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 120 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0 };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0,
				0.0, 0.0 };
		contentPane.setLayout(gbl_contentPane);

		JButton btnSetActiveImage = new JButton("Set active image");
		btnSetActiveImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.setActiveImage();
			}
		});
		GridBagConstraints gbl_btnSetActiveImage = new GridBagConstraints();
		gbl_btnSetActiveImage.fill = GridBagConstraints.HORIZONTAL;
		gbl_btnSetActiveImage.insets = new Insets(0, 0, 5, 0);
		gbl_btnSetActiveImage.gridx = 0;
		gbl_btnSetActiveImage.gridy = 0;
		contentPane.add(btnSetActiveImage, gbl_btnSetActiveImage);

		JButton btnSetTrackingArea = new JButton("Set tracking area");
		btnSetTrackingArea.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.setTrackingArea();
			}
		});
		GridBagConstraints gbl_btnSetTrackingArea = new GridBagConstraints();
		gbl_btnSetTrackingArea.fill = GridBagConstraints.HORIZONTAL;
		gbl_btnSetTrackingArea.insets = new Insets(0, 0, 5, 0);
		gbl_btnSetTrackingArea.gridx = 0;
		gbl_btnSetTrackingArea.gridy = 1;
		contentPane.add(btnSetTrackingArea, gbl_btnSetTrackingArea);

		JButton btnSetFilters = new JButton("Set filters");
		btnSetFilters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.showFiltersDialog();
			}
		});

		GridBagConstraints gbl_btnSetFilters = new GridBagConstraints();
		gbl_btnSetFilters.fill = GridBagConstraints.HORIZONTAL;
		gbl_btnSetFilters.insets = new Insets(0, 0, 5, 0);
		gbl_btnSetFilters.gridx = 0;
		gbl_btnSetFilters.gridy = 2;
		contentPane.add(btnSetFilters, gbl_btnSetFilters);

		JButton btnSetThreshold = new JButton("Set threshold");
		btnSetThreshold.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.showThresholdDlg();
			}
		});
		GridBagConstraints gbl_btnSetThreshold = new GridBagConstraints();
		gbl_btnSetThreshold.fill = GridBagConstraints.HORIZONTAL;
		gbl_btnSetThreshold.insets = new Insets(0, 0, 5, 0);
		gbl_btnSetThreshold.gridx = 0;
		gbl_btnSetThreshold.gridy = 3;
		contentPane.add(btnSetThreshold, gbl_btnSetThreshold);

		JButton btnPostProcessing = new JButton("Set post-processing");
		btnPostProcessing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.showPostPorcessingDlg();
			}
		});
		GridBagConstraints gbl_btnPostProcessing = new GridBagConstraints();
		gbl_btnPostProcessing.fill = GridBagConstraints.HORIZONTAL;
		gbl_btnPostProcessing.insets = new Insets(0, 0, 5, 0);
		gbl_btnPostProcessing.gridx = 0;
		gbl_btnPostProcessing.gridy = 4;
		contentPane.add(btnPostProcessing, gbl_btnPostProcessing);

		JButton btnBlobDetector = new JButton("Set blob-detector");
		btnBlobDetector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.showBlobDetectorDlg();
			}
		});
		GridBagConstraints gbl_btnBlobDetector = new GridBagConstraints();
		gbl_btnBlobDetector.fill = GridBagConstraints.HORIZONTAL;
		gbl_btnBlobDetector.insets = new Insets(0, 0, 5, 0);
		gbl_btnBlobDetector.gridx = 0;
		gbl_btnBlobDetector.gridy = 5;
		contentPane.add(btnBlobDetector, gbl_btnBlobDetector);

		JButton btnSetComperator = new JButton("Set comperator");
		btnSetComperator.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.showComperatorsDlg();
			}
		});
		GridBagConstraints gbl_btnSetComperator = new GridBagConstraints();
		gbl_btnSetComperator.fill = GridBagConstraints.HORIZONTAL;
		gbl_btnSetComperator.insets = new Insets(0, 0, 5, 0);
		gbl_btnSetComperator.gridx = 0;
		gbl_btnSetComperator.gridy = 6;
		contentPane.add(btnSetComperator, gbl_btnSetComperator);

		JButton btnSetLastFrame = new JButton("Set last frame");
		btnSetLastFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.showLastFrameDialog();
			}
		});
		GridBagConstraints gbl_btnSetLastFrame = new GridBagConstraints();
		gbl_btnSetLastFrame.fill = GridBagConstraints.HORIZONTAL;
		gbl_btnSetLastFrame.insets = new Insets(0, 0, 5, 0);
		gbl_btnSetLastFrame.gridx = 0;
		gbl_btnSetLastFrame.gridy = 7;
		contentPane.add(btnSetLastFrame, gbl_btnSetLastFrame);

		JButton btnShowBlobs = new JButton("Show blobs");
		btnShowBlobs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.showBlobs();
			}
		});
		GridBagConstraints gbl_btnShowBlobs = new GridBagConstraints();
		gbl_btnShowBlobs.fill = GridBagConstraints.HORIZONTAL;
		gbl_btnShowBlobs.insets = new Insets(0, 0, 5, 0);
		gbl_btnShowBlobs.gridx = 0;
		gbl_btnShowBlobs.gridy = 8;
		contentPane.add(btnShowBlobs, gbl_btnShowBlobs);

		JButton btnStartTracking = new JButton("Start tracking");
		btnStartTracking.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.startTracking();
			}
		});
		GridBagConstraints gbl_btnStartTracking = new GridBagConstraints();
		gbl_btnStartTracking.fill = GridBagConstraints.HORIZONTAL;
		gbl_btnStartTracking.insets = new Insets(10, 0, 5, 0);
		gbl_btnStartTracking.gridx = 0;
		gbl_btnStartTracking.gridy = 9;
		contentPane.add(btnStartTracking, gbl_btnStartTracking);

		JButton btnEditTracking = new JButton("Edit tracking");
		btnEditTracking.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.editTrack();
			}
		});
		GridBagConstraints gbl_btnEditTracking = new GridBagConstraints();
		gbl_btnEditTracking.fill = GridBagConstraints.HORIZONTAL;
		gbl_btnEditTracking.insets = new Insets(0, 0, 5, 0);
		gbl_btnEditTracking.gridx = 0;
		gbl_btnEditTracking.gridy = 10;
		contentPane.add(btnEditTracking, gbl_btnEditTracking);

		JPanel trackingTaskPanel = new JPanel();
		trackingTaskPanel.setBorder(new TitledBorder(null,
				"Tracking Task file", TitledBorder.LEADING, TitledBorder.TOP,
				null, null));
		GridBagConstraints gbl_trackingTaskPanel = new GridBagConstraints();
		gbl_trackingTaskPanel.fill = GridBagConstraints.HORIZONTAL;
		gbl_trackingTaskPanel.insets = new Insets(0, 0, 5, 0);
		gbl_trackingTaskPanel.gridx = 0;
		gbl_trackingTaskPanel.gridy = 12;
		contentPane.add(trackingTaskPanel, gbl_trackingTaskPanel);
		JButton btnOpenTrackingTask = new JButton("Open");
		btnOpenTrackingTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.openTackingTask();
			}
		});
		JButton btnSaveTrackingTask = new JButton("Save");
		btnSaveTrackingTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.saveTackingTask();
			}
		});
		trackingTaskPanel.add(btnOpenTrackingTask);
		trackingTaskPanel.add(btnSaveTrackingTask);

		JPanel trackingConfigPanel = new JPanel();
		trackingConfigPanel.setBorder(new TitledBorder(null,
				"Tracking Config file", TitledBorder.LEADING, TitledBorder.TOP,
				null, null));
		GridBagConstraints gbl_trackingConfigPanel = new GridBagConstraints();
		gbl_trackingConfigPanel.fill = GridBagConstraints.HORIZONTAL;
		gbl_trackingConfigPanel.insets = new Insets(10, 0, 5, 0);
		gbl_trackingConfigPanel.gridx = 0;
		gbl_trackingConfigPanel.gridy = 11;
		contentPane.add(trackingConfigPanel, gbl_trackingConfigPanel);
		JButton btnOpenTrackingConfig = new JButton("Open");
		btnOpenTrackingConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.openTrackingConfig();
			}
		});
		JButton btnSaveTrackingConfig = new JButton("Save");
		btnSaveTrackingConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.saveTrackingConfig();
			}
		});
		trackingConfigPanel.add(btnOpenTrackingConfig);
		trackingConfigPanel.add(btnSaveTrackingConfig);

		JButton btnExportTracking = new JButton("Export tracking");
		btnExportTracking.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.exportTrack();
			}
		});
		GridBagConstraints gbl_btnExportTracking = new GridBagConstraints();
		gbl_btnExportTracking.fill = GridBagConstraints.HORIZONTAL;
		gbl_btnExportTracking.insets = new Insets(10, 0, 5, 0);
		gbl_btnExportTracking.gridx = 0;
		gbl_btnExportTracking.gridy = 13;
		contentPane.add(btnExportTracking, gbl_btnExportTracking);

		JButton btnDone = new JButton("Done");
		btnDone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.trackControllerReturn();
				close();
			}
		});
		GridBagConstraints gbl_btnDone = new GridBagConstraints();
		gbl_btnDone.fill = GridBagConstraints.HORIZONTAL;
		gbl_btnDone.insets = new Insets(0, 0, 5, 0);
		gbl_btnDone.gridx = 0;
		gbl_btnDone.gridy = 14;
		contentPane.add(btnDone, gbl_btnDone);

		JButton btnHelp = new JButton("Help");
		btnHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!Desktop.isDesktopSupported()) {
					IJ.error("Auto link is not working! Please open it manually: "
							+ link);
					return;
				}
				Desktop desktop = java.awt.Desktop.getDesktop();
				URI uri;
				try {
					uri = new URI(link);
					desktop.browse(uri);
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		});
		GridBagConstraints gbl_btnHelp = new GridBagConstraints();
		// gbl_btnHelp.fill = GridBagConstraints.HORIZONTAL;
		gbl_btnHelp.insets = new Insets(0, 0, 5, 0);
		gbl_btnHelp.gridx = 0;
		gbl_btnHelp.gridy = 15;
		contentPane.add(btnHelp, gbl_btnHelp);
	}

	protected void close() {
		super.dispose();
	}
}
