package hu.elte.animaltracker.controller.listeners;
import ij.ImagePlus;

public interface TrackerControllerListener {
	
	void activeImageChanged(ImagePlus imp);

	void TrackerControllerReturned();
}
