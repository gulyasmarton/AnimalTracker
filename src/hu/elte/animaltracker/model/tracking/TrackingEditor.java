package hu.elte.animaltracker.model.tracking;

import java.util.List;

/**
 * This class is a toolbox for track-editing. You can delete or interpolate
 * track sections.
 * 
 * 
 */
public class TrackingEditor {

	ObjectLocation[] locations;

	public TrackingEditor(ObjectLocation[] locations) {
		super();
		this.locations = locations;
	}

	/**
	 * Finds and interpolates gaps between TrackSequences.
	 * 
	 * @param track
	 */
	public void interpolationGaps(List<TrackSequence> track) {
		for (int i = 0; i < track.size() - 1; i++)
			interPoltracking(track.get(i).getLastPoint(), track.get(i + 1)
					.getFirstPoint());
	}

	/**
	 * Returns the time-connected sequences of ObjectLocation arrays.
	 * 
	 * @return
	 */
	public List<TrackSequence> findTrackSequence() {
		return TrackSequence.getConnectedSequences(locations);
	}

	/**
	 * Performs interpolation between two points.
	 * 
	 * @param p1
	 * @param p2
	 */
	public void interPoltracking(ObjectLocation p1, ObjectLocation p2) {
		int distance = Math.abs(p1.frame - p2.frame);
		if (distance < 3)
			return;
		int start = Math.min(p1.frame, p2.frame);
		int stop = Math.max(p1.frame, p2.frame);
		ObjectLocation first = p1.frame < p2.frame ? p1 : p2;
		ObjectLocation second = p1.frame > p2.frame ? p1 : p2;

		for (int i = start; i <= stop; i++) {
			float ratio = ((float) (i - start)) / ((float) (stop - start));
			locations[i - 1] = new ObjectLocation(first.x * (1 - ratio)
					+ second.x * ratio, first.y * (1 - ratio) + second.y
					* ratio, i);

		}
	}

	/**
	 * Deletes tracks between two points.
	 * 
	 * @param p1
	 * @param p2
	 */
	public void deleteTrack(ObjectLocation p1, ObjectLocation p2) {
		int start = Math.min(p1.frame, p2.frame);
		int stop = Math.max(p1.frame, p2.frame);

		for (int i = start; i <= stop; i++)
			locations[i - 1] = null;

	}

	/**
	 * Sets a new coordinate in a given frame.
	 * 
	 * @param currentFrame
	 *            one-based index of frame.
	 * @param x
	 * @param y
	 */
	public void manualTracking(int currentFrame, int x, int y) {
		locations[currentFrame - 1] = new ObjectLocation(x, y);
	}
}
