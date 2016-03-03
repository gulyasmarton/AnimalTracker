package hu.elte.animaltracker.controller.zones;

import hu.elte.animaltracker.model.zones.Primitive;
import hu.elte.animaltracker.model.zones.SetExclusive;
import hu.elte.animaltracker.model.zones.SetIntersection;
import hu.elte.animaltracker.model.zones.SetSubtraction;
import hu.elte.animaltracker.model.zones.SetUnion;
import hu.elte.animaltracker.model.zones.ZoneUnit;
import ij.gui.Roi;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class RoiTreeCellRenderer extends DefaultTreeCellRenderer {
	final private String imageFolder = "/hu/elte/animaltracker/view/images/";

	final public ImageIcon rectangleIcon = new ImageIcon(getClass()
			.getResource(imageFolder + "rectangle.png"));
	final public ImageIcon ellipseIcon = new ImageIcon(getClass().getResource(
			imageFolder + "ellipse.png"));
	final public ImageIcon polygonIcon = new ImageIcon(getClass().getResource(
			imageFolder + "polygon.png"));
	final public ImageIcon freehandIcon = new ImageIcon(getClass().getResource(
			imageFolder + "freehand.png"));
	final public ImageIcon subtractionIcon = new ImageIcon(getClass()
			.getResource(imageFolder + "subtraction.png"));
	final public ImageIcon exclusiveIcon = new ImageIcon(getClass()
			.getResource(imageFolder + "exclusive.png"));
	final public ImageIcon intersectionIcon = new ImageIcon(getClass()
			.getResource(imageFolder + "intersection.png"));
	final public ImageIcon unionIcon = new ImageIcon(getClass().getResource(
			imageFolder + "union.png"));

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean exp, boolean leaf, int row, boolean hasFocus) {

		ZoneUnit zoneUnit = null;
		if (((DefaultMutableTreeNode) value).getUserObject() instanceof ZoneUnit)
			zoneUnit = (ZoneUnit) ((DefaultMutableTreeNode) value)
					.getUserObject();

		if (zoneUnit != null && zoneUnit instanceof SetUnion) {
			setOpenIcon(unionIcon);
			setClosedIcon(unionIcon);
		} else if (zoneUnit != null && zoneUnit instanceof SetSubtraction) {
			setOpenIcon(subtractionIcon);
			setClosedIcon(subtractionIcon);
		} else if (zoneUnit != null && zoneUnit instanceof SetExclusive) {
			setOpenIcon(exclusiveIcon);
			setClosedIcon(exclusiveIcon);
		} else if (zoneUnit != null && zoneUnit instanceof SetIntersection) {
			setOpenIcon(intersectionIcon);
			setClosedIcon(intersectionIcon);
		} else if (zoneUnit != null && zoneUnit instanceof Primitive) {
			Primitive roi = (Primitive) zoneUnit;
			if (roi.getType() == Roi.RECTANGLE) {
				setLeafIcon(rectangleIcon);
			} else if (roi.getType() == Roi.OVAL) {
				setLeafIcon(ellipseIcon);
				setForeground(Color.green);
			} else if (roi.getType() == Roi.POLYGON) {
				setLeafIcon(polygonIcon);
				setForeground(Color.blue);
			} else if (roi.getType() == Roi.FREEROI)
				setLeafIcon(freehandIcon);
		}
		super.getTreeCellRendererComponent(tree, value, sel, exp, leaf, row,
				hasFocus);

		if (zoneUnit != null) {
			if (zoneUnit.isVisible())
				setForeground(Color.black);
			else
				setForeground(Color.gray);
		}

		return this;
	}
}
