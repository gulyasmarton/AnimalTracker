package hu.elte.animaltracker.model.tracking.blobdetecting;

import hu.elte.animaltracker.model.CustomisableProcess;
import hu.elte.animaltracker.model.tracking.blobs.BaseBlob;
import hu.elte.animaltracker.model.tracking.thresholding.BooleanImage;
import ij.process.ImageProcessor;

import java.util.List;

/**
 * This interface finds the connected areas on the binary image and collects
 * these pixels into a BaseBlob object (or some derived classes).
 * 
 * 
 */
public interface BlobDetector extends CustomisableProcess {
	/**
	 * Finds the connected areas on the binary image and makes a list of the
	 * blobs.
	 * 
	 * @param image
	 *            binary image
	 * @param raw
	 *            If the raw parameter is not null, the original pixel
	 *            information is stored in the blob file.
	 * @return found blobs
	 * 
	 * @see package hu.elte.animaltracker.model.tracking.blobs.BaseBlob;
	 */
	List<BaseBlob> getBlobs(BooleanImage image, ImageProcessor raw);
}
