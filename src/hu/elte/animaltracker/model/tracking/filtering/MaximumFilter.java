package hu.elte.animaltracker.model.tracking.filtering;

import hu.elte.animaltracker.model.CustomisableProcess;
import ij.plugin.filter.RankFilters;
import ij.process.ImageProcessor;

/**
 * This is a wrapper class for the ImageJ MaximumFilter.
 * 
 * 
 */
public class MaximumFilter extends MedianFilter {

	private static final long serialVersionUID = 2811462251321746452L;

	public MaximumFilter() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MaximumFilter(double radius) {
		super(radius);
		// TODO Auto-generated constructor stub
	}

	@Override
	public CustomisableProcess getNewInstance() {
		return new MaximumFilter();
	}

	@Override
	public String getName() {
		return "Maximum Filter";
	}

	@Override
	public ImageProcessor processImage(ImageProcessor ip) {
		filter.rank(ip, radius, RankFilters.MAX);
		return ip;
	}
}
