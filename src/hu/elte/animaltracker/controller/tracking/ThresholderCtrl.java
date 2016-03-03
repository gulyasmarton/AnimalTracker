package hu.elte.animaltracker.controller.tracking;

import java.awt.Button;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import hu.elte.animaltracker.controller.listeners.ThersholderCtrlListener;
import ij.IJ;
import ij.plugin.frame.ThresholdAdjuster;

public class ThresholderCtrl extends ThresholdAdjuster {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1181713099168154933L;
	List<ThersholderCtrlListener> listeners = new ArrayList<ThersholderCtrlListener>();

	public ThresholderCtrl() {
		super();

		Panel c = (Panel) this.getComponent(this.getComponentCount() - 1);

		Button getThresholdBtn = new Button("Return");
		getThresholdBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				getData();
			}
		});
		c.add(getThresholdBtn);
	}

	public void addThersholderCtrlListener(ThersholderCtrlListener listener) {
		if (!listeners.contains(listener))
			listeners.add(listener);
	}

	public void removeThersholderCtrlListener(ThersholderCtrlListener listener) {
		listeners.remove(listener);
	}

	protected void getData() {

		try {

			double min = (Double) getFlield("minThreshold");
			double max = (Double) getFlield("maxThreshold");

			fireReturnThresholds(min, max);

		} catch (Exception e) {
			IJ.error("Error - Reflection");
			this.close();
			return;
		}

		this.close();
	}

	private void fireReturnThresholds(double min, double max) {
		for (ThersholderCtrlListener listener : listeners) {
			listener.returnThresholds(min, max);
		}

	}

	private Object getFlield(String fieldName) throws SecurityException,
			NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException {
		Field field = ThresholdAdjuster.class.getDeclaredField(fieldName);
		field.setAccessible(true);
		return field.get(this);
	}
}
