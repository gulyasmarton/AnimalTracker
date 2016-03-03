package hu.elte.animaltracker.controller.tracking;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import ij.ImagePlus;
import ij.gui.Toolbar;
import ij.plugin.frame.ColorPicker;
import hu.elte.animaltracker.model.tracking.filtering.ColorBackgroundSubtractor;

public class ColorBackgroundSubtractorController extends
		BackgroundSubtractorController {
	ColorBackgroundSubtractor colorBackgroundSubtractor;
	

	public ColorBackgroundSubtractorController(
			ColorBackgroundSubtractor colorBackgroundSubtractor) {
		super(colorBackgroundSubtractor);
		this.colorBackgroundSubtractor = colorBackgroundSubtractor;
	}

	public ColorBackgroundSubtractorController(ImagePlus imp,
			ColorBackgroundSubtractor colorBackgroundSubtractor) {
		super(imp, colorBackgroundSubtractor);

	}

	public void setThreshold() {
		// TODO Auto-generated method stub

	}

	public void setBackground() {
		ColorPicker picker = new ColorPicker();	
		picker.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {
				colorBackgroundSubtractor.setBackground(Toolbar.getBackgroundColor().getRGB());
				
			}
			
			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

}
