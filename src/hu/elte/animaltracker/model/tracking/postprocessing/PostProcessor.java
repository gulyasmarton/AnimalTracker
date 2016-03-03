package hu.elte.animaltracker.model.tracking.postprocessing;

import hu.elte.animaltracker.model.CustomisableProcess;
import hu.elte.animaltracker.model.tracking.thresholding.BooleanImage;

/**
 * The function of the classes implementing this interface is the reparation of
 * the mistakes of binarization. Because the type of the input parameter and the
 * return value are equal you can easily concatenate more postprocessors.
 * 
 * 
 */
public interface PostProcessor extends CustomisableProcess {
	/**
	 * This function carries out postprocessing.
	 * 
	 * @param original
	 *            binary image
	 * @return postprocessed binary image
	 */
	public abstract BooleanImage processImage(BooleanImage imp);
}
