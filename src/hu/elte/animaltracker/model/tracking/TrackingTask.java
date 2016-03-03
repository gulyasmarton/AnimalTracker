package hu.elte.animaltracker.model.tracking;

import hu.elte.animaltracker.model.zones.ZoneUnit;
import ij.IJ;
import ij.ImagePlus;
import ij.io.FileInfo;
import ij.io.TiffDecoder;
import ij.plugin.AVI_Reader;
import ij.plugin.FileInfoVirtualStack;
import ij.plugin.FolderOpener;
import ij.process.ColorProcessor;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * This is a configuration class which contains any necessary information about
 * a 2D single target tracking process. You can save and load it by using the
 * Serializable interface.
 * 
 * This class is useful if when analyzing a large number of configured videos
 * without manual interaction. (Batch mode)
 * 
 * 
 */
public class TrackingTask implements Serializable {
	private static final long serialVersionUID = -4659181866943835308L;
	protected transient ImagePlus imp;
	protected CoreTracker coreTracker;
	protected ObjectLocation[] point;
	protected int firstFrame, lastFrame;
	protected ZoneUnit trackingArea;

	public TrackingTask(ImagePlus imp, CoreTracker coreTracker) {
		super();
		this.imp = imp;
		this.coreTracker = coreTracker;
		lastFrame = imp.getStackSize();
	}

	private void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
		String path = imp.getOriginalFileInfo().directory + File.separator
				+ imp.getOriginalFileInfo().fileName;

		oos.writeObject(path);

		// is virtual?
		oos.writeBoolean(imp.getStack().isVirtual());

		// is color?
		oos.writeBoolean(imp.getProcessor() instanceof ColorProcessor);
	}

	private void readObject(ObjectInputStream ois)
			throws ClassNotFoundException, IOException {
		ois.defaultReadObject();
		String path = (String) ois.readObject();
		File file = new File(path);
		boolean isVirtual = ois.readBoolean();
		boolean isColor = ois.readBoolean();

		FileInfo info = new FileInfo();
		info.directory = file.getParent();
		info.fileName = file.getName();

		if (file.isDirectory()) {
			FolderOpener opener = new FolderOpener();
			opener.openAsVirtualStack(isVirtual);
			imp = opener.openFolder(file.getPath());
			imp.setFileInfo(info);
			return;
		} else if (file.getName().contains(".avi")) {
			AVI_Reader avi = new AVI_Reader();
			imp = new ImagePlus(file.getName(), avi.makeStack(file.getPath(),
					1, -1, isVirtual, !isColor, false));
			imp.setFileInfo(info);
			return;
		} else if (file.getName().contains(".tif") && isVirtual) {

			TiffDecoder td = new TiffDecoder(file.getParent(), file.getName());
			FileInfo[] infos;
			IJ.showStatus("Decoding TIFF header...");
			try {
				infos = td.getTiffInfo();
			} catch (IOException e) {
				String msg = e.getMessage();
				if (msg == null || msg.equals(""))
					msg = "" + e;
				IJ.log("TiffDecoder: " + msg);
				return;
			}
			FileInfoVirtualStack vs = new FileInfoVirtualStack(infos[0], false);
			imp = new ImagePlus(file.getName(), vs);
			imp.setFileInfo(info);
			return;
		}
		imp = new ImagePlus(file.getPath());
		imp.setFileInfo(info);
	}

	/**
	 * Returns the number of the starting frame.
	 * 
	 * @return one-based frame index.
	 */
	public int getFirstFrame() {
		return firstFrame;
	}

	/**
	 * Sets the starting frame.
	 * 
	 * @param firstFrame
	 *            one-based frame index.
	 */
	public void setFirstFrame(int firstFrame) {
		this.firstFrame = firstFrame;
	}

	/**
	 * Returns the number of the end frame.
	 * 
	 * @return one-based frame index.
	 */
	public int getLastFrame() {
		return lastFrame;
	}

	/**
	 * Sets the end frame.
	 * 
	 * @param lastFrame
	 *            one-based frame index.
	 */
	public void setLastFrame(int lastFrame) {
		this.lastFrame = lastFrame;
	}

	/**
	 * Returns the array of ObjectLocations.
	 * 
	 * @return
	 */
	public ObjectLocation[] getPoint() {
		return point;
	}

	/**
	 * Sets the array of ObjectLocations.
	 * 
	 * @param point
	 */
	public void setPoint(ObjectLocation[] point) {
		this.point = point;
	}

	/**
	 * Returns the video recording.
	 * 
	 * @return
	 */
	public ImagePlus getImage() {
		return imp;
	}

	/**
	 * Returns the CoreTracker object.
	 * 
	 * @return
	 */
	public CoreTracker getCoreTracker() {
		return coreTracker;
	}

	/**
	 * Returns the tracking area.
	 * 
	 * @return
	 */
	public ZoneUnit getTrackingArea() {
		return trackingArea;
	}

	/**
	 * Sets the tracking area.
	 * 
	 * @param trackingArea
	 */
	public void setTrackingArea(ZoneUnit trackingArea) {
		coreTracker.getThresholder().setMask(trackingArea, imp.getWidth(),
				imp.getHeight());
		this.trackingArea = trackingArea;
	}

	/**
	 * Sets the CoreTracker object.
	 * 
	 * @param coreTracker
	 */
	public void setCoreTracker(CoreTracker coreTracker) {
		this.coreTracker = coreTracker;
	}
}
