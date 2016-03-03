package hu.elte.animaltracker.model.analyzing;

import hu.elte.animaltracker.model.CustomisableProcess;
import hu.elte.animaltracker.model.tracking.ObjectLocation;
import hu.elte.animaltracker.model.tracking.TrackSequence;
import ij.gui.GenericDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * This class carries out the calculation of the elapsed time.
 * 
 * 
 * 
 */
public class TrackingParameterTime extends AbstractTrackingParameter<Double> {

	private static final long serialVersionUID = -1359709982235385822L;
	protected double scale = 1d;

	/**
	 * Default constructor.
	 * 
	 * @param unit
	 *            Time unit (e.g. sec)
	 */
	public TrackingParameterTime(String unit) {
		super("Time", unit);
	}

	@Override
	public List<DataPoint<Double>> getValues(List<TrackSequence> sequences) {
		List<DataPoint<Double>> dataPoints = new ArrayList<DataPoint<Double>>();
		for (TrackSequence ts : sequences) {
			for (ObjectLocation ol : ts) {
				dataPoints.add(new DataPoint<Double>(ol.frame, scale));
			}
		}
		return dataPoints;
	}

	/**
	 * Returns the frame rate (sec/frame)
	 * 
	 * @return
	 */
	public double getScale() {
		return scale;
	}

	/**
	 * Sets the frame rate (sec/frame)
	 * 
	 * @param scale
	 */
	public void setScale(double scale) {
		this.scale = scale;
	}

	@Override
	public Double getMean(List<DataPoint<Double>> raw) {
		return scale;
	}

	@Override
	public Double getSd(List<DataPoint<Double>> raw) {
		return 0d;
	}

	@Override
	public Double getSum(List<DataPoint<Double>> raw) {
		return raw.size() * scale;
	}

	@Override
	public Double getMin(List<DataPoint<Double>> raw) {
		return scale;
	}

	@Override
	public Double getMax(List<DataPoint<Double>> raw) {
		return scale;
	}

	@Override
	public CustomisableProcess getNewInstance() {
		TrackingParameterTime t = new TrackingParameterTime(getUnit());
		t.setScale(getScale());
		return t;
	}

	@Override
	public void showGUI() {
		GenericDialog gd = new GenericDialog("Time Settings");
		gd.addStringField("time unit:", getUnit());
		gd.addNumericField("Frame interval:", getScale(), 3);

		gd.showDialog();
		if (gd.wasCanceled())
			return;

		setUnit(gd.getNextString());
		setScale(gd.getNextNumber());
	}

}
