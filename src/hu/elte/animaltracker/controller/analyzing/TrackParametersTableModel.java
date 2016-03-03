package hu.elte.animaltracker.controller.analyzing;

import hu.elte.animaltracker.model.analyzing.AbstractTrackingParameter;
import ij.gui.Roi;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class TrackParametersTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 3451714498412108421L;

	List<AbstractTrackingParameter> trackParameters;

	private List<Object[]> data = new ArrayList<Object[]>();

	public TrackParametersTableModel(
			List<AbstractTrackingParameter> trackParameters) {
		this.trackParameters = trackParameters;
	}

	@Override
	public int getColumnCount() {
		return trackParameters.size() + 1;
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		return data.get(row)[col];
	}

	@Override
	public String getColumnName(int col) {
		if (col == 0)
			return "Name";

		return trackParameters.get(col - 1).getName();
	}

	@Override
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
		data.get(row)[col] = value;
		fireTableCellUpdated(row, col);
	}

	public void clear() {
		data.clear();
		fireTableDataChanged();
	}

	public void addRows(List<ZoneSetting> zs){
		for (ZoneSetting setting : zs) {
			addRow(setting);
		}
	}
	
	public void addRow(ZoneSetting z) {

		Object[] row = new Object[getColumnCount() + 1];

		row[0] = z.getZone().getName();
		int idx = 1;
		for (ParameterController paramter : z
				.getParameterControllers()) {
			String s = "";
			if (paramter.needRaw)
				s += "raw, ";
			if (paramter.needSum)
				s += "sum, ";
			if (paramter.needMean)
				s += "mean, ";
			if (paramter.needSd)
				s += "sd, ";
			if (paramter.needMax)
				s += "max, ";
			if (paramter.needMin)
				s += "min, ";
			if (s.equals("")) {
				s = "none";
			} else {
				s = s.substring(0, s.length() - 2);
			}
			row[idx++] = s;
		}
		row[idx] = z.getZone().getRoi();
		data.add(row);
	}

	public Roi getRoi(int row) {
		Object r = getValueAt(row, getColumnCount());
		if (r == null)
			return null;

		return (Roi) r;
	}
}
