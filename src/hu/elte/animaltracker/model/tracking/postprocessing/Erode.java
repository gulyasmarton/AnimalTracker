package hu.elte.animaltracker.model.tracking.postprocessing;

import hu.elte.animaltracker.model.CustomisableProcess;
import hu.elte.animaltracker.model.tracking.thresholding.BooleanImage;

/**
 * This is an implementation of the algorithm that performs morphological erosion.
 * 
 */
public class Erode extends Dilate {

	public Erode() {
		super();
	}

	/**
	 * Using this constructor you can set the number of iterations.
	 * 
	 * @param iteration
	 *            number of iterations
	 */
	public Erode(int iteration) {
		super(iteration);
	}

	@Override
	public CustomisableProcess getNewInstance() {
		return new Erode(getIteration());
	}

	@Override
	public String getName() {
		return "Erode";
	}

	@Override
	public BooleanImage processImage(BooleanImage imp) {
		Outline outline = new Outline(true);

		for (int i = 0; i < iteration; i++) {
			BooleanImage outl = outline.processImage(imp);
			imp.not(outl);
		}
		return imp;
	}
}
