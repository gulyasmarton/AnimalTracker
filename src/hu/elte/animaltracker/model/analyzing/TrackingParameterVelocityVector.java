package hu.elte.animaltracker.model.analyzing;

import hu.elte.animaltracker.model.CustomisableProcess;
import hu.elte.animaltracker.model.tracking.TrackSequence;
import hu.elte.animaltracker.model.tracking.Vector2D;
import ij.gui.GenericDialog;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * This class carries out the calculation of the magnitude and direction of
 * displacement.
 * 
 * 
 * 
 */
public class TrackingParameterVelocityVector extends
		AbstractTrackingParameter<Vector2D> {

	private static final long serialVersionUID = -4789273914471214714L;

	protected double scaleDistance = 1d;
	protected double scaleTime = 1d;

	/**
	 * Default constructor.
	 * 
	 * @param unit
	 *            unit of speed (e.g m/s, cm/s)
	 */
	public TrackingParameterVelocityVector(String unit) {
		super("Velocity Vector", unit);
	}

	/**
	 * This constructor helps to set scaling.
	 * 
	 * @param distance
	 * @param time
	 */
	public TrackingParameterVelocityVector(TrackingParameterDistance distance,
			TrackingParameterTime time) {
		super("Velocity Vector", distance.getUnit() + "/" + time.getUnit());
		scaleDistance = distance.getScale();
		scaleTime = time.getScale();
	}

	@Override
	public List<DataPoint<Vector2D>> getValues(List<TrackSequence> sequences) {
		List<DataPoint<Vector2D>> dataPoints = new ArrayList<DataPoint<Vector2D>>();
		for (TrackSequence ts : sequences) {
			for (int i = 0; i < ts.size() - 1; i++) {
				Vector2D vector = new Vector2D(ts.get(i), ts.get(i + 1));
				vector.multiply(1 / scaleDistance / scaleTime);
				dataPoints.add(new DataPoint<Vector2D>(ts.get(i + 1).frame,
						vector));
			}
		}
		return dataPoints;
	}

	@Override
	public Vector2D getMean(List<DataPoint<Vector2D>> raws) {
		if (raws.size() == 0)
			return new Vector2D(1, 0);
		double length = 0;
		double direction = 0;
		for (DataPoint<Vector2D> v : raws) {
			length += v.getData().getLength();
			direction += v.getData().getRadian();
		}
		length /= raws.size();
		direction /= raws.size();

		return new Vector2D(length, direction);
	}

	@Override
	public Vector2D getSd(List<DataPoint<Vector2D>> raws) {
		if (raws.size() == 0)
			return new Vector2D(1, 0);
		double sL = 0;
		double sR = 0;
		Vector2D m = getMean(raws);
		for (DataPoint<Vector2D> p : raws) {
			sL = (p.data.getLength() - m.getLength())
					* (p.data.getLength() - m.getLength());
			sR = (p.data.getRadian() - m.getRadian())
					* (p.data.getRadian() - m.getRadian());
		}
		sL = Math.sqrt(sL / raws.size() - 1);
		sR = Math.sqrt(sR / raws.size() - 1);
		return new Vector2D(sL, sR);
	}

	@Override
	public Vector2D getSum(List<DataPoint<Vector2D>> raws) {
		if (raws.size() == 0)
			return new Vector2D(1, 0);
		double x = 0;
		double y = 0;
		for (DataPoint<Vector2D> v : raws) {
			Vector2D org = v.getData().getOrigo();
			x += org.getP2().x;
			y += org.getP2().y;
		}
		return new Vector2D(new Point2D.Float(0, 0), new Point2D.Float(
				(float) x, (float) y));
	}

	@Override
	public Vector2D getMin(List<DataPoint<Vector2D>> raws) {
		if (raws.size() == 0)
			return new Vector2D(1, 0);
		double length = Double.MAX_VALUE;
		double direction = Double.MAX_VALUE;
		for (DataPoint<Vector2D> v : raws) {
			if (v.getData().getLength() < length)
				length = v.getData().getLength();
			if (v.getData().getRadian() < direction)
				direction = v.getData().getRadian();
		}

		return new Vector2D(length, direction);
	}

	@Override
	public Vector2D getMax(List<DataPoint<Vector2D>> raws) {
		if (raws.size() == 0)
			return new Vector2D(1, 0);
		double length = Double.MIN_VALUE;
		double direction = Double.MIN_VALUE;
		for (DataPoint<Vector2D> v : raws) {
			if (v.getData().getLength() > length)
				length = v.getData().getLength();
			if (v.getData().getRadian() > direction)
				direction = v.getData().getRadian();
		}

		return new Vector2D(length, direction);
	}

	public double getScaleDistance() {
		return scaleDistance;
	}

	public void setScaleDistance(double scaleDistance) {
		this.scaleDistance = scaleDistance;
	}

	public double getScaleTime() {
		return scaleTime;
	}

	public void setScaleTime(double scaleTime) {
		this.scaleTime = scaleTime;
	}

	@Override
	public CustomisableProcess getNewInstance() {
		TrackingParameterVelocityVector p = new TrackingParameterVelocityVector(
				getUnit());
		p.setScaleDistance(getScaleDistance());
		p.setScaleTime(getScaleTime());
		return p;
	}

	@Override
	public void showGUI() {
		GenericDialog gd = new GenericDialog("Velocity Vector Settings");
		gd.addStringField("unit: ", getUnit());
		gd.addNumericField("Pixel/unit: ", getScaleDistance(), 3);
		gd.addNumericField("Frame interval: ", getScaleTime(), 3);

		gd.showDialog();
		if (gd.wasCanceled())
			return;

		setUnit(gd.getNextString());
		setScaleDistance(gd.getNextNumber());
		setScaleTime(gd.getNextNumber());
	}

}
