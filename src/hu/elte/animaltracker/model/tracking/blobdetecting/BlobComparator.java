package hu.elte.animaltracker.model.tracking.blobdetecting;

import hu.elte.animaltracker.model.CustomisableProcess;
import hu.elte.animaltracker.model.tracking.blobs.BaseBlob;

import java.util.List;

/**
 * The BlobDetector selects the most similar blob from the list of blobs, based
 * on a reference blob.
 * 
 * 
 */
public interface BlobComparator extends CustomisableProcess {
	/**
	 * Returns the most similar blob.
	 * 
	 * @param reference
	 * @param samples
	 * @return
	 */
	BaseBlob compareBlob(BaseBlob reference, List<BaseBlob> samples);

	/**
	 * Returns 'true' if the identification demands real pixel values. (derived
	 * from the BaseBlob class)
	 * 
	 * @see hu.elte.animaltracker.model.tracking.blobs.BaseBlob
	 * 
	 * @return
	 */
	boolean needPixelData();
}
