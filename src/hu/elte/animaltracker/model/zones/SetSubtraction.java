package hu.elte.animaltracker.model.zones;

import ij.gui.ShapeRoi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Unary subtraction operator. This class subtracts the overlapping regions of
 * others elements from the first element.
 * 
 */
public class SetSubtraction extends SetOperator implements Serializable {

	private static final long serialVersionUID = 2358393006501410140L;

	public SetSubtraction(List<ZoneUnit> zoneUnit) {
		super(zoneUnit);
	}

	public SetSubtraction(ZoneUnit... zoneUnit) {
		super(Arrays.asList(zoneUnit));
	}

	@Override
	public String getName() {
		if (name != null)
			return name;
		return "Subtraction " + setID;
	}

	@Override
	public void RoiUpdate() {
		// TODO Auto-generated method stub
		for (ZoneUnit unit : elements) {
			if (roi == null)
				roi = new ShapeRoi(unit.getRoi());
			else
				roi.not(new ShapeRoi(unit.getRoi()));
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
		SetOperator operator = new SetSubtraction(items);
		operator.setVisible(false);
		return operator;
	}
}