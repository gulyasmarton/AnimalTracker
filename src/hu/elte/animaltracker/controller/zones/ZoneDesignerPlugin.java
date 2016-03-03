package hu.elte.animaltracker.controller.zones;

import hu.elte.animaltracker.view.zones.ZoneDesignerView;
import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.plugin.PlugIn;

public class ZoneDesignerPlugin implements PlugIn {

	@Override
	public void run(String arg) {
		ImagePlus imp = WindowManager.getCurrentImage();
		if (imp == null) {
			IJ.showMessage("No images are opened.");
			return;
		}

		ZoneDesignerController zoneDesignerController = new ZoneDesignerController(
				imp);

		ZoneDesignerView zoneDesignerView = new ZoneDesignerView(
				zoneDesignerController);
		zoneDesignerView.setVisible(true);
	}

}
