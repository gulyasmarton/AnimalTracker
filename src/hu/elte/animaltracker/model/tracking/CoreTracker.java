package hu.elte.animaltracker.model.tracking;

import hu.elte.animaltracker.model.tracking.blobdetecting.BlobComparator;
import hu.elte.animaltracker.model.tracking.blobdetecting.BlobDetector;
import hu.elte.animaltracker.model.tracking.blobs.*;
import hu.elte.animaltracker.model.tracking.filtering.AbstractFilter;
import hu.elte.animaltracker.model.tracking.postprocessing.PostProcessor;
import hu.elte.animaltracker.model.tracking.thresholding.BooleanImage;
import hu.elte.animaltracker.model.tracking.thresholding.AbstractThresholder;
import ij.process.ImageProcessor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the skeleton of single-target 2D-tracking. You can change the
 * algorithms of the five basic tracking steps (filtering, binarization,
 * postprocessing, blob detection, blob comparison).
 * 
 * 
 */
public class CoreTracker implements Serializable {
	private static final long serialVersionUID = 4146312438362943212L;
	protected List<AbstractFilter> filters;
	protected List<PostProcessor> postProcessors;
	protected AbstractThresholder thresholder;
	protected BlobDetector blobDetector;
	protected BlobComparator blobComparator;
	protected BaseBlob referenceBlob;

	public CoreTracker() {
		filters = new ArrayList<AbstractFilter>();
		postProcessors = new ArrayList<PostProcessor>();
	}

	/**
	 * Returns the result of filtering.
	 * 
	 * @param ip
	 *            original image
	 * @return filtered image.
	 */
	public ImageProcessor getFilteredImage(ImageProcessor ip) {
		ip = ip.duplicate();
		for (AbstractFilter processor : filters) {
			ip = processor.processImage(ip);
		}
		return ip;
	}

	/**
	 * Returns the result of binarization.
	 * 
	 * @param ip
	 *            original image
	 * @return binary image.
	 */
	public BooleanImage getBinaryImage(ImageProcessor ip) {
		if (thresholder == null)
			return new BooleanImage(ip.getWidth(), ip.getHeight());
		ip = getFilteredImage(ip);
		return thresholder.getBinaryImage(ip);
	}

	/**
	 * Returns the result of postprocessing.
	 * 
	 * @param ip
	 *            original image
	 * @return postprocessed image.
	 */
	public BooleanImage getPostProcessedImage(ImageProcessor ip) {
		BooleanImage img = getBinaryImage(ip);
		for (PostProcessor processor : postProcessors) {
			img = processor.processImage(img);
		}
		return img;
	}

	/**
	 * Returns the result of blob detection.
	 * 
	 * @param ip
	 *            original image
	 * @return List of blobs.
	 */
	public List<BaseBlob> getBlobs(ImageProcessor ip) {
		return getBlobs(ip, blobComparator.needPixelData());
	}

	/**
	 * Returns the result of blob detection.
	 * 
	 * @param ip
	 *            original image
	 * @param needPixelData
	 *            true: get real pixels of blobs
	 * @return List of blobs.
	 */
	public List<BaseBlob> getBlobs(ImageProcessor ip, boolean needPixelData) {
		BooleanImage image = getPostProcessedImage(ip);
		return blobDetector.getBlobs(image, needPixelData ? ip : null);
	}

	/**
	 * Compares the last identified blob to all possible blobs and returns the
	 * best matching blob.
	 * 
	 * @param ip
	 *            original image
	 * @return It may be null if there is no match.
	 */
	public BaseBlob getMatchedBlob(ImageProcessor ip) {

		List<BaseBlob> blobs = getBlobs(ip);
		if (blobs.size() == 0)
			return null;

		if (referenceBlob == null)
			return null;

		BaseBlob blob = blobComparator.compareBlob(referenceBlob, blobs);
		if (blob != null) {
			referenceBlob = blob;
		}
		return blob;
	}

	/**
	 * Returns the location of the found blob.
	 * 
	 * @param ip
	 *            original image
	 * @return It may be null if there is no found blob.
	 */
	public ObjectLocation getBlobPosition(ImageProcessor ip) {
		BaseBlob blob = getMatchedBlob(ip);
		if (blob == null)
			return null;
		return blob.getCoM();
	}

	/**
	 * Returns the list of filters.
	 * 
	 * @return
	 */
	public List<AbstractFilter> getFilters() {
		return filters;
	}

	/**
	 * Sets the list of filters.
	 * 
	 * @param filters
	 */
	public void setFilters(List<AbstractFilter> filters) {
		this.filters = filters;
	}

	/**
	 * Returns the current Thresholder.
	 * 
	 * @return
	 */
	public AbstractThresholder getThresholder() {
		return thresholder;
	}

	/**
	 * Sets the Thresholder.
	 * 
	 * @param thresholder
	 */
	public void setThresholder(AbstractThresholder thresholder) {
		this.thresholder = thresholder;
	}

	/**
	 * Returns the current BlobDetector.
	 * 
	 * @return
	 */
	public BlobDetector getBlobDetector() {
		return blobDetector;
	}

	/**
	 * Sets the BlobDetector.
	 * 
	 * @param blobDetector
	 */
	public void setBlobDetector(BlobDetector blobDetector) {
		this.blobDetector = blobDetector;
	}

	/**
	 * Returns the current BlobComparator.
	 * 
	 * @return
	 */
	public BlobComparator getBlobComparator() {
		return blobComparator;
	}

	/**
	 * Sets the BlobComparator.
	 * 
	 * @param blobComparator
	 */
	public void setBlobComparator(BlobComparator blobComparator) {
		this.blobComparator = blobComparator;
	}

	/**
	 * Returns the last identified blob.
	 * 
	 * @return
	 * 
	 * @see hu.elte.animaltracker.model.tracking.blobdetecting.BlobComparator
	 */
	public BaseBlob getReferenceBlob() {
		return referenceBlob;
	}

	/**
	 * Sets the reference blob for blob comparison.
	 * 
	 * @param referenceBlob
	 * 
	 * @see hu.elte.animaltracker.model.tracking.blobdetecting.BlobComparator
	 */
	public void setReferenceBlob(BaseBlob referenceBlob) {
		this.referenceBlob = referenceBlob;
	}

	/**
	 * Adds a new filter to the list of filters.
	 * 
	 * @param filter
	 */
	public void addFilter(AbstractFilter filter) {
		filters.add(filter);
	}

	/**
	 * Removes an existing filter from the list of filters.
	 * 
	 * @param filter
	 */
	public void removeFilter(AbstractFilter filter) {
		filters.remove(filter);
	}

	/**
	 * Adds a new PostProcessor to the list of postprocessors.
	 * 
	 * @param postProcessor
	 */
	public void addPostProcessor(PostProcessor postProcessor) {
		postProcessors.add(postProcessor);
	}

	/**
	 * Removes an existing PostProcessor from the list of postprocessors.
	 * 
	 * @param postProcessor
	 */
	public void removePostProcessor(PostProcessor postProcessor) {
		postProcessors.remove(postProcessor);
	}

	/**
	 * Returns the list of postprocessors.
	 * 
	 * @return
	 */
	public List<PostProcessor> getPostProcessors() {
		return postProcessors;
	}

	/**
	 * Sets the list of postprocessors.
	 * 
	 * @param postProcessors
	 */
	public void setPostProcessors(List<PostProcessor> postProcessors) {
		this.postProcessors = postProcessors;
	}
}
