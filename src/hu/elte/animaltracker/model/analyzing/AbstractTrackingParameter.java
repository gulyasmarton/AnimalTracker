package hu.elte.animaltracker.model.analyzing;

import hu.elte.animaltracker.model.CustomisableProcess;
import hu.elte.animaltracker.model.tracking.TrackSequence;

import java.io.Serializable;
import java.util.List;

/**
 * This is a general definition of a tracking-point-sequence analysis. The
 * derived classes of this class implement the measurement method of a concrete
 * parameter. The getValues function performs the analysis of data and returns a
 * DataPoint list. In addition to the getValues function, the derived classes
 * have to implement five base statistical functions as well.
 * 
 * 
 * @param <E>
 *            The derived classes should define the type according to the
 *            analysis.
 */
public abstract class AbstractTrackingParameter<E> implements
		CustomisableProcess, Serializable {

	private static final long serialVersionUID = 927929128888043020L;
	protected String name;
	protected String unit;

	/**
	 * Default constructor
	 * 
	 * @param name
	 *            Name of the parameter
	 * @param unit
	 *            Name of the parameter's unit
	 */
	public AbstractTrackingParameter(String name, String unit) {
		super();
		this.name = name;
		this.unit = unit;
	}

	/**
	 * This function performs the analysis of data and returns a DataPoint list.
	 * 
	 * @param sequences
	 * @return
	 */
	public abstract List<DataPoint<E>> getValues(List<TrackSequence> sequences);

	/**
	 * Returns the name of the unit.
	 * 
	 * @return
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * Sets the name of the unit.
	 * 
	 * @param unit
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * Returns the name of the parameter.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Calculates the mean value of the input DataPoint list.
	 * 
	 * @param raws
	 * @return
	 */
	public abstract E getMean(List<DataPoint<E>> raws);

	/**
	 * Calculates the standard deviation value of the input DataPoint list.
	 * 
	 * @param raws
	 * @return
	 */
	public abstract E getSd(List<DataPoint<E>> raws);

	/**
	 * Calculates the sum value of the input DataPoint list.
	 * 
	 * @param raws
	 * @return
	 */
	public abstract E getSum(List<DataPoint<E>> raws);

	/**
	 * Calculates the minimal value of the input DataPoint list.
	 * 
	 * @param raws
	 * @return
	 */
	public abstract E getMin(List<DataPoint<E>> raws);

	/**
	 * Calculates the maximal value of the input DataPoint list.
	 * 
	 * @param raws
	 * @return
	 */
	public abstract E getMax(List<DataPoint<E>> raws);

}
