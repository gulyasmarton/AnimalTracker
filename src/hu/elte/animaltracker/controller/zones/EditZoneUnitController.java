package hu.elte.animaltracker.controller.zones;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.OvalRoi;
import ij.gui.PointRoi;
import ij.gui.PolygonRoi;
import ij.gui.Roi;
import ij.measure.Calibration;
import ij.process.FloatPolygon;

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.DefaultListModel;

import hu.elte.animaltracker.controller.listeners.EditZoneUnitControllerListener;
import hu.elte.animaltracker.model.zones.Primitive;
import hu.elte.animaltracker.model.zones.SetOperator;
import hu.elte.animaltracker.model.zones.ZoneUnit;

public class EditZoneUnitController {

	public class PolygonPoint {
		FloatPolygon polygon;
		int idx;

		public PolygonPoint(FloatPolygon polygon, int idx) {
			super();
			this.polygon = polygon;
			this.idx = idx;
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "p" + (idx + 1) + "(" + polygon.xpoints[idx] + "; "
					+ polygon.ypoints[idx] + ")";
		}

		public float getX() {
			// TODO Auto-generated method stub
			return polygon.xpoints[idx];
		}

		public float getY() {
			// TODO Auto-generated method stub
			return polygon.ypoints[idx];
		}

		public void setPoint(double x, double y) {
			// TODO Auto-generated method stub
			polygon.xpoints[idx] = (float) x;
			polygon.ypoints[idx] = (float) y;
		}

		public FloatPolygon getPolygon() {
			// TODO Auto-generated method stub
			return polygon;
		}

	}

	private ZoneUnit zoneUnit;
	private ImagePlus imp;
	public final int TOPLEFT = 0, TOP = 1, TOPRIGHT = 2, LEFT = 3, CENTER = 4,
			RIGHT = 5, BOTTOMLEFT = 6, BOTTOM = 7, BOTTOMRIGHT = 8;
	private int anchor;
	private Point2D.Double anchorXY;
	private DefaultListModel polygonModel = new DefaultListModel();

	private ArrayList<EditZoneUnitControllerListener> listeners = new ArrayList<EditZoneUnitControllerListener>();

	public EditZoneUnitController(ImagePlus imp, ZoneUnit zoneUnit) {
		this.zoneUnit = zoneUnit;
		if (zoneUnit instanceof SetOperator
				|| ((Primitive) zoneUnit).getType() == Roi.FREEROI) {
			zoneUnit.setVisible(true);
		} else {
			imp.setRoi(zoneUnit.getRoi());
		}
		polygonModelUpload();

		this.imp = imp;
		// Arc2D.Double d = new Arc2D.Double(0,0,10,20,20,30,Arc2D.PIE);

		// ShapeRoi s = new ShapeRoi(d);
		// this.imp.setRoi(s);
	}

	public DefaultListModel getPolygonModel() {
		return polygonModel;
	}

	private void polygonModelUpload() {
		if (!(zoneUnit instanceof Primitive))
			return;
		Primitive primitive = (Primitive) zoneUnit;
		if (primitive.getType() != Roi.POLYGON)
			return;
		polygonModel.clear();
		FloatPolygon polygon = ((PolygonRoi) primitive.getRoi())
				.getFloatPolygon();
		// polygonModel = new DefaultListModel();
		for (int i = 0; i < polygon.npoints; i++) {
			polygonModel.addElement(new PolygonPoint(polygon, i));
			/*
			 * polygonModel.addElement(new Point2D.Double(polygon.xpoints[i],
			 * polygon.ypoints[i]));
			 */
		}
	}

	private Point2D.Double convertToUnit(double x, double y) {
		Calibration calibration = imp.getCalibration();
		return new Point2D.Double(calibration.getX(x), calibration.getY(y));
	}

	private Point2D.Double convertToPixel(double x, double y) {
		Calibration calibration = imp.getCalibration();
		return new Point2D.Double(calibration.getRawX(x),
				calibration.getRawY(y));
	}

	public Point2D.Double getAnchor() {
		Rectangle rectangle = zoneUnit.getBounds();
		switch (anchor) {
		case TOPLEFT:
			return convertToUnit(rectangle.getX(), rectangle.getY());
		case TOP:
			return convertToUnit(rectangle.getCenterX(), rectangle.getY());
		case TOPRIGHT:
			return convertToUnit(rectangle.getMaxX(), rectangle.getY());
		case LEFT:
			return convertToUnit(rectangle.getX(), rectangle.getCenterY());
		case CENTER:
			return convertToUnit(rectangle.getCenterX(), rectangle.getCenterY());
		case RIGHT:
			return convertToUnit(rectangle.getMaxX(), rectangle.getCenterY());
		case BOTTOMLEFT:
			return convertToUnit(rectangle.getX(), rectangle.getMaxY());
		case BOTTOM:
			return convertToUnit(rectangle.getCenterX(), rectangle.getMaxY());
		case BOTTOMRIGHT:
			return convertToUnit(rectangle.getMaxX(), rectangle.getMaxY());
		}
		return null;

	}

	public String getPrimitiveName() {
		return zoneUnit.toString();
	}

	public void setAnchor(int anchor) {
		// TODO Auto-generated method stub
		this.anchor = anchor;
		this.anchorXY = getAnchor();
		firedAncherChanged();
	}

	private void firedAncherChanged() {
		// TODO Auto-generated method stub
		for (EditZoneUnitControllerListener listener : listeners)
			listener.anchorChanged(anchorXY);
	}

	private void firedroiWidthChanged(double v) {
		// TODO Auto-generated method stub
		for (EditZoneUnitControllerListener listener : listeners)
			listener.roiWidthChanged(v);
	}

	private void firedroiHeightChanged(double v) {
		// TODO Auto-generated method stub
		for (EditZoneUnitControllerListener listener : listeners)
			listener.roiHeightChanged(v);
	}

	private void firedroiLocationXChanged(double v) {
		// TODO Auto-generated method stub
		for (EditZoneUnitControllerListener listener : listeners)
			listener.roiLocationXChanged(v);
	}

	private void firedroiLocationYChanged(double v) {
		// TODO Auto-generated method stub
		for (EditZoneUnitControllerListener listener : listeners)
			listener.roiLocationYChanged(v);
	}

	public void addEditPrimitiveControllerListener(
			EditZoneUnitControllerListener listener) {
		listeners.add(listener);
	}

	public void removeEditPrimitiveControllerListener(
			EditZoneUnitControllerListener listener) {
		listeners.remove(listener);
	}

	public String getUnit() {
		// TODO Auto-generated method stub
		return imp.getCalibration().getUnit();
	}

	public boolean isSizeEnable() {
		if (zoneUnit instanceof SetOperator
				|| ((Primitive) zoneUnit).getType() == Roi.FREEROI
				|| ((Primitive) zoneUnit).getType() == Roi.POLYGON)
			return false;
		return true;
	}

	public boolean isPolygon() {
		if (zoneUnit instanceof Primitive
				&& ((Primitive) zoneUnit).getType() == Roi.POLYGON)
			return true;
		return false;
	}

	public String getwLabel() {
		if (!(zoneUnit instanceof Primitive))
			return "";
		Primitive p = (Primitive) zoneUnit;

		switch (p.getType()) {
		case Roi.FREEROI:
		case Roi.POLYGON:
		case Roi.RECTANGLE:
			return "Width";
		case Roi.OVAL:
			return "Radius 1";
		}
		return "";
	}

	public String gethLabel() {
		if (!(zoneUnit instanceof Primitive))
			return "";
		Primitive p = (Primitive) zoneUnit;

		switch (p.getType()) {
		case Roi.FREEROI:
		case Roi.POLYGON:
		case Roi.RECTANGLE:
			return "Height";
		case Roi.OVAL:
			return "Radius 2";
		}
		return "";
	}

	public double getWidth() {
		// TODO Auto-generated method stub

		Rectangle rectangle = zoneUnit.getBounds();
		Calibration calibration = imp.getCalibration();

		if ((zoneUnit instanceof Primitive)) {
			Primitive p = (Primitive) zoneUnit;
			if (p.getType() == Roi.OVAL)
				return calibration.getX(rectangle.getWidth() / 2d);
		}
		return calibration.getX(rectangle.getWidth());
	}

	public double getHeight() {
		// TODO Auto-generated method stub
		Rectangle rectangle = zoneUnit.getBounds();
		Calibration calibration = imp.getCalibration();

		if ((zoneUnit instanceof Primitive)) {
			Primitive p = (Primitive) zoneUnit;
			if (p.getType() == Roi.OVAL)
				return calibration.getY(rectangle.getHeight() / 2d);
		}
		return calibration.getY(rectangle.getHeight());
	}

	private Point2D.Double scaleTo(Point2D.Double center, Point2D.Double scale,
			Point2D.Double p) {
		// TODO Auto-generated method stub

		double vx = p.x - center.x;
		double vy = p.y - center.y;

		vx *= scale.x;
		vy *= scale.y;

		return new Point2D.Double(center.x + vx, center.y + vy);
	}

	public void setSize2(double x, double y) {
		// TODO Auto-generated method stub
		Primitive p = (Primitive) zoneUnit;
		Point2D.Double size = convertToPixel(x, y);
		Point2D.Double location = getAnchor();
		location = convertToPixel(location.x, location.y);

		switch (anchor) {
		case TOP:
			location.x -= size.x / 2d;
			break;
		case TOPRIGHT:
			location.x -= size.x;
			break;
		case LEFT:
			location.y -= size.y / 2d;
			break;
		case CENTER:
			location.x -= size.x / 2d;
			location.y -= size.y / 2d;
			break;
		case RIGHT:
			location.x -= size.x;
			location.y -= size.y / 2d;
		case BOTTOMLEFT:
			location.y -= size.y;
			break;
		case BOTTOM:
			location.x -= size.x / 2d;
			location.y -= size.y;
			break;
		case BOTTOMRIGHT:
			location.x -= size.x;
			location.y -= size.y;
			break;
		}
		if (p.getType() == Roi.RECTANGLE)
			p.setRoi(new Roi(location.x, location.y, size.x, size.y));
		else
			p.setRoi(new OvalRoi(location.x, location.y, size.x, size.y));
		imp.setRoi(zoneUnit.getRoi());
	}

	public void setRoi() {
		// TODO Auto-generated method stub
		Roi r = imp.getRoi();
		if (r == null) {
			IJ.error("no roi");
			return;
		}
		if (!(zoneUnit instanceof Primitive)) {
			IJ.error("just Primitives");
			return;
		}

		Primitive primitive = (Primitive) zoneUnit;
		if (r.getType() != primitive.getType()) {
			IJ.error("Not same type");
			return;
		}
		primitive.setRoi(r);
		polygonModelUpload();

	}

	public void updatePolygon(FloatPolygon polygon) {
		Primitive primitive = (Primitive) zoneUnit;
		Roi r = new PolygonRoi(polygon, Roi.POLYGON);
		Rectangle rec = r.getBounds();
		firedroiWidthChanged(rec.getWidth());
		firedroiHeightChanged(rec.getHeight());
		firedroiLocationXChanged(rec.getX());
		firedroiLocationYChanged(rec.getY());
		primitive.setRoi(r);
		imp.setRoi(r);
	}

	public void RemovePolygonPoint(PolygonPoint polygon) {
		if (polygon == null)
			return;
		if (polygon.polygon.npoints < 4)
			return;
		int idx = polygon.idx;
		polygonModel.clear();
		for (int i = idx; i < polygon.polygon.npoints - 1; i++) {
			polygon.polygon.xpoints[i] = polygon.polygon.xpoints[i + 1];
			polygon.polygon.ypoints[i] = polygon.polygon.ypoints[i + 1];

		}
		FloatPolygon p = new FloatPolygon(polygon.polygon.xpoints,
				polygon.polygon.ypoints, polygon.polygon.npoints - 1);
		updatePolygon(p);
		polygonModelUpload();
	}
	public boolean isPrimitive(){
		return zoneUnit instanceof Primitive;
	}

	public void setTransform(double x, double y, double w, double h) {
		// TODO Auto-generated method stub

//		if (w <= 0 || h <= 0) {
//			IJ.error("invalid width or height!");
//			return;
//		}
		Double anchor = getAnchor();
		double xs = x-anchor.x ;
		double ys = y-anchor.y;

		if (zoneUnit instanceof Primitive) {
			Primitive p = (Primitive) zoneUnit;
			setTransformPrimitive(p, x, y, w, h);
		} else {

			trafo(zoneUnit, xs, ys);
		}
		imp.setRoi(zoneUnit.getRoi());
	}

	private void trafo(ZoneUnit unit, double w, double h) {
		if (unit instanceof Primitive)
			setTransformPrimitiveSc((Primitive) unit, w, h);
		else {
			SetOperator op = (SetOperator) unit;
			for (ZoneUnit u : op.getElements()) {
				trafo(u, w, h);
			}
		}
	}

	private void setTransformPrimitiveSc(Primitive p, double x, double y) {
		Roi r = p.getRoi();
		Roi n = null;
		Rectangle rec = r.getBounds();
		switch (r.getType()) {
		case Roi.RECTANGLE:
			n = new Roi(rec.getX()+x, rec.getY()+y, rec.getWidth(),
					rec.getHeight());
			break;
		case Roi.OVAL:
			n = new OvalRoi(rec.getX()+x, rec.getY()+y, rec.getWidth(),
					rec.getHeight());
			break;
		case Roi.FREELINE:
		case Roi.POLYGON:
			FloatPolygon pl = ((PolygonRoi) r).getFloatPolygon();		

			for (int i = 0; i < pl.npoints; i++) {
				
				pl.xpoints[i] += x;
				pl.ypoints[i] += y;
			}
			n = new PolygonRoi(pl, r.getType());
		}
		p.setRoi(n);
	}

	private void setTransformPrimitive(Primitive p, double x, double y,
			double w, double h) {

		Point2D.Double location = convertToPixel(x, y);
		Point2D.Double locationPol = convertToPixel(x, y);
		FloatPolygon pl = null;
		Point2D.Double size = convertToPixel(w, h);
		Point2D.Double sizePol = convertToPixel(w, h);

		if (p.getType() == Roi.POLYGON || p.getType() == Roi.FREEROI) {
			pl = ((PolygonRoi) p.getRoi()).getFloatPolygon();

			Rectangle rec = p.getBounds();
			Point2D.Double scale = new Point2D.Double(w / rec.width, h
					/ rec.height);
			Point2D.Double center = getAnchor();

			for (int i = 0; i < pl.npoints; i++) {
				Point2D.Double xy = scaleTo(center, scale, new Point2D.Double(
						pl.xpoints[i], pl.ypoints[i]));
				pl.xpoints[i] = (float) xy.x;
				pl.ypoints[i] = (float) xy.y;
			}
			rec = pl.getBounds();
			// location = new Point2D.Double(rec.x,rec.y);
			locationPol = new Point2D.Double(rec.x, rec.y);
			sizePol = new Point2D.Double(rec.width, rec.height);
		}

		switch (anchor) {
		case TOP:
			location.x -= size.x / 2d;
			// locationPol.x += sizePol.x / 2d;
			break;
		case TOPRIGHT:
			location.x -= size.x;
			// locationPol.x -= sizePol.x;
			break;
		case LEFT:
			location.y -= size.y / 2d;
			// locationPol.y -= sizePol.y / 2d;
			break;
		case CENTER:
			location.x -= size.x / 2d;
			location.y -= size.y / 2d;
			// locationPol.x -= sizePol.x / 2d;
			// locationPol.y -= sizePol.y / 2d;
			break;
		case RIGHT:
			location.x -= size.x;
			location.y -= size.y / 2d;
			// locationPol.x -= sizePol.x;
			// locationPol.y -= sizePol.y / 2d;
		case BOTTOMLEFT:
			location.y -= sizePol.y;
			// locationPol.y -= sizePol.y;
			break;
		case BOTTOM:
			location.x -= size.x / 2d;
			location.y -= size.y;
			// locationPol.x -= sizePol.x / 2d;
			// locationPol.y -= sizePol.y;
			break;
		case BOTTOMRIGHT:
			location.x -= size.x;
			location.y -= size.y;
			// locationPol.x -= sizePol.x;
			// locationPol.y -= sizePol.y;
			break;
		}
		if (p.getType() == Roi.RECTANGLE)
			p.setRoi(new Roi(location.x, location.y, size.x, size.y));
		else if (p.getType() == Roi.OVAL)
			p.setRoi(new OvalRoi(location.x, location.y, size.x, size.y));
		else {
			location.x -= locationPol.x;
			location.y -= locationPol.y;
			for (int i = 0; i < pl.npoints; i++) {
				pl.xpoints[i] += (float) location.x;
				pl.ypoints[i] += (float) location.y;
			}
			p.setRoi(new PolygonRoi(pl, p.getType()));

			polygonModelUpload();

		}

		// Point2D.Double scale = new Point2D.Double(x / rec.width, y /
		// rec.height);
		// Point2D.Double center = getAnchor();
	}

	public void insertPolygonPoint(float x, float y, int idx) {
		// TODO Auto-generated method stub
		Primitive primitive = (Primitive) zoneUnit;
		FloatPolygon polygon = ((PolygonRoi) primitive.getRoi())
				.getFloatPolygon();
		int n = polygon.npoints + 1;
		float[] xk = Arrays.copyOf(polygon.xpoints, n);
		float[] yk = Arrays.copyOf(polygon.ypoints, n);

		if (idx == -1) {
			xk[n - 1] = x;
			yk[n - 1] = y;
		} else {
			float a1 = xk[idx];
			float a2 = yk[idx];
			xk[idx] = x;
			yk[idx] = y;
			for (int i = idx + 1; i < n; i++) {
				float b1 = xk[i];
				xk[i] = a1;
				a1 = b1;

				float b2 = yk[i];
				yk[i] = a2;
				a2 = b2;
			}
		}

		polygon = new FloatPolygon(xk, yk);
		updatePolygon(polygon);

		polygonModelUpload();
	}

	public void addPoligonPointByPointer(int idx) {
		// TODO Auto-generated method stub
		Roi r = imp.getRoi();
		if (r == null || r.getType() != Roi.POINT) {
			IJ.error("No pointer roi");
			return;
		}
		PointRoi point = (PointRoi) r;
		insertPolygonPoint((float) point.getXBase(), (float) point.getYBase(),
				idx);

	}
}
