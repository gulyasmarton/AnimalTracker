package hu.elte.animaltracker.model.tracking.blobdetecting;

import hu.elte.animaltracker.model.CustomisableProcess;
import hu.elte.animaltracker.model.tracking.blobs.BaseBlob;
import hu.elte.animaltracker.model.tracking.thresholding.BooleanImage;
import ij.process.ImageProcessor;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * This is a simple implementation of the BlobDetector interface.
 * 
 * 
 */
public class BaseBlobDetector implements BlobDetector, Serializable {
	private static final long serialVersionUID = 4538976800917698765L;

	@Override
	public List<BaseBlob> getBlobs(BooleanImage image, ImageProcessor raw) {
		FloodFill floodFill = new FloodFill(image);

		List<BaseBlob> blobs = floodFill.getBlobs();

		if (raw != null)
			blobs = BaseBlob.setPixelInformation(raw, blobs);

		return blobs;
	}

	@Override
	public CustomisableProcess getNewInstance() {
		return new BaseBlobDetector();
	}

	@Override
	public String getName() {
		return "Base Blob Detector";
	}

	/**
	 * Unimplemented function. There is no adjustable parameter.
	 */
	@Override
	public void showGUI() {
	}

	@Override
	public String toString() {
		return getName();
	}

}
