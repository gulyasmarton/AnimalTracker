package hu.elte.animaltracker.controller.tracking;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.Roi;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

import hu.elte.animaltracker.controller.listeners.OverlayListener;
import hu.elte.animaltracker.model.tracking.ObjectLocation;
import hu.elte.animaltracker.model.tracking.TrackSequence;
import hu.elte.animaltracker.model.tracking.TrackingEditor;
import hu.elte.animaltracker.model.tracking.TrackingTask;

import javax.swing.DefaultListModel;


public class TrackingEditorController {
	protected DefaultListModel interModel = new DefaultListModel();
	protected TrackingEditor model;
	protected ImagePlus imp;
	protected boolean isAllowManualTracking;
	protected ObjectLocation[] locations;

	protected List<OverlayListener> listeners = new ArrayList<OverlayListener>();

	public TrackingEditorController(TrackingTask trackingTask) {
		imp = trackingTask.getImage();
		locations = trackingTask.getPoint();
		model = new TrackingEditor(locations);
	}

	public void addOverlayListener(OverlayListener listener) {
		listeners.add(listener);
	}

	public void removeOverlayListener(OverlayListener listener) {
		listeners.remove(listener);
	}

	protected void fireOverlayListener() {
		for (OverlayListener listener : listeners)
			listener.overlayDataChanged();
	}

	public DefaultListModel getInterModel() {
		return interModel;
	}

	public void interpolationGaps() {
		List<TrackSequence> track = model.findTrackSequence();
		if (track.size() < 2) {
			IJ.showMessage("There is no gap");
			return;
		}
		model.interpolationGaps(track);
		fireOverlayListener();
		IJ.showMessage("Fixed gap: " + track.size());

	}

	public void addPoint() {
		Roi r = imp.getRoi();
		if (r != null && r.getType() == Roi.POINT) {
			Polygon p = r.getPolygon();
			interModel.addElement(new InterpolPoint(p.xpoints[0], p.ypoints[0],
					imp.getCurrentSlice()));
		} else if (locations[imp.getCurrentSlice()] != null) {
			interModel.addElement(new InterpolPoint(locations[imp
					.getCurrentSlice()], imp.getCurrentSlice()));
		} else {
			IJ.error("Please select a point or set a tracked frame");
		}
	}

	public void removePoint(Object[] objects) {
		if (objects == null)
			return;
		for (Object obj : objects)
			interModel.removeElement(obj);

	}

	public void interpolationTracking() {
		if (imp == null) {
			IJ.error("No image");
			return;
		}
		if (interModel.getSize() < 2) {
			IJ.error("Need two points");
			return;
		}
		for (int i = 0; i < interModel.getSize() - 1; i++) {
			model.interPoltracking((InterpolPoint) interModel.get(i),
					(InterpolPoint) interModel.get(i + 1));
		}
		interModel.clear();
		fireOverlayListener();
	}

	public void deleteTrack() {
		if (imp == null) {
			IJ.error("No image");
			return;
		}
		if (interModel.getSize() < 2) {
			IJ.error("Need two points");
			return;
		}
		for (int i = 0; i < interModel.getSize() - 1; i++) {
			model.deleteTrack((InterpolPoint) interModel.get(i),
					(InterpolPoint) interModel.get(i + 1));
		}
		interModel.clear();
		fireOverlayListener();
	}

	public boolean isAllowManualTracking() {
		return isAllowManualTracking;
	}

	public void setAllowManualTracking(boolean isAllowManualTracking) {
		this.isAllowManualTracking = isAllowManualTracking;
	}

	public void manualTracking(int x, int y) {
		if (!isAllowManualTracking)
			return;
		model.manualTracking(imp.getCurrentSlice(), x, y);
		if (imp.getCurrentSlice() < imp.getStackSize())
			imp.setSlice(imp.getCurrentSlice() + 1);
		fireOverlayListener();
	}

}
