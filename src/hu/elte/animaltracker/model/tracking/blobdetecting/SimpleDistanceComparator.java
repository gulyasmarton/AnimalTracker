package hu.elte.animaltracker.model.tracking.blobdetecting;

import ij.gui.GenericDialog;

import java.io.Serializable;
import java.util.List;

import hu.elte.animaltracker.model.CustomisableProcess;
import hu.elte.animaltracker.model.tracking.ObjectLocation;
import hu.elte.animaltracker.model.tracking.blobs.BaseBlob;

/**
 * This class selects the blob closest to the reference blob. You can define a
 * maximum distance. If there are no blobs in this range, the compareBlob
 * function returns null.
 * 
 */
public class SimpleDistanceComparator implements BlobComparator, Serializable {
	private static final long serialVersionUID = -4852566767695708624L;
	protected Double maximumDistance;

	public SimpleDistanceComparator() {
	}

	/**
	 * 
	 * @param maximumDistance
	 *            Sets the maximum search distance.
	 */
	public SimpleDistanceComparator(Double maximumDistance) {
		this.maximumDistance = maximumDistance;
	}

	@Override
	public BaseBlob compareBlob(BaseBlob reference, List<BaseBlob> samples) {
		if (reference == null || samples == null || samples.size() == 0)
			return null;

		ObjectLocation ref = reference.getCoM();
		double distance = Double.MAX_VALUE;
		BaseBlob closest = null;

		for (BaseBlob blob : samples) {
			double d = ref.distance(blob.getCoM());
			if (maximumDistance != null && maximumDistance < d)
				continue;
			if (d < distance) {
				distance = d;
				closest = blob;
			}
		}

		return closest;
	}

	/**
	 * Returns the maximum search distance.
	 * 
	 * @return
	 */
	public Double getMaximumDistance() {
		return maximumDistance;
	}

	/**
	 * Sets the maximum search distance.
	 * 
	 * @param maximumDistance
	 */
	public void setMaximumDistance(Double maximumDistance) {
		this.maximumDistance = maximumDistance;
	}

	@Override
	public CustomisableProcess getNewInstance() {
		return new SimpleDistanceComparator();
	}

	@Override
	public String getName() {
		return "Simple Distance Comparator";
	}

	@Override
	public void showGUI() {
		GenericDialog gd = new GenericDialog("Set maximal distance");
		gd.addNumericField("Distance in pixel", maximumDistance == null ? 0
				: maximumDistance, 1);
		gd.showDialog();
		if (gd.wasCanceled())
			return;
		double max = gd.getNextNumber();
		setMaximumDistance(max == 0 ? null : max);
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public boolean needPixelData() {
		return false;
	}

}
