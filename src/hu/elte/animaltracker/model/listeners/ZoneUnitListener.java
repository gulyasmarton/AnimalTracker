package hu.elte.animaltracker.model.listeners;

import hu.elte.animaltracker.model.zones.ZoneUnit;

/**
 * This interface contains action events of a ZoneUnit.
 * 
 * @see hu.elte.animaltracker.model.zones.ZoneUnit
 */
public interface ZoneUnitListener {
	/**
	 * Fires when a ZoneUnit is changed.
	 * 
	 * @param source
	 *            changed ZoneUnit.
	 */
	void onRoiChanged(ZoneUnit source);
}
