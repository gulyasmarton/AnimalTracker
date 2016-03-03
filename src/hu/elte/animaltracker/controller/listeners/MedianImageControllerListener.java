package hu.elte.animaltracker.controller.listeners;

import ij.ImagePlus;

public interface MedianImageControllerListener {

	/**
	 * Erre a figyelõre egyáltalán nincs szükség csak annyit, hogy bezárt.
	 * @param imp
	 * @param sigma
	 */
	void MedianImageCallBack();	
}
