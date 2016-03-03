package hu.elte.animaltracker.controller.listeners;

import java.awt.geom.Point2D;

public interface EditZoneUnitControllerListener {
	void anchorChanged(Point2D.Double anchor);
	void overlayerChanged();
	void roiWidthChanged(double width);
	void roiHeightChanged(double heigth);
	void roiLocationXChanged(double x);
	void roiLocationYChanged(double y);
}
