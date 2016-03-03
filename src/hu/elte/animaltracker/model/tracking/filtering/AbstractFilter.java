package hu.elte.animaltracker.model.tracking.filtering;

import java.io.Serializable;

import hu.elte.animaltracker.model.CustomisableProcess;
import hu.elte.animaltracker.model.tracking.SourceClassifiable;
import hu.elte.animaltracker.model.tracking.SourceClassifier;
import ij.ImagePlus;
import ij.process.ImageProcessor;

/**
 * 
 * The task of the AbstractFilter is to prepare the frames for the binarization.
 * The derived classes of the AbstractFilter are noise reduction or background
 * subtractor algorithms. The most important function of the AbstractFilter
 * class is processImage(ImageProcessor ip) which performs real filtering.
 * Because the type of the input parameter and the return value are equal you
 * can easily concatenate more filters.
 * 
 * @see hu.elte.animaltracker.model.tracking.filtering.BackgroundSubtractor
 * @see hu.elte.animaltracker.model.tracking.filtering.ColorBackgroundSubtractor
 * @see hu.elte.animaltracker.model.tracking.filtering.GaussianBlurFilter
 * 
 * 
 */
public abstract class AbstractFilter implements CustomisableProcess,
		SourceClassifiable, Serializable {
	private static final long serialVersionUID = 3839605002115191799L;

	public boolean isSupported(ImagePlus imp) {
		return isSupported(imp.getProcessor());
	}

	public boolean isSupported(ImageProcessor ip) {
		return SourceClassifier.isSupported(ip, getSupportedImageType());
	}

	/**
	 * This function carries out the filtering. If the type of the source image
	 * is not supported, the original image is returned.
	 * 
	 * @param ip
	 *            original image.
	 * @return filtered image.
	 */
	public abstract ImageProcessor processImage(ImageProcessor ip);

	/**
	 * This function carries out the filtering. If the type of the source image
	 * is not supported, the original image is returned.
	 * 
	 * @param imp
	 *            original image.
	 * @return filtered image.
	 */
	public ImageProcessor processImage(ImagePlus imp) {
		return processImage(imp.getProcessor());
	}

	@Override
	public String toString() {
		return getName();
	}

}
