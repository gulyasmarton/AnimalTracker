package hu.elte.animaltracker.model.zones;

import java.awt.Rectangle;

import hu.elte.animaltracker.model.listeners.ZoneUnitListener;
import ij.gui.Roi;

/**
 * This is a general descriptor of a tracking zone.
 */
public interface ZoneUnit {
	/**
	 * Returns 'true' if the ZoneUnit is visible.
	 */
	boolean isVisible();

	/**
	 * Sets the visibility of the ZoneUnit.
	 */
	void setVisible(boolean isVisible);

	/**
	 * Returns an ImageJ ROI version of this ZoneUnit.
	 */
	Roi getRoi();

	/**
	 * Returns the bounding rectangle of this ZoneUnit.
	 */
	Rectangle getBounds();

	void addZoneUnitListener(ZoneUnitListener l);

	void removeZoneUnitListener(ZoneUnitListener l);

	/**
	 * Sets the new location of this ZoneUnit.
	 */
	void setLocation(double x, double y);

	/**
	 * Returns a shadow copy of this ZoneUnit.
	 */
	ZoneUnit duplicate();

	/**
	 * Sets the name of this ZoneUnit.
	 */
	void setName(String name);

	/**
	 * Returns the name of this ZoneUnit.
	 */
	String getName();

}
