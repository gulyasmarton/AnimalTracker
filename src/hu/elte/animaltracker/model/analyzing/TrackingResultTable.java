package hu.elte.animaltracker.model.analyzing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import hu.elte.animaltracker.model.zones.ZoneUnit;

/**
 * This is a data organizer class. Aids the query of data by area (ZoneUnit),
 * parameter (AbstractTrackingParameter) and data type (raw, mean, sum, sd, min,
 * max).
 * 
 * 
 * @see hu.elte.animaltracker.model.zones.ZoneUnit
 * @see hu.elte.animaltracker.model.analyzing.AbstractTrackingParameter
 */
public class TrackingResultTable {

	/**
	 * This a double key for the Map<,> of the TrackingResultTable class.
	 * 
	 * 
	 * 
	 */
	protected class ResultKey {
		ZoneUnit zone;
		AbstractTrackingParameter parameter;

		public ResultKey(ZoneUnit zone, AbstractTrackingParameter parameter) {
			super();
			this.zone = zone;
			this.parameter = parameter;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + TrackingResultTable.this.hashCode();
			result = prime * result
					+ ((parameter == null) ? 0 : parameter.hashCode());
			result = prime * result + ((zone == null) ? 0 : zone.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ResultKey other = (ResultKey) obj;
			if (parameter == null) {
				if (other.parameter != null)
					return false;
			} else if (!parameter.equals(other.parameter))
				return false;
			if (zone == null) {
				if (other.zone != null)
					return false;
			} else if (!zone.equals(other.zone))
				return false;
			return true;
		}

	}

	/**
	 * Defines the 6 default data query modes: raw data, summarization, mean,
	 * standard deviation, maximum, minimum.
	 * 
	 * 
	 * 
	 */
	public enum DataType {
		SUM, MEAN, SD, MIN, MAX, RAW
	}

	protected Map<ResultKey, TrackingResult> list;

	public TrackingResultTable() {
		list = new HashMap<ResultKey, TrackingResult>();
	}

	/**
	 * Adds a new TrackingResult to the table if keys (zone and parameter) are
	 * unique.
	 * 
	 * @param zone
	 * @param parameter
	 * @param result
	 * @return
	 */
	public boolean add(ZoneUnit zone, AbstractTrackingParameter parameter,
			TrackingResult result) {

		if (list.put(new ResultKey(zone, parameter), result) == null)
			return false;
		return true;
	}

	/**
	 * Returns data by key and data type.
	 * 
	 * @param zone
	 * @param parameter
	 * @param dataType
	 * @return return null if keys do not exist.
	 */
	public Object getValue(ZoneUnit zone, AbstractTrackingParameter parameter,
			DataType dataType) {
		TrackingResult tr = list.get(new ResultKey(zone, parameter));
		if (tr == null)
			return null;

		switch (dataType) {
		case SUM:
			return tr.getSum();
		case MAX:
			return tr.getMax();
		case MIN:
			return tr.getMin();
		case MEAN:
			return tr.getMean();
		case SD:
			return tr.getSd();
		case RAW:
			return tr.getRaws();

		}

		return null;
	}
}
