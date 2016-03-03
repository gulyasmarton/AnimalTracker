package hu.elte.animaltracker.model.analyzing;

import hu.elte.animaltracker.model.CustomisableProcess;
import hu.elte.animaltracker.model.tracking.TrackSequence;
import ij.gui.GenericDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * This class carries out the calculation of the distance traveled between two
 * frames.
 * 
 * 
 * 
 */
public class TrackingParameterDistance extends
		AbstractTrackingParameter<Double> {

	private static final long serialVersionUID = 958649726859888649L;
	protected double scale = 1d;

	/**
	 * 
	 * @param unit
	 *            distance unit e.g.: cm
	 */
	public TrackingParameterDistance(String unit) {
		super("Distance", unit);

	}

	/**
	 * 
	 * @param unit
	 *            distance unit e.g.: cm
	 * @param scale
	 *            pixel/unit ratio
	 */
	public TrackingParameterDistance(String unit, double scale) {
		super("Distance", unit);
		setScale(scale);
	}

	/**
	 * Returns the pixel/unit ratio
	 * 
	 * @return
	 */
	public double getScale() {
		return scale;
	}

	/**
	 * Sets the pixel/unit ratio
	 * 
	 * @return
	 */
	public void setScale(double scale) {
		this.scale = scale;
	}

	@Override
	public List<DataPoint<Double>> getValues(List<TrackSequence> sequences) {
		List<DataPoint<Double>> dataPoints = new ArrayList<DataPoint<Double>>();
		for (TrackSequence ts : sequences) {
			for (int i = 0; i < ts.size() - 1; i++) {
				double d = ts.get(i).distance(ts.get(i + 1)) / scale;
				dataPoints.add(new DataPoint<Double>(ts.get(i + 1).frame, d));
			}
		}
		return dataPoints;
	}

	@Override
	public Double getMean(List<DataPoint<Double>> raw) {
		if (raw.size() == 0)
			return 0d;
		double mean = 0;
		mean = getSum(raw) / raw.size();
		return mean;
	}

	@Override
	public Double getSd(List<DataPoint<Double>> raw) {
		if (raw.size() < 2)
			return 0d;
		double s = 0;
		double m = getMean(raw);
		for (DataPoint<Double> p : raw) {
			s += (p.data - m) * (p.data - m);
		}
		s = Math.sqrt(s / (raw.size() - 1));
		return s;
	}

	@Override
	public Double getSum(List<DataPoint<Double>> raw) {
		double d = 0;
		for (DataPoint<Double> p : raw)
			d += p.data;
		return d;
	}

	@Override
	public Double getMin(List<DataPoint<Double>> raw) {
		if (raw.size() == 0)
			return 0d;
		double min = Double.MAX_VALUE;
		for (DataPoint<Double> p : raw)
			if (min > p.data)
				min = p.data;
		return min;
	}

	@Override
	public Double getMax(List<DataPoint<Double>> raw) {
		if (raw.size() == 0)
			return 0d;
		double max = Double.MIN_VALUE;
		for (DataPoint<Double> p : raw)
			if (max < p.data)
				max = p.data;
		return max;
	}

	@Override
	public CustomisableProcess getNewInstance() {
		TrackingParameterDistance d = new TrackingParameterDistance(getUnit(),
				getScale());
		d.setScale(getScale());
		return d;
	}

	@Override
	public void showGUI() {
		GenericDialog gd = new GenericDialog("Distance Settings");
		gd.addStringField("distance unit: ", getUnit());
		gd.addNumericField("Pixel/unit: ", getScale(), 3);

		gd.showDialog();
		if (gd.wasCanceled())
			return;

		setUnit(gd.getNextString());
		setScale(gd.getNextNumber());
	}

}
