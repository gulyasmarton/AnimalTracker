package hu.elte.animaltracker.model.zones;

import ij.gui.Roi;
import ij.gui.ShapeRoi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Unary union operator. This class carries out the union of the areas of
 * ZoneUnits.
 * 
 */
public class SetUnion extends SetOperator implements Serializable {

	private static final long serialVersionUID = 1469020952154738026L;

	public SetUnion(List<ZoneUnit> zoneUnit) {
		super(zoneUnit);
	}

	public SetUnion(ZoneUnit... zoneUnit) {
		super(Arrays.asList(zoneUnit));
	}

	@Override
	public String getName() {
		if (name != null)
			return name;
		return "Union " + setID;
	}

	@Override
	public void RoiUpdate() {
		// TODO Auto-generated method stub
		roi = null;
		for (ZoneUnit unit : elements) {
			Roi r = unit.getRoi();
			if (r == null)
				continue;
			if (roi == null)
				roi = new ShapeRoi(r);
			else
				roi.or(new ShapeRoi(r));
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
		SetOperator operator = new SetUnion(items);
		operator.setVisible(false);
		return operator;
	}

}
