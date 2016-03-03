package hu.elte.animaltracker.model.tracking.filtering;

import java.io.IOException;
import java.io.ObjectInputStream;

import hu.elte.animaltracker.model.CustomisableProcess;
import hu.elte.animaltracker.model.tracking.SourceClassifier;
import ij.gui.GenericDialog;
import ij.plugin.filter.RankFilters;
import ij.process.ImageProcessor;

/**
 * This is a wrapper class for the ImageJ MedianFilter.
 * 
 *
 */
public class MedianFilter extends AbstractFilter {

	private static final long serialVersionUID = 3685009555001444269L;
	protected transient RankFilters filter;
	protected double radius;
	
	public MedianFilter(double radius) {
		this.radius = radius;
		filter = new RankFilters();
	}

	public MedianFilter() {
		this(2.0d);
	}
	
	private void readObject(ObjectInputStream ois)
			throws ClassNotFoundException, IOException {
		ois.defaultReadObject();
		filter = new RankFilters();
	}
	
	@Override
	public CustomisableProcess getNewInstance() {
		return new MedianFilter();
	}

	@Override
	public String getName() {
		return "Median Filter";
	}

	@Override
	public void showGUI() {
		GenericDialog gd = new GenericDialog(getName());

		gd.addNumericField("Radius: ", getRadius(), 2);
		gd.showDialog();
		if (gd.wasCanceled())
			return;

		double s = gd.getNextNumber();
		if (s > 0)
			setRadius(radius);
	}

	@Override
	public int getSupportedImageType() {
		return SourceClassifier.BYTE_IMAGE | SourceClassifier.SHORT_IMAGE
				| SourceClassifier.FLOAT_IMAGE | SourceClassifier.COLOR_IMAGE;
	}

	@Override
	public ImageProcessor processImage(ImageProcessor ip) {
		filter.rank(ip, radius, RankFilters.MEDIAN);
		return ip;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

}
