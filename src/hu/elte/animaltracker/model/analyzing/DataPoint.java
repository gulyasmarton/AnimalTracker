package hu.elte.animaltracker.model.analyzing;

import java.io.Serializable;

/**
 * This is a universal data wrapper class which also stores frame position.
 * 
 * 
 * 
 * @param <E>
 *            type of data
 */
public class DataPoint<E> implements Serializable {
	private static final long serialVersionUID = -3692912082698393091L;
	protected int frame;
	protected E data;

	/**
	 * Default constructor.
	 * 
	 * @param frame
	 *            The frame position from which the data comes from.
	 * @param data
	 *            Data
	 */
	public DataPoint(int frame, E data) {
		super();
		this.frame = frame;
		this.data = data;
	}

	/**
	 * Returns the frame position.
	 * 
	 * @return
	 */
	public int getFrame() {
		return frame;
	}

	/**
	 * Sets the frame position.
	 * 
	 * @param frame
	 */
	public void setFrame(int frame) {
		this.frame = frame;
	}

	/**
	 * Returns stored data.
	 * 
	 * @return
	 */
	public E getData() {
		return data;
	}

	/**
	 * Sets stored data.
	 * 
	 * @param data
	 */
	public void setData(E data) {
		this.data = data;
	}

	/**
	 * Returns the value of the data.toString() function.
	 */
	@Override
	public String toString() {
		return data.toString();
	}
}
