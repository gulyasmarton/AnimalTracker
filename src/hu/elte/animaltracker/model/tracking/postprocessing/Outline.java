package hu.elte.animaltracker.model.tracking.postprocessing;

import ij.gui.GenericDialog;
import hu.elte.animaltracker.model.CustomisableProcess;
import hu.elte.animaltracker.model.tracking.thresholding.BooleanImage;

/**
 * Returns inner or outer outlines.
 * 
 * 
 */
public class Outline implements PostProcessor {

	boolean inSide;

	public Outline() {
		inSide = true;
	}

	/**
	 * If the parameter is true, the inner outline is calculated. Otherwise, the
	 * outer outline is calculated.
	 * 
	 * @param inSide
	 */
	public Outline(boolean inSide) {
		super();
		this.inSide = inSide;
	}

	@Override
	public CustomisableProcess getNewInstance() {
		return new Outline(isInSide());
	}

	@Override
	public String getName() {
		return "Outline";
	}

	@Override
	public void showGUI() {
		GenericDialog gd = new GenericDialog("Oulining strategy");

		gd.addCheckbox("get inner outline", inSide);
		gd.showDialog();

		if (gd.wasCanceled())
			return;

		setInSide(gd.getNextBoolean());
	}

	@Override
	public BooleanImage processImage(BooleanImage imp) {
		int w = imp.getWidth();
		int h = imp.getHeight();
		BooleanImage outline = new BooleanImage(w, h);
		for (int y = 0; y < h; y++)
			for (int x = 0; x < w; x++)
				outline.getPixels()[x + y * w] = isValid(imp, x, y);
		return outline;
	}

	private boolean isValid(BooleanImage imp, int x, int y) {
		int w = imp.getWidth();
		int size = w * imp.getHeight();
		if (inSide != imp.getPixels()[x + y * w])
			return false;

		for (int yk = y - 1; yk <= y + 1; yk++)
			for (int xk = x - 1; xk <= x + 1; xk++) {
				int idx = xk + yk * w;
				if (idx < 0 || idx >= size)
					continue;
				if (imp.getPixels()[idx] != inSide)
					return true;

			}
		return false;
	}

	public boolean isInSide() {
		return inSide;
	}

	public void setInSide(boolean inSide) {
		this.inSide = inSide;
	}

	@Override
	public String toString() {
		return getName();
	}
}
