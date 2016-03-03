package hu.elte.animaltracker.model.tracking.postprocessing;

import hu.elte.animaltracker.model.CustomisableProcess;
import hu.elte.animaltracker.model.tracking.thresholding.BooleanImage;

/**
 * This is an implementation of the algorithm that performs morphological
 * closing.
 * 
 */
public class Close extends Dilate {

	public Close() {
		super();
	}

	/**
	 * Using this constructor you can set the number of iterations.
	 * 
	 * @param iteration
	 *            number of iterations.
	 */
	public Close(int iteration) {
		super(iteration);
	}

	@Override
	public CustomisableProcess getNewInstance() {
		return new Close(getIteration());
	}

	@Override
	public String getName() {
		return "Close";
	}

	@Override
	public BooleanImage processImage(BooleanImage imp) {
		Erode erode = new Erode(iteration);
		Dilate dilate = new Dilate(iteration);

		for (int i = 0; i < iteration; i++)
			imp = dilate.processImage(imp);

		for (int i = 0; i < iteration; i++)
			imp = erode.processImage(imp);

		return imp;
	}
}
