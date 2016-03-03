package hu.elte.animaltracker.model.tracking.filtering;

import java.io.IOException;
import java.io.ObjectInputStream;

import hu.elte.animaltracker.model.tracking.SourceClassifier;
import ij.gui.GenericDialog;
import ij.plugin.filter.GaussianBlur;
import ij.process.ImageProcessor;

/**
 * This is a wrapper class for the ImageJ GaussianBlur.
 * 
 * @see ij.plugin.filter.GaussianBlur
 */
public class GaussianBlurFilter extends AbstractFilter {
	private static final long serialVersionUID = 4069940412578044165L;
	protected transient GaussianBlur gaussianBlur;
	protected double sigma;

	public GaussianBlurFilter(double sigma) {
		this.sigma = sigma;
		gaussianBlur = new GaussianBlur();
	}

	public GaussianBlurFilter() {
		this(2.0d);
	}

	private void readObject(ObjectInputStream ois)
			throws ClassNotFoundException, IOException {
		ois.defaultReadObject();
		gaussianBlur = new GaussianBlur();
	}

	@Override
	public ImageProcessor processImage(ImageProcessor ip) {
		gaussianBlur.blurGaussian(ip, sigma, sigma, 0.02);
		return ip;
	}

	/**
	 * Returns the sigma value of the GaussianBlur filter.
	 * 
	 * @see ij.plugin.filter.GaussianBlur#blurGaussian(ImageProcessor, double,
	 *      double, double)
	 * @return
	 */
	public double getSigma() {
		return sigma;
	}

	/**
	 * Sets the sigma value of the GaussianBlur filter.
	 * 
	 * @see ij.plugin.filter.GaussianBlur#blurGaussian(ImageProcessor, double,
	 *      double, double)
	 * @return
	 */
	public void setSigma(double sigma) {
		this.sigma = sigma;
	}

	@Override
	public int getSupportedImageType() {
		return SourceClassifier.BYTE_IMAGE | SourceClassifier.SHORT_IMAGE
				| SourceClassifier.FLOAT_IMAGE | SourceClassifier.COLOR_IMAGE;
	}

	@Override
	public String getName() {
		return "Gaussian Blur";
	}

	@Override
	public void showGUI() {
		GenericDialog gd = new GenericDialog("Gaussian Blur");

		gd.addNumericField("Sigma: ", getSigma(), 2);
		gd.showDialog();
		if (gd.wasCanceled())
			return;

		double s = gd.getNextNumber();
		if (s > 0)
			setSigma(s);

	}

	@Override
	public AbstractFilter getNewInstance() {
		return new GaussianBlurFilter();
	}

}
