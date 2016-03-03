package hu.elte.animaltracker.model.tracking.filtering;

import ij.plugin.filter.RankFilters;
import ij.process.ImageProcessor;
import hu.elte.animaltracker.model.CustomisableProcess;

/**
 * This is a wrapper class for the ImageJ MeanFilter.
 * 
 * 
 */
public class MeanFilter extends MedianFilter {

	private static final long serialVersionUID = -1853733981196240468L;

	public MeanFilter() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MeanFilter(double radius) {
		super(radius);
		// TODO Auto-generated constructor stub
	}

	@Override
	public CustomisableProcess getNewInstance() {
		return new MeanFilter();
	}

	@Override
	public String getName() {
		return "Mean Filter";
	}

	@Override
	public ImageProcessor processImage(ImageProcessor ip) {
		filter.rank(ip, radius, RankFilters.MEAN);
		return ip;
	}

}
