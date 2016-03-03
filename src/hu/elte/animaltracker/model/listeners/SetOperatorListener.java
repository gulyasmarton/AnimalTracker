package hu.elte.animaltracker.model.listeners;

import hu.elte.animaltracker.model.zones.SetOperator;
import hu.elte.animaltracker.model.zones.ZoneUnit;

/**
 * This interface contains action events of the SetOperator class.
 * 
 * @see hu.elte.animaltracker.model.zones.SetOperator
 */
public interface SetOperatorListener {
	/**
	 * Fires when the SetOperator is expanded by a new ZoneUnit;
	 * 
	 * @param source
	 * @param item
	 */
	void onItemsAdded(SetOperator source, ZoneUnit item);

	/**
	 * Fires when the SetOperator is decreased by a ZoneUnit;
	 * 
	 * @param source
	 * @param item
	 */
	void onItemsRemoved(SetOperator source, ZoneUnit item);
}
