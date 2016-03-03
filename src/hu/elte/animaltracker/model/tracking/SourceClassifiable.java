package hu.elte.animaltracker.model.tracking;

import ij.ImagePlus;
import ij.process.ImageProcessor;

/**
 * This interface provides information from a filter whether or not it can
 * process the current source image.
 * 
 * 
 */
public interface SourceClassifiable {

	/**
	 * Returns 'true' if the type of image is supported.
	 * 
	 * @param imp
	 * @return
	 */
	public boolean isSupported(ImagePlus imp);

	/**
	 * Returns 'true' if the type of image is supported.
	 * 
	 * @param ip
	 * @return
	 */
	public boolean isSupported(ImageProcessor ip);

	/**
	 * Returns image-type flags which are supported by the filter. See the
	 * SourceClassifier class.
	 * 
	 * @return BYTE_IMAGE, SHORT_IMAGE, FLOAT_IMAGE, COLOR_IMAGE
	 * 
	 * @see hu.elte.animaltracker.model.tracking.SourceClassifier
	 */
	public abstract int getSupportedImageType();
}
