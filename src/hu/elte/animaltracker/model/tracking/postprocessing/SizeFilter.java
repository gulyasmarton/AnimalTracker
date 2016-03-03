package hu.elte.animaltracker.model.tracking.postprocessing;

import ij.gui.GenericDialog;

import java.io.Serializable;
import java.util.List;

import hu.elte.animaltracker.model.CustomisableProcess;
import hu.elte.animaltracker.model.tracking.blobdetecting.FloodFill;
import hu.elte.animaltracker.model.tracking.blobs.BaseBlob;
import hu.elte.animaltracker.model.tracking.thresholding.BooleanImage;

/**
 * This class removes any blob from the image the area of which lies outside of
 * the minimum-maximum range.
 * 
 * 
 */
public class SizeFilter implements PostProcessor, Serializable {

	private static final long serialVersionUID = -5493220834032738192L;
	protected Double min, max;

	public SizeFilter() {
		super();
	}

	/**
	 * Sets the minimum-maximum range.
	 * 
	 * @param min
	 *            if it is null then there is no minimum size limit.
	 * @param max
	 *            if it is null then there is no maximum size limit.
	 */
	public SizeFilter(Double min, Double max) {
		super();
		this.min = min;
		this.max = max;
	}

	@Override
	public CustomisableProcess getNewInstance() {
		return new SizeFilter(min, max);
	}

	@Override
	public String getName() {
		return "Size Filter";
	}

	@Override
	public void showGUI() {
		GenericDialog gd = new GenericDialog("Set blob size limits");
		double min = this.min != null ? this.min : 0;
		double max = this.max != null ? this.max : 0;
		gd.addNumericField("Maximum size: ", max, 0);
		gd.addNumericField("Minimum size: ", min, 0);
		gd.showDialog();

		if (gd.wasCanceled())
			return;

		max = gd.getNextNumber();
		min = gd.getNextNumber();
		this.max = (max > 0 ? max : null);
		this.min = (min > 0 ? min : null);
	}

	@Override
	public BooleanImage processImage(BooleanImage imp) {
		FloodFill floodFill = new FloodFill(imp.duplicate());
		List<BaseBlob> blobs = floodFill.getBlobs();

		for (BaseBlob b : blobs) {
			if (min != null && min >= b.getSize()) {
				imp.remove(b);
				continue;
			}
			if (max != null && max <= b.getSize()) {
				imp.remove(b);
				continue;
			}
		}

		return imp;
	}

	@Override
	public String toString() {
		return getName();
	}

	/**
	 * Returns the minimum size limit.
	 * 
	 * @return if it is null then there is no minimum size limit.
	 */
	public Double getMin() {
		return min;
	}

	/**
	 * Sets the minimum size limit.
	 * 
	 * @param min
	 *            if it is null then there is no minimum size limit.
	 */
	public void setMin(Double min) {
		this.min = (min == null || min == 0) ? null : min;
	}

	/**
	 * Returns the maximum size limit.
	 * 
	 * @return if it is null then there is no maximum size limit.
	 */
	public Double getMax() {
		return max;
	}

	/**
	 * Sets the maximum size limit.
	 * 
	 * @param min
	 *            if it is null then there is no maximum size limit.
	 */
	public void setMax(Double max) {
		this.max = (max == null || max == 0) ? null : max;
	}

}
