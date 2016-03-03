package hu.elte.animaltracker.examples.watermaze;

import ij.gui.GenericDialog;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import hu.elte.animaltracker.model.CustomisableProcess;
import hu.elte.animaltracker.model.analyzing.DataPoint;
import hu.elte.animaltracker.model.analyzing.TrackingParameterDistance;
import hu.elte.animaltracker.model.analyzing.TrackingParameterVelocityVector;
import hu.elte.animaltracker.model.tracking.TrackSequence;
import hu.elte.animaltracker.model.tracking.Vector2D;

/**
 * This class calculates the direction and distance of the hidden platform
 * compared to the observed object's course of movement.
 * 
 */
public class TrackingParameterAnglePreference extends
		TrackingParameterVelocityVector {

	private static final long serialVersionUID = 2908313010870482865L;

	Point2D.Float target = new Point2D.Float(0, 0);

	/**
	 * this constructor extracts the pixel/unit ratio and unit name data from
	 * the distance parameter, thereby easing the preconfiguration.
	 * 
	 * @param target
	 *            position of the hidden platform
	 * @param distance
	 *            configured TrackingParameterDistance object
	 */
	public TrackingParameterAnglePreference(Point2D.Float target,
			TrackingParameterDistance distance) {
		super(distance.getUnit() + " - degree");
		scaleDistance = distance.getScale();
		name = "Angle Preference";
		this.target = target;
	}

	/**
	 * Default constructor.
	 * 
	 * @param target
	 *            position of the hidden platform
	 * @param unit
	 *            name of the unit of measurement of distance (e.g. cm)
	 */
	public TrackingParameterAnglePreference(Point2D.Float target, String unit) {
		super(unit + " - degree");
		name = "Angle Preference";
		this.target = target;
	}

	// The function performing the calculation.
	@Override
	public List<DataPoint<Vector2D>> getValues(List<TrackSequence> sequences) {

		// Calculation of the speed vectors.
		List<DataPoint<Vector2D>> velocities = super.getValues(sequences);

		List<DataPoint<Vector2D>> dataPoints = new ArrayList<DataPoint<Vector2D>>();
		int idx = 0;
		for (TrackSequence ts : sequences) {
			for (int i = 0; i < ts.size() - 1; i++) {

				// Creation of a vector pointing from the observed object to
				// the hidden platform.
				Vector2D vector = new Vector2D(ts.get(i), target);

				// Subtraction of the angle of the observed object's course of
				// movement from the angle of the vector pointing to the hidden
				// platform.
				double rad = vector.getRadian()
						- velocities.get(idx++).getData().getRadian();

				// Creation of a new vector starting from the origin (with the
				// knowledge of the calculated angle and distance).
				Vector2D data = new Vector2D(ts.get(i).distance(target), rad);

				// Shifting of the vector's position to the current position of
				// the observed object.
				data.shiftWith(ts.get(i));

				// Addition of the vector to the results table.
				dataPoints.add(new DataPoint<Vector2D>(ts.get(i).frame, data));
			}
		}
		return dataPoints;
	}

	@Override
	public CustomisableProcess getNewInstance() {
		TrackingParameterAnglePreference p = new TrackingParameterAnglePreference(
				getTarget(), getUnit());
		p.setScaleDistance(getScaleDistance());
		p.setScaleTime(getScaleTime());
		return p;
	}

	@Override
	public void showGUI() {
		GenericDialog gd = new GenericDialog("Velocity Vector Settings");
		gd.addStringField("unit: ", getUnit());
		gd.addNumericField("Pixel/unit: ", getScaleDistance(), 3);

		gd.showDialog();
		if (gd.wasCanceled())
			return;

		setUnit(gd.getNextString());
		setScaleDistance(gd.getNextNumber());
	}

	/**
	 * Returns the location of the hidden platform
	 * 
	 * @return
	 */
	public Point2D.Float getTarget() {
		return target;
	}

	/**
	 * Sets the location of the hidden platform
	 * 
	 * @param target
	 */
	public void setTarget(Point2D.Float target) {
		this.target = target;
	}

}
