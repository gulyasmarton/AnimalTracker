package hu.elte.animaltracker.controller.listeners;

import ij.ImagePlus;

public interface MedianImageControllerListener {

	/**
	 * Erre a figyel�re egy�ltal�n nincs sz�ks�g csak annyit, hogy bez�rt.
	 * @param imp
	 * @param sigma
	 */
	void MedianImageCallBack();	
}
