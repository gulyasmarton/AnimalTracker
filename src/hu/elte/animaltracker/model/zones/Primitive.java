package hu.elte.animaltracker.model.zones;

import hu.elte.animaltracker.model.listeners.ZoneUnitListener;
import ij.gui.OvalRoi;
import ij.gui.PolygonRoi;
import ij.gui.Roi;
import ij.gui.ShapeRoi;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This is a wrapper class of the ImageJ ROIs. These geometric primitives can be
 * used for building more complex shapes.
 * 
 */
public class Primitive implements ZoneUnit, Serializable, Cloneable {
	private static final long serialVersionUID = 1220653912779269033L;
	private int type;
	private Rectangle rec;
	private Polygon pol;
	private boolean isVisible;
	private String name;
	private transient static int globalRoiID = 0;
	private transient int roiID;
	private transient ArrayList<ZoneUnitListener> listeners = new ArrayList<ZoneUnitListener>();
	private transient Roi roi;

	/**
	 * You can create a geometric primitive from the Rectangle, Oval, Polygon or
	 * Freehand ROI.
	 * 
	 * @return Returns null if the ROI is not supported.
	 */
	public static Primitive createPrimitive(Roi r, boolean isVisible) {
		switch (r.getType()) {
		case Roi.RECTANGLE:
		case Roi.OVAL:
		case Roi.POLYGON:
		case Roi.FREEROI:
			return new Primitive(r, isVisible);
		}
		return null;
	}

	@Override
	public String getName() {
		if (name != null) {
			return name;
		} else if (type == Roi.RECTANGLE)
			return "ROI - Rectangle " + roiID;
		else if (type == Roi.OVAL)
			return "ROI - Ellipse " + roiID;
		else if (type == Roi.POLYGON)
			return "ROI - Polygon " + roiID;
		else if (type == Roi.FREEROI)
			return "ROI - Freehand " + roiID;
		else
			return "ROI - Unknown " + roiID;
	}

	private Primitive(Roi r, boolean isVisible) {
		this.type = r.getType();
		this.rec = r.getBounds();
		this.pol = r.getPolygon();
		roiID = ++globalRoiID;
		this.isVisible = isVisible;
	}

	private void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
	}

	private void readObject(ObjectInputStream ois)
			throws ClassNotFoundException, IOException {
		ois.defaultReadObject();
		listeners = new ArrayList<ZoneUnitListener>();
	}

	@Override
	public Roi getRoi() {
		if (roi == null) {
			if (type == Roi.RECTANGLE) {
				roi = new Roi(rec);
			} else if (type == Roi.OVAL) {
				roi = new OvalRoi(rec.x, rec.y, rec.width, rec.height);
			} else if (type == Roi.POLYGON || type == Roi.FREEROI) {
				roi = new PolygonRoi(pol, type);
			} else {
				roi = new Roi(rec);
			}
		}
		return roi;
	}

	/**
	 * Returns a new ShapeRoi.
	 * 
	 * @return
	 */
	public ShapeRoi getShapeRoi() {
		return new ShapeRoi(getRoi());
	}

	@Override
	public Rectangle getBounds() {
		return getRoi().getBounds();
	}

	/**
	 * Sets a new ROI.
	 * 
	 * @param r
	 * @return
	 */
	public boolean setRoi(Roi r) {
		switch (r.getType()) {
		case Roi.RECTANGLE:
		case Roi.OVAL:
		case Roi.POLYGON:
		case Roi.FREEROI:
			this.type = r.getType();
			this.rec = r.getBounds();
			this.pol = r.getPolygon();
			fireRoiChanged();
			roi = r;
			return true;

		}
		return false;
	}

	@Override
	public String toString() {

		return getName();
	}

	/**
	 * Returns the type of the ROI.
	 * 
	 */
	public int getType() {
		return type;
	}

	@Override
	public boolean isVisible() {
		return isVisible;
	}

	@Override
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	private void fireRoiChanged() {
		for (ZoneUnitListener l : listeners)
			l.onRoiChanged(this);
	}

	@Override
	public void addZoneUnitListener(ZoneUnitListener l) {
		listeners.add(l);
	}

	@Override
	public void removeZoneUnitListener(ZoneUnitListener l) {
		listeners.remove(l);
	}

	@Override
	public void setLocation(double x, double y) {
		Roi r = getRoi();
		r.setLocation(x, y);
		setRoi(r);
	}

	@Override
	protected ZoneUnit clone() {
		ZoneUnit zone = new Primitive(getRoi(), false);
		zone.setName(name);
		return zone;
	}

	@Override
	public ZoneUnit duplicate() {
		return clone();
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

}
