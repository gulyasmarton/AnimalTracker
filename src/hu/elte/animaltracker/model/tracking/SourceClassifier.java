package hu.elte.animaltracker.model.tracking;

import ij.ImagePlus;
import ij.process.ByteProcessor;
import ij.process.ColorProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import ij.process.ShortProcessor;

/**
 * This class decides whether the source image suits one of flags.
 * 
 * 
 */
public class SourceClassifier {
	/**
	 * Supports 8-bit grayscale images.
	 */
	public final static int BYTE_IMAGE = 0x0001;
	/**
	 * Supports 16-bit grayscale images.
	 */
	public final static int SHORT_IMAGE = 0x0002;
	/**
	 * Supports 32-bit grayscale images.
	 */
	public final static int FLOAT_IMAGE = 0x0004;
	/**
	 * Supports 32-bit color images.
	 */
	public final static int COLOR_IMAGE = 0x0008;

	/**
	 * Returns 'true' if the type of the image suits one of flags.
	 * 
	 * @param imp
	 *            source image
	 * @param suppFlag
	 *            flags
	 * @return
	 */
	public static boolean isSupported(ImagePlus imp, int suppFlag) {
		return isSupported(imp.getProcessor(), suppFlag);
	}

	/**
	 * Returns 'true' if the type of the image suits one of flags.
	 * 
	 * @param ip
	 *            source image
	 * @param suppFlag
	 *            flags
	 * @return
	 */
	public static boolean isSupported(ImageProcessor ip, int suppFlag) {

		if (ip instanceof ByteProcessor
				&& (suppFlag & BYTE_IMAGE) == BYTE_IMAGE)
			return true;
		if (ip instanceof ShortProcessor
				&& (suppFlag & SHORT_IMAGE) == SHORT_IMAGE)
			return true;
		if (ip instanceof FloatProcessor
				&& (suppFlag & FLOAT_IMAGE) == FLOAT_IMAGE)
			return true;
		if (ip instanceof ColorProcessor
				&& (suppFlag & COLOR_IMAGE) == COLOR_IMAGE)
			return true;
		return false;
	}
}
