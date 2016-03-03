package hu.elte.animaltracker.model.analyzing;

import java.util.List;

/**
 * This is a container class which stores the result of the
 * AbstractTrackingParameter class.
 * 
 * @see hu.elte.animaltracker.model.analyzing.AbstractTrackingParameter
 * 
 * 
 * 
 */
public class TrackingResult {
	protected List<Object> raws;
	protected Object sum, mean, sd, min, max;

	public TrackingResult(List<Object> raws, Object sum, Object mean,
			Object sd, Object min, Object max) {
		super();
		this.raws = raws;
		this.sum = sum;
		this.mean = mean;
		this.sd = sd;
		this.min = min;
		this.max = max;
	}

	/**
	 * Returns raw data.
	 * 
	 * @return
	 */
	public List<Object> getRaws() {
		return raws;
	}

	/**
	 * Returns the summarized value.
	 * 
	 * @return
	 */
	public Object getSum() {
		return sum;
	}

	/**
	 * Returns the mean value.
	 * 
	 * @return
	 */
	public Object getMean() {
		return mean;
	}

	/**
	 * Returns the standard deviation value.
	 * 
	 * @return
	 */
	public Object getSd() {
		return sd;
	}

	/**
	 * Returns the minimum value.
	 * 
	 * @return
	 */
	public Object getMin() {
		return min;
	}

	/**
	 * Returns the maximum value.
	 * 
	 * @return
	 */
	public Object getMax() {
		return max;
	}

}
