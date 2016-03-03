package hu.elte.animaltracker.model.tracking;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.io.Serializable;

/**
 * This class is an implementation of the 2D Euclidean vector.
 * 
 */
public class Vector2D implements Serializable, Cloneable {

	private static final long serialVersionUID = -8279176595720850402L;
	protected double length;
	protected double direction;
	protected Point2D.Float from;
	protected Point2D.Float to;

	/**
	 * Creates a vector with the given length and direction.
	 * 
	 * @param length
	 *            magnitude of vector
	 * @param direction
	 *            direction of vector in radians.
	 * 
	 */
	public Vector2D(double length, double direction) {
		super();
		this.length = length;
		this.direction = radianNorm(direction);
		this.from = new Point2D.Float(0, 0);
		setDirection(this.direction);
	}

	/**
	 * Creates a vector using two points.
	 */
	public Vector2D(Point2D.Float from, Point2D.Float to) {
		this.from = from;
		this.to = to;
		length = from.distance(to);
		setDirection();
	}

	/**
	 * Limits the radian value between 0 and 2 PI.
	 * 
	 * @param radian
	 * @return
	 */
	protected double radianNorm(double radian) {
		double r = radian / (Math.PI * 2);
		if (r < 1)
			return radian;
		r = Math.floor(r);
		return radian - r * (Math.PI * 2);
	}

	/**
	 * Returns the magnitude of the vector.
	 */
	public double getLength() {
		return length;
	}

	/**
	 * Returns the direction of the vector in radians.
	 */
	public double getRadian() {
		return direction;
	}

	/**
	 * Returns the direction of the vector in degrees.
	 */
	public double getDegree() {
		return Math.toDegrees(direction);
	}

	/**
	 * Sets starting point.
	 * 
	 */
	public void setP1(float x, float y) {
		setP1(new Point2D.Float(x, y));
	}

	/**
	 * Sets starting point.
	 * 
	 */
	public void setP1(Point2D.Float p1) {
		this.from = p1;
		length = from.distance(to);
		setDirection();
	}

	/**
	 * Sets end point.
	 * 
	 */
	public void setP2(float x, float y) {
		setP2(new Point2D.Float(x, y));
	}

	/**
	 * Sets end point.
	 * 
	 */
	public void setP2(Point2D.Float p2) {
		this.to = p2;
		length = from.distance(to);
		setDirection();
	}

	/**
	 * Returns starting point.
	 */
	public Point2D.Float getP1() {
		return from;
	}

	/**
	 * Returns end point.
	 */
	public Point2D.Float getP2() {
		return to;
	}

	/**
	 * Shifts a copy of the vector to the origo (0,0).
	 */
	public Vector2D getOrigo() {
		Point2D.Float p2 = new Point2D.Float(to.x - from.x, to.y - from.y);
		return new Vector2D(new Point2D.Float(0, 0), p2);
	}

	/**
	 * Shifts this vector by the given point.
	 * 
	 * @param p
	 */
	public void shiftWith(Point2D.Float p) {
		to.x += p.x;
		to.y += p.y;
		from.x += p.x;
		from.y += p.y;
	}

	/**
	 * Calculates the direction from p1 and p2.
	 */
	protected void setDirection() {
		float x = to.x - from.x;
		float y = to.y - from.y;
		direction = Math.atan2(x, y);
		if (direction < 0)
			direction = Math.abs(direction) + Math.PI / 2;
		else if (direction < Math.PI / 2) {
			direction = Math.abs(direction - Math.PI / 2);
		} else
			direction = 2.5 * Math.PI - direction;
	}

	/**
	 * Sets the new direction in radians.
	 */
	public void setDirection(double rad) {
		this.direction = radianNorm(rad);
		double y = Math.sin(direction) * length;
		double x = Math.cos(direction) * length;
		to = new Point2D.Float((float) x, (float) y);
		// System.out.println((to));
		// System.out.println(new Vector2D(from, to));
	}

	@Override
	public String toString() {
		return "length: " + length + "; deg: " + getDegree();
	}

	/**
	 * Multiplies the vector.
	 */
	public void multiply(double scale) {
		Vector2D org = getOrigo();
		Point2D.Float p2 = org.getP2();
		p2.x *= scale;
		p2.y *= scale;
		org.setP2(p2);
		org.shiftWith(getP1());
		setP1(org.getP1());
		setP2(org.getP2());
	}

	@Override
	public Vector2D clone() {
		Vector2D v;
		try {
			v = (Vector2D) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
		v.from = (Float) from.clone();
		v.to = (Float) to.clone();
		return v;
	}
}
