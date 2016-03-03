package hu.elte.animaltracker.model.analyzing;

import hu.elte.animaltracker.model.CustomisableProcess;
import hu.elte.animaltracker.model.tracking.TrackSequence;
import ij.gui.GenericDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * This class carries out the counting of frames where the target doesn't move.
 * 
 * 
 * 
 */
public class TrackingParameterImmobilityTime extends TrackingParameterTime {

	private static final long serialVersionUID = -5223500981269197253L;
	protected int minTimeInFrame = 5;
	protected double minDistance = 5;

	/**
	 * 
	 * @param unit
	 *            time unit e.g.: sec
	 */
	public TrackingParameterImmobilityTime(String unit) {
		super(unit);
		name = "Immobility Time";
	}

	/**
	 * Uses TrackingParameterTime for configuration.
	 * 
	 * @param time
	 */
	public TrackingParameterImmobilityTime(TrackingParameterTime time) {
		super(time.getUnit());
		name = "Immobility Time";
		scale = time.getScale();
	}

	/**
	 * Returns the window of the frames where displacement is observed.
	 * 
	 * @return
	 */
	public int getMinTimeInFrame() {
		return minTimeInFrame;
	}

	/**
	 * Sets the number of frame windows where displacement is observed.
	 * 
	 * @param minTimeInFrame
	 */
	public void setMinTimeInFrame(int minTimeInFrame) {
		this.minTimeInFrame = minTimeInFrame;
	}

	/**
	 * Returns the minimum distance which defines movement.
	 * 
	 * @return
	 */
	public double getMinDistance() {
		return minDistance;
	}

	/**
	 * Sets the minimum distance which defines movement.
	 * 
	 * @param minDistance
	 */
	public void setMinDistance(double minDistance) {
		this.minDistance = minDistance;
	}

	@Override
	public List<DataPoint<Double>> getValues(List<TrackSequence> sequences) {
		List<DataPoint<Double>> dataPoints = new ArrayList<DataPoint<Double>>();
		for (TrackSequence ts : sequences) {
			for (int i = 0; i < ts.size() - minTimeInFrame; i++) {
				if (minDistance < ts.get(i)
						.distance(ts.get(i + minTimeInFrame)))
					dataPoints
							.add(new DataPoint<Double>(ts.get(i).frame, scale));
			}
		}
		return dataPoints;
	}

	@Override
	public CustomisableProcess getNewInstance() {
		TrackingParameterImmobilityTime t = new TrackingParameterImmobilityTime(
				getUnit());
		t.setScale(getScale());
		t.setMinDistance(getMinDistance());
		t.setMinTimeInFrame(getMinTimeInFrame());
		return t;
	}

	@Override
	public void showGUI() {
		GenericDialog gd = new GenericDialog("Immobility Time Settings");
		gd.addStringField("time unit: ", getUnit());
		gd.addNumericField("Frame interval: ", getScale(), 3);
		gd.addNumericField("Immobility in frame: ", getMinTimeInFrame(), 0);
		gd.addNumericField("Immobility distance:", getMinDistance(), 3);

		gd.showDialog();
		if (gd.wasCanceled())
			return;

		setUnit(gd.getNextString());
		setScale(gd.getNextNumber());
		setMinTimeInFrame((int) gd.getNextNumber());
		setMinDistance(gd.getNextNumber());
	}

}
