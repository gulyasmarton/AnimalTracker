package hu.elte.animaltracker.model.tracking.postprocessing;

import ij.gui.GenericDialog;
import hu.elte.animaltracker.model.CustomisableProcess;
import hu.elte.animaltracker.model.tracking.thresholding.BooleanImage;

/**
 * This is an implementation of the algorithm that performs morphological
 * dilation.
 * 
 */
public class Dilate implements PostProcessor {

	int iteration = 1;

	public Dilate() {
		super();
	}

	/**
	 * Using this constructor you can set the number of iterations.
	 * 
	 * @param iteration
	 *            number of iterations.
	 */
	public Dilate(int iteration) {
		super();
		this.iteration = iteration;
	}

	@Override
	public CustomisableProcess getNewInstance() {
		return new Dilate(getIteration());
	}

	@Override
	public String getName() {
		return "Dilate";
	}

	@Override
	public void showGUI() {
		GenericDialog gd = new GenericDialog("Set number of iterations");
		gd.addNumericField("Iteration", getIteration(), 0);
		gd.showDialog();

		if (gd.wasCanceled())
			return;
		setIteration((int) Math.max(1, gd.getNextNumber()));
	}

	@Override
	public BooleanImage processImage(BooleanImage imp) {
		Outline outline = new Outline(false);

		for (int i = 0; i < iteration; i++) {
			BooleanImage outl = outline.processImage(imp);
			imp.or(outl);
		}
		return imp;
	}

	/**
	 * Returns the number of iterations.
	 * 
	 * @return
	 */
	public int getIteration() {
		return iteration;
	}

	/**
	 * Sets the number of iterations.
	 * 
	 * @param iteration
	 */
	public void setIteration(int iteration) {
		this.iteration = iteration;
	}

	@Override
	public String toString() {
		return getName();
	}
}
