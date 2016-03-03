package hu.elte.animaltracker.controller.tracking;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Panel;
import java.awt.Scrollbar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;

import hu.elte.animaltracker.controller.listeners.ColorThersholderCtrlListener;
import hu.elte.animaltracker.model.tracking.thresholding.ColorThresholder.ColorSpaces;
import ij.IJ;
import ij.plugin.frame.ColorThresholder;

public class ColorThersholderCtrl extends ColorThresholder {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8485587944493721737L;
	private ArrayList<ColorThersholderCtrlListener> listeners = new ArrayList<ColorThersholderCtrlListener>();

	public static void main(String[] args) {
		new ColorThersholderCtrl();
	}

	public ColorThersholderCtrl() {
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

	public void addColorThersholderCtrlListener(
			ColorThersholderCtrlListener listener) {
		listeners.add(listener);
	}

	public void removeColorThersholderCtrlListener(
			ColorThersholderCtrlListener listener) {
		listeners.remove(listener);
	}

	protected void fireReturnThresholds(
			hu.elte.animaltracker.model.tracking.thresholding.ColorThresholder.ColorSpaces space,
			int[] ch1, int[] ch2, int[] ch3, boolean p1, boolean p2, boolean p3) {
		for (ColorThersholderCtrlListener listener : listeners)
			listener.returnThresholds(space, ch1, ch2, ch3, p1, p2, p3);
	}

	protected void getData() {

		int[] ch1 = new int[2];
		int[] ch2 = new int[2];
		int[] ch3 = new int[2];
		boolean p1, p2, p3;
		hu.elte.animaltracker.model.tracking.thresholding.ColorThresholder.ColorSpaces space = null;

		try {

			Scrollbar sliderMin1 = (Scrollbar) getFlield("minSlider");
			Scrollbar sliderMin2 = (Scrollbar) getFlield("minSlider2");
			Scrollbar sliderMin3 = (Scrollbar) getFlield("minSlider3");

			Scrollbar sliderMax1 = (Scrollbar) getFlield("maxSlider");
			Scrollbar sliderMax2 = (Scrollbar) getFlield("maxSlider2");
			Scrollbar sliderMax3 = (Scrollbar) getFlield("maxSlider3");

			Choice c = (Choice) getFlield("colorSpaceChoice");

			Checkbox pass1 = (Checkbox) getFlield("bandPassH");
			Checkbox pass2 = (Checkbox) getFlield("bandPassH");
			Checkbox pass3 = (Checkbox) getFlield("bandPassH");

			ch1[0] = sliderMin1.getValue();
			ch2[0] = sliderMin2.getValue();
			ch3[0] = sliderMin3.getValue();

			ch1[1] = sliderMax1.getValue();
			ch2[1] = sliderMax2.getValue();
			ch3[1] = sliderMax3.getValue();

			p1 = pass1.getState();
			p2 = pass2.getState();
			p3 = pass3.getState();

			switch (c.getSelectedIndex()) {
			case 0:
				space = ColorSpaces.HSB;
				break;
			case 1:
				space = ColorSpaces.RGB;
				break;
			case 2:
				space = ColorSpaces.Lab;
				break;
			case 3:
				space = ColorSpaces.YUV;
				break;
			default:
				break;
			}

		} catch (Exception e) {
			IJ.error("Error - Reflection");
			this.close();
			return;
		}
		fireReturnThresholds(space, ch1, ch2, ch3, p1, p2, p3);
		this.close();
	}

	private Object getFlield(String fieldName) throws SecurityException,
			NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException {
		Field field = ColorThersholderCtrl.class.getSuperclass()
				.getDeclaredField(fieldName);
		field.setAccessible(true);
		return field.get(this);
	}
}
