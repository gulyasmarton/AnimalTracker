package hu.elte.animaltracker.model.tracking.thresholding;

import java.io.Serializable;
import java.util.Arrays;

import hu.elte.animaltracker.model.tracking.blobs.BaseBlob;
import ij.process.ByteProcessor;

/**
 * 
 * This class contains the binary pixel data of the image. True values mark
 * foreground pixels and false values mark background pixels.
 * 
 */
public class BooleanImage implements Serializable, Cloneable {

	private static final long serialVersionUID = -1250990485074837912L;
	int width;
	int height;
	boolean[] pixels;

	/**
	 * Creates a background image.
	 * 
	 * @param width
	 *            image width
	 * @param height
	 *            image height
	 */
	public BooleanImage(int width, int height) {
		super();
		this.width = width;
		this.height = height;
		this.pixels = new boolean[width * height];
	}

	/**
	 * Creates a BooleanImage from a binary pixel array.
	 * 
	 * @param width
	 *            image width
	 * @param height
	 *            image height
	 * @param pixels
	 *            binary pixel array
	 */
	public BooleanImage(int width, int height, boolean[] pixels) {
		super();
		this.width = width;
		this.height = height;
		this.pixels = pixels;
	}

	/**
	 * Returns the width of the image.
	 * 
	 * @return
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the height of the image.
	 * 
	 * @return
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Returns the binary pixel array.
	 * 
	 * @return
	 */
	public boolean[] getPixels() {
		return pixels;
	}

	/**
	 * Returns a 8-bit grayscale image. Foreground pixels are white and
	 * background pixels are black.
	 * 
	 * @return
	 */
	public ByteProcessor getByteProcessor() {
		byte[] pix = new byte[width * height];
		for (int i = 0; i < pixels.length; i++) {
			if (pixels[i])
				pix[i] = (byte) 255;
		}
		return new ByteProcessor(width, height, pix);
	}

	/**
	 * Sets to background covered area of blob.
	 * 
	 * @param blob
	 */
	public void remove(BaseBlob blob) {
		int xk = blob.getBound().x;
		int yk = blob.getBound().y;
		int w = blob.getBound().width;
		int h = blob.getBound().height;
		boolean[] mask = blob.getMask();
		for (int y = 0; y < h; y++)
			for (int x = 0; x < w; x++)
				if (mask[x + y * w])
					pixels[x + xk + (y + yk) * width] = false;

	}

	@Override
	protected BooleanImage clone() {
		return new BooleanImage(width, height, Arrays.copyOf(pixels,
				pixels.length));
	}

	/**
	 * Returns a deep copy of BooleanImage object.
	 * 
	 * @return
	 */
	public BooleanImage duplicate() {
		return clone();
	}

	/**
	 * Unary intersection operator.
	 * 
	 * @param imp
	 */
	public void and(BooleanImage imp) {
		if (pixels.length != imp.getPixels().length)
			return;

		for (int i = 0; i < imp.getPixels().length; i++)
			pixels[i] &= imp.getPixels()[i];
	}

	/**
	 * Unary union operator.
	 * 
	 * @param imp
	 */
	public void or(BooleanImage imp) {
		if (pixels.length != imp.getPixels().length)
			return;
		for (int i = 0; i < imp.getPixels().length; i++) {
			if (imp.getPixels()[i])
				pixels[i] = true;
		}
	}

	/**
	 * Unary subtraction operator.
	 * 
	 * @param imp
	 */
	public void not(BooleanImage imp) {
		if (pixels.length != imp.getPixels().length)
			return;

		for (int i = 0; i < imp.getPixels().length; i++)
			if (imp.getPixels()[i])
				pixels[i] = false;
	}

	/**
	 * Unary 'exclusive or' operator.
	 * 
	 * @param imp
	 */
	public void xor(BooleanImage imp) {
		if (pixels.length != imp.getPixels().length)
			return;
		for (int i = 0; i < imp.getPixels().length; i++) {
			pixels[i] ^= imp.getPixels()[i];
		}
	}

}
