package hu.elte.animaltracker.model.tracking.thresholding;

import java.util.Arrays;

import hu.elte.animaltracker.controller.listeners.ThersholderCtrlListener;
import hu.elte.animaltracker.controller.tracking.ThresholderCtrl;
import hu.elte.animaltracker.model.CustomisableProcess;
import hu.elte.animaltracker.model.tracking.SourceClassifier;
import ij.process.ByteProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import ij.process.ShortProcessor;

/**
 * This class is based on the grayscale thresholder of ImageJ. It works with 8-,
 * 16- and 32-bit grayscale images.
 * 
 * 
 */
public class GrayscaleThresholder extends AbstractThresholder {
	private static final long serialVersionUID = -1444884985105318537L;
	protected Double min, max;

	public GrayscaleThresholder() {
		super();
	}

	public GrayscaleThresholder(Double min, Double max) {
		super();
		this.min = min;
		this.max = max;
	}

	@Override
	public BooleanImage getBinaryImage(ImageProcessor ip) {
		if (min == null || max == null || !isSupported(ip))
			return new BooleanImage(ip.getWidth(), ip.getHeight());

		boolean[] pix;

		if (ip instanceof ByteProcessor) {
			pix = getBinaryImage((ByteProcessor) ip, min.intValue(),
					max.intValue());
		} else if (ip instanceof ShortProcessor) {
			pix = getBinaryImage((ShortProcessor) ip, min.shortValue(),
					max.shortValue());
		} else if (ip instanceof FloatProcessor) {
			pix = getBinaryImage((FloatProcessor) ip, min.floatValue(),
					max.floatValue());
		} else {
			return new BooleanImage(ip.getWidth(), ip.getHeight());
		}

		pix = applyMask(pix);
		return new BooleanImage(ip.getWidth(), ip.getHeight(), pix);
	}

	protected boolean[] getBinaryImage(FloatProcessor ip, float min, float max) {
		float[] inpix = (float[]) ip.getPixels();
		boolean[] pix = new boolean[inpix.length];

		for (int i = 0; i < inpix.length; i++) {
			if ((inpix[i]) >= min && (inpix[i]) <= max)
				pix[i] = true;
		}
		return pix;
	}

	protected boolean[] getBinaryImage(ShortProcessor ip, short min, short max) {
		short[] inpix = (short[]) ip.getPixels();
		boolean[] pix = new boolean[inpix.length];

		for (int i = 0; i < inpix.length; i++) {
			if ((inpix[i]) >= min && (inpix[i]) <= max)
				pix[i] = true;
		}
		return pix;
	}

	protected boolean[] getBinaryImage(ByteProcessor ip, int min, int max) {
		byte[] inpix = (byte[]) ip.getPixels();
		boolean[] pix = new boolean[inpix.length];

		for (int i = 0; i < inpix.length; i++) {
			if ((inpix[i] & 0xff) >= min && (inpix[i] & 0xff) <= max)
				pix[i] = true;
		}
		return pix;
	}

	@Override
	public CustomisableProcess getNewInstance() {
		GrayscaleThresholder thresholder = new GrayscaleThresholder(min, max);

		if (mask != null)
			thresholder.mask = Arrays.copyOf(mask, mask.length);

		return thresholder;
	}

	@Override
	public String getName() {
		return "Grayscale Thresholder";

	}

	@Override
	public void showGUI() {
		ThresholderCtrl thresholderCtrl = new ThresholderCtrl();
		thresholderCtrl
				.addThersholderCtrlListener(new ThersholderCtrlListener() {

					@Override
					public void returnThresholds(double min2, double max2) {
						min = min2;
						max = max2;
					}
				});
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public int getSupportedImageType() {
		return SourceClassifier.BYTE_IMAGE | SourceClassifier.SHORT_IMAGE
				| SourceClassifier.FLOAT_IMAGE;
	}

}
