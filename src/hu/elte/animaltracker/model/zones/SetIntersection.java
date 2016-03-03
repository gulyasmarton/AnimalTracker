package hu.elte.animaltracker.model.zones;

import ij.gui.ShapeRoi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Unary intersection operator. This class carries out the selection of the
 * overlapping regions amongst ZoneUnits.
 * 
 */
public class SetIntersection extends SetOperator implements Serializable {

	private static final long serialVersionUID = 6319049367551926627L;

	public SetIntersection(List<ZoneUnit> zoneUnit) {
		super(zoneUnit);
	}

	public SetIntersection(ZoneUnit... zoneUnit) {
		super(Arrays.asList(zoneUnit));
	}

	@Override
	public String getName() {
		if (name != null)
			return name;
		return "Intersection " + setID;
	}

	@Override
	public void RoiUpdate() {
		// TODO Auto-generated method stub
		for (ZoneUnit unit : elements) {
			if (roi == null)
				roi = new ShapeRoi(unit.getRoi());
			else
				roi.and(new ShapeRoi(unit.getRoi()));
			unit.setVisible(false);
		}
		super.RoiUpdate();
	}

	@Override
	public ZoneUnit duplicate() {
		List<ZoneUnit> items = new ArrayList<ZoneUnit>(elements.size());
		for (ZoneUnit zoneUnit : elements) {
			items.add(zoneUnit.duplicate());
		}
		SetOperator operator = new SetIntersection(items);
		operator.setVisible(false);
		return operator;
	}

}
