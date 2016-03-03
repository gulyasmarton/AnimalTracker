package hu.elte.animaltracker.model.zones;

import hu.elte.animaltracker.model.listeners.SetOperatorListener;
import hu.elte.animaltracker.model.listeners.ZoneUnitListener;
import ij.gui.Roi;
import ij.gui.ShapeRoi;

import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Derived classes of SetOperator carry out the basic logical operations amongst
 * ZoneUnits.
 * 
 * @see hu.elte.animaltracker.model.zones.SetExclusive
 * @see hu.elte.animaltracker.model.zones.SetIntersection
 * @see hu.elte.animaltracker.model.zones.SetSubtraction
 * @see hu.elte.animaltracker.model.zones.SetUnion
 */
public abstract class SetOperator implements ZoneUnit, Serializable, Cloneable {

	private static final long serialVersionUID = -4618542038831993672L;

	protected boolean isVisible = true;
	protected List<ZoneUnit> elements;
	protected transient ShapeRoi roi;
	protected transient static int globalSetID = 0;
	protected int setID;
	protected String name;
	private transient ArrayList<SetOperatorListener> setOperatorListeners = new ArrayList<SetOperatorListener>();
	private transient ArrayList<ZoneUnitListener> zoneUnitListeners = new ArrayList<ZoneUnitListener>();

	/**
	 * Creates a new logical operation amongst elements of the given ZoneUnit
	 * list.
	 * 
	 */
	public SetOperator(List<ZoneUnit> zoneUnits) {
		this.elements = zoneUnits;
		for (ZoneUnit zoneUnit : elements) {
			zoneUnit.addZoneUnitListener(new ZoneUnitListener() {

				@Override
				public void onRoiChanged(ZoneUnit source) {
					// TODO Auto-generated method stub
					RoiUpdate();
				}
			});
		}
		setID = ++globalSetID;
		RoiUpdate();
	}

	private void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
	}

	private void readObject(ObjectInputStream ois)
			throws ClassNotFoundException, IOException {
		ois.defaultReadObject();
		setOperatorListeners = new ArrayList<SetOperatorListener>();
		zoneUnitListeners = new ArrayList<ZoneUnitListener>();
		globalSetID = setID;
		for (ZoneUnit zoneUnit : elements) {
			zoneUnit.addZoneUnitListener(new ZoneUnitListener() {

				@Override
				public void onRoiChanged(ZoneUnit source) {
					// TODO Auto-generated method stub
					RoiUpdate();
				}
			});
		}
		RoiUpdate();
	}

	private void fireRoiChanged() {
		for (ZoneUnitListener l : zoneUnitListeners)
			l.onRoiChanged(this);
	}

	@Override
	public boolean isVisible() {
		// TODO Auto-generated method stub
		return isVisible;
	}

	@Override
	public void setVisible(boolean isVisible) {
		// TODO Auto-generated method stub
		this.isVisible = isVisible;
	}

	@Override
	public Roi getRoi() {
		// TODO Auto-generated method stub
		// if(roi==null)RoiUpdate();
		return roi;
	}

	@Override
	public Rectangle getBounds() {
		return roi.getBounds();
	}

	/**
	 * Returns the members of the current logical operation.
	 * 
	 * @return
	 */
	public List<ZoneUnit> getElements() {
		return elements;
	}

	/**
	 * Forces the updating of the result ROI of the current logical operation.
	 */
	public void RoiUpdate() {
		fireRoiChanged();
	}

	public void addSetOperatorListener(SetOperatorListener l) {
		setOperatorListeners.add(l);
	}

	public void removeSetOperatorListener(SetOperatorListener l) {
		setOperatorListeners.remove(l);
	}

	@Override
	public void addZoneUnitListener(ZoneUnitListener l) {
		// TODO Auto-generated method stub
		zoneUnitListeners.add(l);
	}

	@Override
	public void removeZoneUnitListener(ZoneUnitListener l) {
		// TODO Auto-generated method stub
		zoneUnitListeners.remove(l);
	}

	/**
	 * Adds a new ZoneUnit to the current logical operation.
	 * 
	 * @param zoneUnit
	 */
	public void addZoneUnit(ZoneUnit zoneUnit) {
		if (!elements.add(zoneUnit))
			return;
		zoneUnit.addZoneUnitListener(new ZoneUnitListener() {

			@Override
			public void onRoiChanged(ZoneUnit source) {
				// TODO Auto-generated method stub
				RoiUpdate();
			}
		});

		fireZoneUnitAdded(zoneUnit);
	}

	/**
	 * Removes a new ZoneUnit from the current logical operation.
	 * 
	 * @param zoneUnit
	 */
	public void removeZoneUnit(ZoneUnit zoneUnit) {
		if (!elements.remove(zoneUnit))
			return;
		zoneUnit.addZoneUnitListener(new ZoneUnitListener() {
			@Override
			public void onRoiChanged(ZoneUnit source) {
				// TODO Auto-generated method stub
				RoiUpdate();
			}
		});
		fireZoneUnitRemoved(zoneUnit);
	}

	private void fireZoneUnitAdded(ZoneUnit zoneUnit) {
		RoiUpdate();
		for (SetOperatorListener l : setOperatorListeners) {
			l.onItemsAdded(this, zoneUnit);
		}
	}

	private void fireZoneUnitRemoved(ZoneUnit zoneUnit) {
		RoiUpdate();
		for (SetOperatorListener l : setOperatorListeners) {
			l.onItemsRemoved(this, zoneUnit);
		}
	}

	@Override
	public void setLocation(double x, double y) {
		for (ZoneUnit element : elements) {
			element.setLocation(x, y);
		}
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	protected ZoneUnit clone() {
		return duplicate();
	}

	@Override
	public String toString() {
		return getName();
	}
}
