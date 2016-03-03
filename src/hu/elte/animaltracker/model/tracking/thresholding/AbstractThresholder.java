package hu.elte.animaltracker.model.tracking.thresholding;

import java.awt.Rectangle;
import java.io.Serializable;

import hu.elte.animaltracker.model.CustomisableProcess;
import hu.elte.animaltracker.model.tracking.SourceClassifiable;
import hu.elte.animaltracker.model.tracking.SourceClassifier;
import hu.elte.animaltracker.model.zones.ZoneUnit;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.process.ByteProcessor;
import ij.process.ImageProcessor;

/**
 * Derived classes of the AbstractThresholder carry out binarization.
 * 
 * 
 */
public abstract class AbstractThresholder implements Serializable,
		CustomisableProcess, SourceClassifiable {
	private static final long serialVersionUID = 1881337612821345274L;

	protected boolean[] mask;

	public boolean isSupported(ImagePlus imp) {
		return isSupported(imp.getProcessor());
	}

	public boolean isSupported(ImageProcessor ip) {
		return SourceClassifier.isSupported(ip, getSupportedImageType());
	}

	public abstract BooleanImage getBinaryImage(ImageProcessor ip);

	public BooleanImage getBinaryImage(ImagePlus imp) {
		return getBinaryImage(imp.getProcessor());
	}

	/**
	 * Sets any pixel which lies outside of the mask to 'false'.
	 * 
	 * @param pixel
	 * @return
	 */
	protected boolean[] applyMask(boolean[] pixel) {
		if (mask == null || mask.length != pixel.length)
			return pixel;

		for (int i = 0; i < mask.length; i++) {
			if (!mask[i] && pixel[i])
				pixel[i] = false;
		}

		return pixel;
	}

	/**
	 * Sets a mask. Pixels that lie outside of this mask automatically become
	 * part of the background.
	 * 
	 * @param mask
	 * @param width
	 *            Image width
	 * @param height
	 *            Image height
	 */
	public void setMask(ZoneUnit mask, int width, int height) {
		if (mask == null) {
			this.mask = null;
			return;
		}
		this.mask = new boolean[width * height];
		Rectangle rec = mask.getBounds();
		ByteProcessor bp = (ByteProcessor) mask.getRoi().getMask();
		if (bp == null) {
			for (int y = rec.y; y < rec.y + rec.height; y++)
				for (int x = rec.x; x < rec.x + rec.width; x++)
					if (x >= 0 && y >= 0 && x < width && y < height)
						this.mask[x + y * width] = true;
		} else {
			byte[] m = (byte[]) bp.getPixels();
			for (int y = rec.y; y < rec.y + rec.height; y++)
				for (int x = rec.x; x < rec.x + rec.width; x++)
					if (x >= 0 && y >= 0 && x < width && y < height
							&& m[x - rec.x + (y - rec.y) * rec.width] != 0)
						this.mask[x + y * width] = true;
		}

	}
}
