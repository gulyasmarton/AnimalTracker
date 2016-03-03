package hu.elte.animaltracker.controller.tracking;

import java.util.ArrayList;
import java.util.List;

import hu.elte.animaltracker.controller.listeners.MedianImageControllerListener;
import hu.elte.animaltracker.model.tracking.filtering.BackgroundSubtractor;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.WindowManager;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;

import javax.swing.DefaultListModel;

public class BackgroundSubtractorController {
	protected class Frame {
		ImageProcessor imageProcessor;
		int frame;

		public Frame(ImageStack imageStack, int frame) {
			super();
			this.imageProcessor = imageStack.getProcessor(frame);
			this.frame = frame;
		}

		@Override
		public String toString() {
			return frame + ". frame";
		}
	}

	BackgroundSubtractor medianFilter;
	protected DefaultListModel listModel = new DefaultListModel();
	protected ImagePlus imp;
	protected ImagePlus medianImp;
	private List<MedianImageControllerListener> listener = new ArrayList<MedianImageControllerListener>();

	public BackgroundSubtractorController(ImagePlus imp,
			BackgroundSubtractor medianFilter) {
		this.imp = imp;
		medianFilter.clearFilters();
		this.medianFilter = medianFilter;

	}

	public BackgroundSubtractorController(BackgroundSubtractor medianFilter) {
		this(null, medianFilter);
	}

	public DefaultListModel getListModel() {
		return listModel;
	}

	public void addFrame() {
		if (imp == null) {
			IJ.noImage();
			return;
		}
		Frame frame = new Frame(imp.getStack(), imp.getCurrentSlice());
		listModel.addElement(frame);
	}

	public void removeFrame(Object[] objects) {
		if (objects == null)
			return;
		for (Object object : objects) {
			listModel.removeElement(object);
		}
	}

	private void firedMedianImageCallBack() {
		for (MedianImageControllerListener l : listener) {
			l.MedianImageCallBack();
		}
	}

	public void show() {
		if (!generateMedianImage())
			return;

		ImageProcessor ip = medianFilter.getFilterImage();

		if (ip == null)
			return;

		if (medianImp == null)
			medianImp = new ImagePlus("Median image", ip);
		else
			medianImp.setProcessor(ip);

		if (medianImp.getWindow() == null)
			medianImp.show();
		else
			medianImp.updateAndDraw();
	}

	private boolean generateMedianImage() {
		if (listModel.size() == 0)
			return false;
		medianFilter.clearFilters();
		for (int z = 0; z < listModel.size(); z++) {
			Frame frame = (Frame) listModel.get(z);
			medianFilter.addFrame(frame.imageProcessor);
		}

		return true;
	}

	public void addMedianImageControllerListener(MedianImageControllerListener l) {
		listener.add(l);
	}

	public void removeMedianImageControllerListener(
			MedianImageControllerListener l) {
		listener.remove(l);
	}

	public boolean finish() {
		if (!generateMedianImage())
			return false;
		firedMedianImageCallBack();
		return true;
	}

	public void setImage() {
		this.imp = WindowManager.getCurrentImage();

	}

}
