package hu.elte.animaltracker.model;

import hu.elte.animaltracker.controller.analyzing.ZoneSetting;
import hu.elte.animaltracker.model.analyzing.AbstractTrackingParameter;
import hu.elte.animaltracker.model.tracking.CoreTracker;
import hu.elte.animaltracker.model.tracking.ObjectLocation;
import hu.elte.animaltracker.model.tracking.TrackingTask;
import hu.elte.animaltracker.model.zones.ZoneUnit;
import ij.io.OpenDialog;
import ij.io.SaveDialog;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains all I/O functions.
 * 
 * 
 */
public class TrackingIO {

	/**
	 * Creates a common File Open Dialog and returns the selected path.
	 * 
	 * @param title
	 *            name of the dialog.
	 * @param defaultDir
	 *            default direction of the dialog.
	 * @param defaultName
	 *            offered name of the file.
	 * @return Returns the selected path or null if the dialog was canceled.
	 */
	public static String commonOpenDialog(String title, String defaultDir,
			String defaultName) {
		OpenDialog od = null;
		if (defaultName == null)
			od = new OpenDialog(title, defaultDir == null ? "" : defaultDir);
		else {
			od = new OpenDialog(title, defaultDir == null ? "" : defaultDir,
					defaultDir);
		}
		String directory = od.getDirectory();
		String fileName = od.getFileName();
		if (fileName == null)
			return null;
		return directory + File.separator + fileName;
	}

	/**
	 * Creates a common File Save Dialog and returns the selected path.
	 * 
	 * @param title
	 *            name of the dialog.
	 * @param defaultDir
	 *            default direction of the dialog.
	 * @param defaultName
	 *            offered name of the file.
	 * @param extension
	 *            extension of the file.
	 * @return Returns the selected path or null if the dialog was canceled.
	 */
	public static String commonSaveDialog(String title, String defaultDir,
			String defaultName, String extension) {
		SaveDialog sd = null;
		if (defaultDir == null)
			defaultDir = "";
		if (extension == null)
			sd = new SaveDialog(title, defaultDir, defaultName);
		else {
			sd = new SaveDialog(title, defaultDir, defaultName, extension);
		}
		String directory = sd.getDirectory();
		String fileName = sd.getFileName();
		if (fileName == null)
			return null;
		return directory + File.separator + fileName;
	}

	/**
	 * Saves an object as a binary file.
	 */
	public static boolean saveObject(String path, Object object) {
		try {
			FileOutputStream fileOut = new FileOutputStream(path);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(object);
			out.close();
			fileOut.close();
			return true;
		} catch (IOException i) {
			i.printStackTrace();
		}
		return false;
	}

	/**
	 * Opens the binary file of an object.
	 */
	public static Object openObject(String path) {
		try {
			FileInputStream fileIn = new FileInputStream(path);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			Object object = in.readObject();
			in.close();
			fileIn.close();
			return object;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Saves an ObjectLocation array as a text file.
	 * 
	 * @param path
	 *            the name of the file
	 */
	public static boolean saveTrack(ObjectLocation[] locations, String path) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(path));
			for (int i = 0; i < locations.length; i++) {
				if (i != 0)
					bw.newLine();
				if (locations[i] != null)
					bw.write(locations[i].x + " " + locations[i].y);
				else
					bw.write("null");
			}
			bw.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * Opens a text file containing tracking coordinates.
	 */
	public static ObjectLocation[] openTrack(String path) {
		ArrayList<ObjectLocation> points = new ArrayList<ObjectLocation>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			int frame = 0;
			String line = br.readLine();
			while (line != null) {
				ObjectLocation p = ObjectLocation.read(line);

				if (p != null)
					p.frame = frame;

				// Filter other meta data
				if (p == null && !line.equals("null")) {
					line = br.readLine();
					continue;
				}

				points.add(p);
				frame++;
				line = br.readLine();

			}

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return points.toArray(new ObjectLocation[0]);
	}

	/**
	 * Saves a ZoneUnit list as a binary file.
	 */
	public static boolean saveZoneUnits(String path, List<ZoneUnit> zones) {
		return saveObject(path, zones);
	}

	/**
	 * Opens a binary file of the ZoneUnit list.
	 */
	public static List<ZoneUnit> openZoneUnits(String path) {
		return (List<ZoneUnit>) openObject(path);
	}

	/**
	 * Saves an AbstractTrackingParameter list as a binary file.
	 */
	public static boolean saveAbstractTrackingParameters(String path,
			List<AbstractTrackingParameter> parameterConfigs) {
		return saveObject(path, parameterConfigs);
	}

	/**
	 * Opens a binary file of the AbstractTrackingParameter list.
	 */
	public static List<AbstractTrackingParameter> openAbstractTrackingParameters(
			String path) {
		return (List<AbstractTrackingParameter>) openObject(path);
	}

	/**
	 * Saves a CoreTracker object as a binary file.
	 */
	public static boolean saveCoreTracking(String path, CoreTracker coreTracker) {
		return saveObject(path, coreTracker);
	}

	/**
	 * Opens a binary file of the CoreTracker object.
	 */
	public static CoreTracker openCoreTracking(String path) {
		return (CoreTracker) openObject(path);
	}

	/**
	 * Saves a TrackingTask object as a binary file.
	 */
	public static boolean saveTrackingTask(String path,
			TrackingTask trackingTask) {
		return saveObject(path, trackingTask);
	}

	/**
	 * Opens a binary file of the TrackingTask object.
	 */
	public static TrackingTask openTrackingTask(String path) {
		return (TrackingTask) openObject(path);
	}

	/**
	 * Saves a ZoneSetting list as a binary file.
	 */
	public static boolean saveAnalyzeSettings(String path,
			List<ZoneSetting> zoneSettings) {
		return saveObject(path, zoneSettings);
	}

	/**
	 * Opens a binary file of the ZoneSetting list.
	 */
	public static List<ZoneSetting> openAnalyzeSettings(String path) {
		return (List<ZoneSetting>) openObject(path);
	}

}
