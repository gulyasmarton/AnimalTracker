package hu.elte.animaltracker.model.tracking.blobs;

import hu.elte.animaltracker.model.tracking.ObjectLocation;
import hu.elte.animaltracker.model.tracking.Vector2D;
import ij.process.ByteProcessor;
import ij.process.ColorProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import ij.process.ShortProcessor;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * This is a basic blob class which contains several general functions and
 * manages the base of serialization. It has four derived classes (ByteBlob,
 * ShortBlob, FloatBlob, ColorBlob) in accordance with pixel types.
 * 
 * @see hu.elte.animaltracker.model.tracking.blobs.ByteBlob
 * @see hu.elte.animaltracker.model.tracking.blobs.ShortBlob
 * @see hu.elte.animaltracker.model.tracking.blobs.FloatBlob
 * @see hu.elte.animaltracker.model.tracking.blobs.ColorBlob
 * 
 */
public class BaseBlob implements Serializable {

	private static final long serialVersionUID = 4112707439187511140L;

	protected boolean[] mask;
	protected int size;

	protected ObjectLocation CoM;
	protected Rectangle bound;

	protected Vector2D direction;

	protected Double mean;
	protected Double sd;

	public BaseBlob(HashSet<Point> points) {
		bound = getBound(points);
		mask = getMask(points);
		size = points.size();
		CoM = getCoM(points);
	}

	public BaseBlob(BaseBlob blob) {
		this.bound = blob.getBound();
		this.mask = blob.getMask();
		this.size = blob.getSize();
		this.CoM = blob.getCoM();
	}

	/**
	 * Converts the set of points to a boolean mask for space reduction.
	 * 
	 * @param points
	 * @return
	 */
	protected boolean[] getMask(HashSet<Point> points) {
		Rectangle rec = bound;
		boolean[] mask = new boolean[rec.width * rec.height];
		for (Point p : points) {
			mask[p.x - rec.x + (p.y - rec.y) * rec.width] = true;
		}
		return mask;
	}

	/**
	 * Returns the boundary rectangle of the blob.
	 * 
	 * @return
	 */
	public Rectangle getBound() {
		return bound;
	}

	protected Rectangle getBound(HashSet<Point> points) {
		int maxW = -1;
		int maxH = -1;
		int x = Integer.MAX_VALUE;
		int y = Integer.MAX_VALUE;
		for (Point point : points) {
			if (point.x > maxW)
				maxW = point.x;
			if (point.y > maxH)
				maxH = point.y;
			if (point.x < x)
				x = point.x;
			if (point.y < y)
				y = point.y;
		}
		maxW -= x;
		maxH -= y;
		maxW++;
		maxH++;
		return new Rectangle(x, y, maxW, maxH);

	}

	/**
	 * Returns the center of mass of the blob.
	 * 
	 * @return
	 */
	public ObjectLocation getCoM() {
		return CoM;
	}

	protected ObjectLocation getCoM(HashSet<Point> points) {
		float x = 0;
		float y = 0;
		for (Point p : points) {
			x += p.x;
			y += p.y;
		}
		return new ObjectLocation(x / points.size(), y / points.size());
	}

	/**
	 * Returns the blob's points. Warning! For space reduction, this HashSet is
	 * recreated at all time!
	 * 
	 * @return
	 */
	public HashSet<Point> getPoints() {
		HashSet<Point> points = new HashSet<Point>();

		for (int i = 0; i < mask.length; i++)
			if (mask[i]) {
				int x = i % bound.width + bound.x;
				int y = i / bound.width + bound.x;
				points.add(new Point(x, y));
			}

		return points;
	}

	/**
	 * Returns the number of the blob's points.
	 * 
	 * @return
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Returns 'true' if there is a common area.
	 * 
	 * @param b
	 * @return
	 */
	public boolean isOverlap(BaseBlob b) {
		if (!bound.intersects(b.getBound()))
			return false;

		for (int y = 0; y < bound.height; y++)
			for (int x = 0; x < bound.width; x++)
				if (b.getBound().contains(x + bound.x, y + bound.y))
					return true;

		return false;
	}

	/**
	 * Returns 'true' if the blob contains this point.
	 * 
	 * @param p
	 * @return
	 */
	public boolean isContain(Point p) {
		return isContain(p.x, p.y);
	}

	/**
	 * Returns 'true' if the blob contains this point.
	 * 
	 * @param p
	 * @return
	 */
	public boolean isContain(int x, int y) {
		if (!bound.contains(x, y))
			return false;
		x -= bound.x;
		y -= bound.y;

		return mask[x + y * bound.width];
	}

	public int OverlapArea(BaseBlob b) {
		int size = 0;

		if (!bound.intersects(b.getBound()))
			return 0;

		for (int y = 0; y < bound.height; y++)
			for (int x = 0; x < bound.width; x++)
				if (b.getBound().contains(x + bound.x, y + bound.y))
					size++;

		return size;
	}

	/**
	 * Returns the mask of the blob.
	 * 
	 * @return
	 */
	public boolean[] getMask() {
		return mask;
	}

	/**
	 * Returns the magnitude and direction of displacement.
	 * 
	 * @return
	 */
	public Vector2D getDirection() {
		return direction;
	}

	/**
	 * Sets a displacement vector.
	 * 
	 * @param direction
	 */
	public void setDirection(Vector2D direction) {
		this.direction = direction;
	}

	/**
	 * Sets a displacement vector using another blob.
	 * 
	 * @param direction
	 */
	public void setDirection(BaseBlob blob) {
		this.direction = new Vector2D(this.getCoM(), blob.getCoM());
	}

	/**
	 * Sets the original pixel values of the blobs.
	 * 
	 * @param raw
	 *            original ImageProcessor object (contains pixels)
	 * @param blobs
	 * @return Derived class of BaseBlob
	 */
	public static List<BaseBlob> setPixelInformation(ImageProcessor raw,
			List<BaseBlob> blobs) {
		List<BaseBlob> list = new ArrayList<BaseBlob>(blobs.size());
		for (BaseBlob abstractBlob : blobs) {
			list.add(setPixelInformation(raw, abstractBlob));
		}
		return list;

	}

	/**
	 * Sets the original pixel values of the blob. Returns the derived class of
	 * BaseBlob in accordance with the pixel type.
	 * 
	 * @param raw
	 *            original ImageProcessor object (contains pixels)
	 * @param blob
	 * @return Derived class of BaseBlob
	 */
	public static BaseBlob setPixelInformation(ImageProcessor raw, BaseBlob blob) {
		if (raw instanceof ByteProcessor) {
			return new ByteBlob(blob, raw);
		}
		if (raw instanceof ShortProcessor) {
			return new ShortBlob(blob, raw);
		}
		if (raw instanceof FloatProcessor) {
			return new FloatBlob(blob, raw);
		}
		if (raw instanceof ColorProcessor) {
			return new ColorBlob(blob, raw);
		}
		return null;
	}
}
