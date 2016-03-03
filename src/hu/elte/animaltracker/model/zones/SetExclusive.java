package hu.elte.animaltracker.model.zones;

import ij.gui.ShapeRoi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Unary 'exclusive or' operator. This class carries out the selection of the
 * whole of non-overlapping regions amongst ZoneUnits.
 * 
 */
public class SetExclusive extends SetOperator implements Serializable,
		Cloneable {

	private static final long serialVersionUID = 5510942152176089479L;

	public SetExclusive(List<ZoneUnit> zoneUnit) {
		super(zoneUnit);
	}

	public SetExclusive(ZoneUnit... zoneUnit) {
		super(Arrays.asList(zoneUnit));
	}

	@Override
	public String getName() {
		if (name != null)
			return name;
		return "Exclusive Or " + setID;
	}

	@Override
	public void RoiUpdate() {
		// TODO Auto-generated method stub
		for (ZoneUnit unit : elements) {
			if (roi == null)
				roi = new ShapeRoi(unit.getRoi());
			else
				roi.xor(new ShapeRoi(unit.getRoi()));
			unit.setVisible(false);
		}
		super.RoiUpdate();
	}

	@Override
	protected ZoneUnit clone() {
		List<ZoneUnit> items = new ArrayList<ZoneUnit>(elements.size());
		for (ZoneUnit zoneUnit : elements) {
			items.add(zoneUnit.duplicate());
		}
		SetOperator operator = new SetExclusive(items);
		operator.setVisible(false);
		return operator;
	};

	@Override
	public ZoneUnit duplicate() {
		return clone();
	}

}
