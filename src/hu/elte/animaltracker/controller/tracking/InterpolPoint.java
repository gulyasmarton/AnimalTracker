package hu.elte.animaltracker.controller.tracking;

import hu.elte.animaltracker.model.tracking.ObjectLocation;

/**
 * This is a wrapper of ObjectLocation class for tracking editor.
 * 
 * 
 */
public class InterpolPoint extends ObjectLocation {

	private static final long serialVersionUID = 1539973388641811859L;

	public InterpolPoint(float x, float y, int frame) {
		super(x, y, frame);
	}

	public InterpolPoint(ObjectLocation point, int frame) {
		this(point.x, point.y, frame);
	}

	@Override
	public String toString() {
		return frame + ". frame";
	}
}