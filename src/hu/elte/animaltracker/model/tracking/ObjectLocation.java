package hu.elte.animaltracker.model.tracking;

import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * This class contains the 2D position and time (frame) position of a blob.
 * 
 * 
 */
public class ObjectLocation extends Point2D.Float implements Serializable,
		Comparable<ObjectLocation> {

	private static final long serialVersionUID = -1961103202323367938L;

	public int frame;

	public ObjectLocation(float x, float y, int frame) {
		super();
		this.x = x;
		this.y = y;
		this.frame = frame;
	}

	public ObjectLocation(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Converts a whitespace-split floating-point x-y coordinate string to
	 * ObjectLocation.
	 * 
	 * @param s
	 * @return
	 */
	public static ObjectLocation read(String s) {
		String[] a = s.split(" ");
		if (a.length != 2)
			return null;
		try {
			float x = java.lang.Float.parseFloat(a[0]);
			float y = java.lang.Float.parseFloat(a[1]);
			return new ObjectLocation(x, y);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public int compareTo(ObjectLocation o) {
		if (frame > o.frame)
			return 1;
		if (frame < o.frame)
			return -1;
		return 0;
	}

}
