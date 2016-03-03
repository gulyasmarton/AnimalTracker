package hu.elte.animaltracker.model.tracking.filtering;

import hu.elte.animaltracker.model.CustomisableProcess;
import ij.plugin.filter.RankFilters;
import ij.process.ImageProcessor;

/**
 * This is a wrapper class for the ImageJ MinimumFilter.
 * 
 *
 */
public class MinimumFilter extends MedianFilter {

	private static final long serialVersionUID = -2932516607969170550L;

	public MinimumFilter() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MinimumFilter(double radius) {
		super(radius);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public CustomisableProcess getNewInstance() {
		return new MinimumFilter();
	}

	@Override
	public String getName() {
		return "Minimum Filter";
	}
	
	@Override
	public ImageProcessor processImage(ImageProcessor ip) {
		filter.rank(ip, radius, RankFilters.MIN);
		return ip;
	}
}
