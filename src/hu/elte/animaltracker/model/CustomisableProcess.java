package hu.elte.animaltracker.model;

/**
 * This interface is used for processing classes where it may be needed to set
 * some parameters through a GUI.
 * 
 * 
 * 
 */
public interface CustomisableProcess {

	/**
	 * Returns a new Instance of the original object. It can be used if the
	 * class does not have a constructor without a parameter.
	 * 
	 * @return
	 */
	CustomisableProcess getNewInstance();

	/**
	 * Returns the name of the process.
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Shows the GUI if it exists.
	 */
	void showGUI();

}
