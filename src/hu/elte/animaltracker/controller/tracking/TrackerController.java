package hu.elte.animaltracker.controller.tracking;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import hu.elte.animaltracker.controller.listeners.OverlayListener;
import hu.elte.animaltracker.controller.listeners.TrackerControllerListener;
import hu.elte.animaltracker.controller.listeners.TrackingProcessListener;
import hu.elte.animaltracker.controller.listeners.ZoneDesignerControllerListener;
import hu.elte.animaltracker.controller.zones.ZoneDesignerController;
import hu.elte.animaltracker.model.CustomisableProcess;
import hu.elte.animaltracker.model.TrackingIO;
import hu.elte.animaltracker.model.tracking.CoreTracker;
import hu.elte.animaltracker.model.tracking.ObjectLocation;
import hu.elte.animaltracker.model.tracking.TrackSequence;
import hu.elte.animaltracker.model.tracking.TrackingTask;
import hu.elte.animaltracker.model.tracking.blobdetecting.BlobComparator;
import hu.elte.animaltracker.model.tracking.blobdetecting.BaseBlobDetector;
import hu.elte.animaltracker.model.tracking.blobdetecting.BlobDetector;
import hu.elte.animaltracker.model.tracking.blobdetecting.SimpleDistanceComparator;
import hu.elte.animaltracker.model.tracking.blobs.BaseBlob;
import hu.elte.animaltracker.model.tracking.filtering.ColorBackgroundSubtractor;
import hu.elte.animaltracker.model.tracking.filtering.GaussianBlurFilter;
import hu.elte.animaltracker.model.tracking.filtering.BackgroundSubtractor;
import hu.elte.animaltracker.model.tracking.filtering.AbstractFilter;
import hu.elte.animaltracker.model.tracking.filtering.MaximumFilter;
import hu.elte.animaltracker.model.tracking.filtering.MeanFilter;
import hu.elte.animaltracker.model.tracking.filtering.MedianFilter;
import hu.elte.animaltracker.model.tracking.filtering.MinimumFilter;
import hu.elte.animaltracker.model.tracking.postprocessing.Close;
import hu.elte.animaltracker.model.tracking.postprocessing.Dilate;
import hu.elte.animaltracker.model.tracking.postprocessing.Erode;
import hu.elte.animaltracker.model.tracking.postprocessing.Open;
import hu.elte.animaltracker.model.tracking.postprocessing.Outline;
import hu.elte.animaltracker.model.tracking.postprocessing.PostProcessor;
import hu.elte.animaltracker.model.tracking.postprocessing.SizeFilter;
import hu.elte.animaltracker.model.tracking.thresholding.ColorThresholder;
import hu.elte.animaltracker.model.tracking.thresholding.GrayscaleThresholder;
import hu.elte.animaltracker.model.tracking.thresholding.AbstractThresholder;
import hu.elte.animaltracker.model.zones.ZoneUnit;
import hu.elte.animaltracker.view.tracking.ProcessDialog;
import hu.elte.animaltracker.view.tracking.TrackerView;
import hu.elte.animaltracker.view.tracking.TrackingEditorView;
import hu.elte.animaltracker.view.tracking.TrackingPorcessImageWindow;
import hu.elte.animaltracker.view.zones.ZoneDesignerView;
import ij.IJ;
import ij.ImageListener;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.GenericDialog;
import ij.gui.ImageCanvas;
import ij.gui.ImageWindow;
import ij.gui.Overlay;
import ij.gui.Roi;
import ij.plugin.AVI_Reader;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;

public class TrackerController implements PlugIn, MouseListener, ImageListener,
		TrackingProcessListener {

	/**
	 * Active image
	 */
	protected ImagePlus imp;

	/**
	 * Image for thresholding
	 */
	protected ImagePlus impFiltered;

	/**
	 * Image canvas need for mouse events
	 */
	protected ImageCanvas canvas;

	/**
	 * All possible tracking start point
	 */
	protected List<BaseBlob> firstBlobs;

	/**
	 * Mouse last click postition on the canvas
	 */
	protected Point clickPosition;

	protected CoreTracker coreTracker;
	protected TrackingTask trackingTask;
	protected TrackingEditorController trackingEditorController;

	protected List<TrackerControllerListener> liseners = new ArrayList<TrackerControllerListener>();

	protected List<AbstractFilter> currentFilters;
	protected List<AbstractFilter> availableFilters;

	protected List<PostProcessor> currentPostProcesses;
	protected List<PostProcessor> availablePostProcesses;

	protected List<BlobComparator> availableBlobComparator;

	protected TrackingPorcessImageWindow window;

	protected List<BlobDetector> availableBlobDetector;

	protected List<AbstractThresholder> availableThresholder;

	public TrackerController() {
		super();

		currentFilters = new ArrayList<AbstractFilter>();
		availableFilters = new ArrayList<AbstractFilter>();
		availableFilters.add(new BackgroundSubtractor(currentFilters));
		availableFilters.add(new GaussianBlurFilter());
		availableFilters.add(new MeanFilter());
		availableFilters.add(new MedianFilter());
		availableFilters.add(new MaximumFilter());
		availableFilters.add(new MinimumFilter());

		availableThresholder = new ArrayList<AbstractThresholder>();
		availableThresholder.add(new GrayscaleThresholder());
		availableThresholder.add(new ColorThresholder());

		currentPostProcesses = new ArrayList<PostProcessor>();
		availablePostProcesses = new ArrayList<PostProcessor>();
		availablePostProcesses.add(new SizeFilter());
		availablePostProcesses.add(new Erode());
		availablePostProcesses.add(new Dilate());
		availablePostProcesses.add(new Open());
		availablePostProcesses.add(new Close());
		availablePostProcesses.add(new Outline());

		availableBlobComparator = new ArrayList<BlobComparator>();
		availableBlobComparator.add(new SimpleDistanceComparator());

		availableBlobDetector = new ArrayList<BlobDetector>();
		availableBlobDetector.add(new BaseBlobDetector());

		coreTracker = new CoreTracker();
		coreTracker.setFilters(currentFilters);
		coreTracker.setPostProcessors(currentPostProcesses);
		coreTracker.setBlobComparator(availableBlobComparator.get(0));
		coreTracker.setBlobDetector(availableBlobDetector.get(0));
		coreTracker.setThresholder(availableThresholder.get(0));

		IJ.register(MouseListener.class);
	}

	public TrackerController(TrackingTask task) {
		this();
		setTrackingTask(task);
	}

	public TrackerController(ImagePlus imp) {
		this();
		if (setImage(imp))
			trackingTask = new TrackingTask(imp, coreTracker);
	}

	public void autoLoadTester(String path) {
		AVI_Reader avi = new AVI_Reader();
		new ImagePlus("temp", avi.makeStack(path, 1, -1, true, true, false))
				.show();
		setActiveImage();
	}

	public void addTrackerControllerListener(TrackerControllerListener listener) {
		liseners.add(listener);
	}

	public void removeTrackerControllerListener(
			TrackerControllerListener listener) {
		liseners.remove(listener);
	}

	private void firedActiveImageChanged() {
		for (TrackerControllerListener l : liseners) {
			l.activeImageChanged(imp);
		}
	}

	public boolean setActiveImage() {
		ImagePlus imp = WindowManager.getCurrentImage();
		if (setImage(imp)) {
			trackingTask = new TrackingTask(imp, coreTracker);
			imageUpdated(imp);
			return true;
		}
		return false;
	}

	public boolean setImage(ImagePlus imp) {
		if (imp == null) {
			IJ.noImage();
			return false;
		}
		ImagePlus.removeImageListener(this);
		ImagePlus.addImageListener(this);

		if (canvas != null)
			canvas.removeMouseListener(this);

		if (imp.isHyperStack()) {
			IJ.error("This is a hyperstack image!");
			this.imp = null;
		} else if (imp.getStackSize() == 1) {
			IJ.error("it is not a stack image");
			this.imp = null;
		} else {
			if (imp.getWindow() == null)
				imp.show();
			ImageWindow win = imp.getWindow();
			canvas = win.getCanvas();
			canvas.removeMouseListener(this);
			canvas.addMouseListener(this);
			this.imp = imp;

			List<AbstractThresholder> list = new ArrayList<AbstractThresholder>();

			for (AbstractThresholder thresholder : availableThresholder) {
				if (thresholder.isSupported(imp))
					list.add(thresholder);
			}
			if (list.size() == 0) {
				IJ.error("No source supported thresholder. Please reconfig the plugin.");
				return false;
			}
			if (coreTracker.getThresholder() == null)
				coreTracker.setThresholder(list.get(0));

		}
		firedActiveImageChanged();
		if (this.imp != null)
			return true;
		return false;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		int offscreenX = canvas.offScreenX(x);
		int offscreenY = canvas.offScreenY(y);
		if (firstBlobs != null && trackingTask != null
				&& trackingTask.getFirstFrame() == imp.getCurrentSlice()) {
			clickPosition = new Point(offscreenX, offscreenY);
			setOverlayer();
		}
		if (trackingEditorController != null)
			trackingEditorController.manualTracking(offscreenX, offscreenY);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	public void setTrackigArea(ZoneUnit trackigArea) {
		if (trackingTask == null)
			return;
		trackingTask.setTrackingArea(trackigArea);
		setOverlayer();
	}

	public ZoneUnit getTrackingArea() {
		if (trackingTask == null)
			return null;
		return trackingTask.getTrackingArea();
	}

	public void showBlobs() {
		if (imp == null) {
			IJ.error("No image");
			return;
		}
		if (coreTracker.getThresholder() == null) {
			IJ.error("Please set the thresholds!");
			return;
		}

		firstBlobs = coreTracker.getBlobs(imp.getStack().getProcessor(
				imp.getCurrentSlice()));

		trackingTask.setFirstFrame(imp.getCurrentSlice());
		coreTracker.setReferenceBlob(null);
		clickPosition = null;
		if (firstBlobs.size() == 0) {
			firstBlobs = null;
			IJ.error("There is no any trackable object!");
			return;
		}
		setOverlayer();

	}

	private void setOverlayer() {
		if (imp == null || trackingTask == null)
			return;

		Overlay ov = new Overlay();
		if (trackingTask.getTrackingArea() != null) {
			Roi maskRoi = trackingTask.getTrackingArea().getRoi();
			maskRoi.setPosition(0);
			maskRoi.setStrokeWidth(2f);
			maskRoi.setStrokeColor(Color.green);
			ov.add(maskRoi);
		}
		if (firstBlobs != null) {
			for (BaseBlob blob : firstBlobs) {
				Roi r = new Roi(blob.getBound());
				r.setPosition(trackingTask.getFirstFrame());
				r.setStrokeColor(Color.yellow);
				r.setStrokeWidth(1d);
				if (clickPosition != null
						&& r.contains(clickPosition.x, clickPosition.y)) {
					r.setStrokeWidth(2d);
					r.setStrokeColor(Color.red);
					coreTracker.setReferenceBlob(blob);
					clickPosition = null;
				}
				ov.add(r);
			}
		}

		if (trackingTask.getPoint() != null) {
			List<TrackSequence> track = TrackSequence
					.getConnectedSequences(trackingTask.getPoint());

			for (TrackSequence ts : track) {
				if (ts.size() < 2)
					continue;

				for (ObjectLocation ol : ts) {
					Roi r = ts.getRoiByIndex(ol.frame);
					r.setStrokeColor(Color.yellow);
					r.setPosition(ol.frame);
					ov.add(r);
				}

			}
		}

		imp.setOverlay(ov);
		cloneOverleyerToFilteredImg();
	}

	public void exportTrack() {
		if (imp == null || trackingTask == null) {
			IJ.error("No image");
			return;
		}

		String path = TrackingIO.commonSaveDialog("Save track file ...", null,
				"-track.txt", ".txt");

		if (TrackingIO.saveTrack(trackingTask.getPoint(), path))
			IJ.showMessage("Done!");
		else
			IJ.error("I/O error");
	}

	public void startTracking() {

		if (imp == null || trackingTask == null) {
			IJ.error("No image");
			return;
		}

		if (coreTracker.getThresholder() == null) {
			IJ.error("Please set the thresholds!");
			return;
		}

		if (coreTracker.getReferenceBlob() == null) {
			IJ.error("No start object selected");
			return;
		}

		final TrackerController ctrl = this;
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				TrackingProcessController controller = new TrackingProcessController(
						trackingTask);
				controller.addTrackingProcessListener(ctrl);
			}
		});
	}

	@Override
	public void imageOpened(ImagePlus imp) {
	}

	@Override
	public void imageClosed(ImagePlus imp) {
		if (imp.equals(impFiltered))
			impFiltered = null;
		if (!this.imp.equals(imp))
			return;
		imp = null;
		firedActiveImageChanged();
	}

	@Override
	public void imageUpdated(ImagePlus imp) {
		if (!imp.equals(this.imp))
			return;

		ImageProcessor ip = imp.getProcessor();

		if (impFiltered == null || impFiltered.getWindow() == null) {
			impFiltered = new ImagePlus("Porcessing Window", ip);
			window = new TrackingPorcessImageWindow(impFiltered, coreTracker);
			window.setVisible(true);
		} else {
			window.setProcessor(ip);
		}

		cloneOverleyerToFilteredImg();
	}

	private void cloneOverleyerToFilteredImg() {
		if (impFiltered == null)
			return;
		Overlay overlayOrg = imp.getOverlay();
		Overlay overlayFilter = new Overlay();
		if (overlayOrg == null)
			return;
		Roi[] rois = overlayOrg.toArray();
		int idx = imp.getCurrentSlice();
		for (Roi roi : rois) {
			if (roi.getPosition() == 0)
				overlayFilter.add(roi);
			else if (roi.getPosition() == idx) {
				Roi clone = (Roi) roi.clone();
				clone.setPosition(0);
				overlayFilter.add(clone);
			}
		}
		impFiltered.setOverlay(overlayFilter);
	}

	@Override
	public void run(String arg) {
		TrackerView trackerView = new TrackerView(this);
		trackerView.setVisible(true);
	}

	private void fireTrackerControllerReturned() {
		for (TrackerControllerListener listener : liseners)
			listener.TrackerControllerReturned();
	}

	public void trackControllerReturn() {
		ImagePlus.removeImageListener(this);

		if (canvas != null)
			canvas.removeMouseListener(this);
		if (window != null)
			window.close();

		fireTrackerControllerReturned();
	}

	public void showLastFrameDialog() {
		if (imp == null || trackingTask == null) {
			IJ.error("No image");
			return;
		}

		GenericDialog gd = new GenericDialog("Set last frame");
		gd.addNumericField("Frame number", imp.getCurrentSlice(), 0);
		gd.showDialog();
		if (gd.wasCanceled())
			return;

		int lastFrame = (int) gd.getNextNumber();
		if (lastFrame > imp.getStackSize())
			lastFrame = imp.getStackSize();

		trackingTask.setLastFrame(lastFrame);
	}

	public void editTrack() {
		if(imp==null){
			IJ.noImage();
			return;
		}
		trackingEditorController = new TrackingEditorController(trackingTask);
		TrackingEditorView trackingEditorView = new TrackingEditorView(
				trackingEditorController);
		trackingEditorView.setVisible(true);
		trackingEditorController.addOverlayListener(new OverlayListener() {

			@Override
			public void overlayDataChanged() {
				setOverlayer();

			}
		});
	}

	public TrackingTask getTrackingTask() {
		return trackingTask;
	}

	public boolean setTrackingTask(TrackingTask trackingTask) {

		coreTracker = trackingTask.getCoreTracker();
		currentFilters = coreTracker.getFilters();
		currentPostProcesses = coreTracker.getPostProcessors();

		if (!setImage(trackingTask.getImage())) {
			IJ.error("Error during the image loading");
			return false;
		}
		this.trackingTask = trackingTask;
		firedActiveImageChanged();
		setOverlayer();
		return true;
	}

	@Override
	public void trackingCanceled(int frame) {
		setOverlayer();
		imp.setSlice(frame);
	}

	@Override
	public void trackingFinished() {
		setOverlayer();
		imp.setSlice(trackingTask.getLastFrame());
		IJ.showMessage("Trakcing finished.");
	}

	public void showThresholdDlg() {
		if (imp == null) {
			IJ.error("No image");
			return;
		}
		imageUpdated(imp);

		List<AbstractThresholder> list = new ArrayList<AbstractThresholder>();

		for (AbstractThresholder thresholder : availableThresholder) {
			if (thresholder.isSupported(imp))
				list.add(thresholder);
		}
		if (list.size() == 0) {
			IJ.error("No source supported thresholder. Please reconfig the plugin.");
			return;
		}

		AbstractThresholder c = (AbstractThresholder) JOptionPane
				.showInputDialog(null, "Select a Thresholder method:",
						"Thresholders", JOptionPane.QUESTION_MESSAGE, null,
						list.toArray(), coreTracker.getThresholder());

		if (c == null)
			return;

		c = (AbstractThresholder) c.getNewInstance();
		coreTracker.setThresholder(c);
		((CustomisableProcess) c).showGUI();
	}

	public void showFiltersDialog() {
		ProcessDialog dialog = new ProcessDialog("Filter Settings",
				availableFilters, currentFilters);
		dialog.setVisible(true);
	}

	public void showPostPorcessingDlg() {
		ProcessDialog dialog = new ProcessDialog("Post-process Settings",
				availablePostProcesses, currentPostProcesses);
		dialog.setVisible(true);
	}

	public void showComperatorsDlg() {
		BlobComparator c = (BlobComparator) JOptionPane.showInputDialog(null,
				"Select a Blob Comparator method:", "Blob Comparators",
				JOptionPane.QUESTION_MESSAGE, null,
				availableBlobComparator.toArray(),
				coreTracker.getBlobComparator());
		if (c == null)
			return;
		c = (BlobComparator) c.getNewInstance();
		if (coreTracker.getBlobComparator() == null
				|| !(c.getClass().equals(coreTracker.getBlobComparator()
						.getClass())))
			coreTracker.setBlobComparator(c);
		((CustomisableProcess) coreTracker.getBlobComparator()).showGUI();

	}

	public void openTrackingConfig() {
		String path = TrackingIO.commonOpenDialog("Open tracking config", null,
				"config.tcf");

		if (path == null)
			return;

		CoreTracker ct = TrackingIO.openCoreTracking(path);
		if (ct == null) {
			IJ.error("Loading error");
			return;
		}
		setCoreTracker(ct);
	}

	public void setCoreTracker(CoreTracker coreTracker) {
		currentFilters = coreTracker.getFilters();
		currentPostProcesses = coreTracker.getPostProcessors();
		if (trackingTask != null)
			trackingTask.setCoreTracker(coreTracker);
		this.coreTracker = coreTracker;

	}

	public void saveTrackingConfig() {
		String path = TrackingIO.commonSaveDialog("Save tracking config", null,
				"config.tcf", ".tcf");

		if (path != null)
			if (TrackingIO.saveCoreTracking(path, coreTracker))
				IJ.showMessage("Saving finished.");
	}

	public void setTrackingArea() {
		if (imp == null) {
			IJ.error("No image");
			return;
		}
		ZoneDesignerController zoneDesignerController = new ZoneDesignerController(
				imp);
		zoneDesignerController
				.addZoneDesignerControllerListener(new ZoneDesignerControllerListener() {

					@Override
					public void zoneSelected(ZoneUnit zoneUnit) {
						setTrackigArea(zoneUnit);
						if (coreTracker.getThresholder() != null)
							coreTracker.getThresholder().setMask(zoneUnit,
									imp.getWidth(), imp.getHeight());
						setOverlayer();
					}
				});
		ZoneDesignerView zoneDesigner2View = new ZoneDesignerView(
				zoneDesignerController);
		zoneDesigner2View.setVisible(true);

	}

	public void showBlobDetectorDlg() {
		BlobDetector c = (BlobDetector) JOptionPane.showInputDialog(null,
				"Select a Blob Detector method:", "Blob Detectors",
				JOptionPane.QUESTION_MESSAGE, null,
				availableBlobDetector.toArray(), coreTracker.getBlobDetector());
		if (c == null)
			return;
		c = (BlobDetector) c.getNewInstance();
		coreTracker.setBlobDetector(c);
		((CustomisableProcess) c).showGUI();

	}

	public void openTackingTask() {
		String path = TrackingIO.commonOpenDialog("Open Tracking Task", null,
				"config.ttf");

		if (path == null)
			return;

		TrackingTask tt = TrackingIO.openTrackingTask(path);
		if (tt == null) {
			IJ.error("Loading error");
			return;
		}
		if (tt.getImage() != null)
			tt.getImage().show();

		setTrackingTask(tt);

	}

	public void saveTackingTask() {
		if (trackingTask == null) {
			IJ.noImage();
			return;
		}
		String path = TrackingIO.commonSaveDialog("Save Tracking Task", null,
				"config.ttf", ".ttf");

		if (path != null)
			if (TrackingIO.saveTrackingTask(path, trackingTask))
				IJ.showMessage("Saving finished.");

	}

}
