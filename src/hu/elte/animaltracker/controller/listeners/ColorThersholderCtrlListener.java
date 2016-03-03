package hu.elte.animaltracker.controller.listeners;

import hu.elte.animaltracker.model.tracking.thresholding.ColorThresholder;

public interface ColorThersholderCtrlListener {
	void returnThresholds(ColorThresholder.ColorSpaces colorSpace, int[] thr1,
			int[] thr2, int[] thr3, boolean pass1, boolean pass2, boolean pass3);
}
