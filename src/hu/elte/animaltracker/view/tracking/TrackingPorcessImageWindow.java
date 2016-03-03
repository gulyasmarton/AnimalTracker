package hu.elte.animaltracker.view.tracking;

import java.awt.Choice;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import hu.elte.animaltracker.model.tracking.CoreTracker;
import ij.ImagePlus;
import ij.gui.ImageWindow;
import ij.process.ImageProcessor;

public class TrackingPorcessImageWindow extends ImageWindow {

	private static final long serialVersionUID = -8496706982276500116L;
	protected ImageProcessor ip;
	protected Choice processing;
	protected ImagePlus imp;
	protected CoreTracker coreTracker;

	public TrackingPorcessImageWindow(ImagePlus imp, CoreTracker coreTracker) {
		super(imp);
		ip = imp.getProcessor().duplicate();
		this.imp = imp;
		this.coreTracker = coreTracker;

		processing = new Choice();
		processing.add("original");
		processing.add("filtered");
		processing.add("thresholded");
		processing.add("postprocessed");

		processing.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				updateCanvas();
			}
		});

		add(processing);

		setSize(new Dimension(getSize().width, getSize().height + 30));

	}

	public void setProcessor(ImageProcessor ip) {
		this.ip = ip.duplicate();
		updateCanvas();
	}

	protected void updateCanvas() {
		switch (processing.getSelectedIndex()) {
		case 0:
			imp.setProcessor(ip);
			break;
		case 1:
			imp.setProcessor(coreTracker.getFilteredImage(ip));
			break;
		case 2:
			imp.setProcessor(coreTracker.getBinaryImage(ip).getByteProcessor());
			break;
		case 3:
			imp.setProcessor(coreTracker.getPostProcessedImage(ip)
					.getByteProcessor());
			break;
		}
		imp.updateImage();

	}

}
