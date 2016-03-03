package hu.elte.animaltracker.model.tracking.thresholding;

import java.util.Arrays;

import hu.elte.animaltracker.controller.listeners.ColorThersholderCtrlListener;
import hu.elte.animaltracker.controller.tracking.ColorThersholderCtrl;
import hu.elte.animaltracker.model.CustomisableProcess;
import hu.elte.animaltracker.model.tracking.SourceClassifier;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;

/**
 * This class is based on the ColorThresholder of ImageJ.
 * 
 * @see ij.plugin.frame.ColorThresholder
 */
public class ColorThresholder extends AbstractThresholder {

	private static final long serialVersionUID = -7672549797793998184L;

	public enum ColorSpaces {
		HSB, RGB, Lab, YUV
	}

	protected boolean pass1, pass2, pass3;

	protected int[] th1, th2, th3;

	protected ColorSpaces colorspace;

	public ColorThresholder() {
		super();
	}

	/**
	 * 
	 * @param pass1
	 *            true: inverts the range of channel 1
	 * @param pass2
	 *            true: inverts the range of channel 2
	 * @param pass3
	 *            true: inverts the range of channel 3
	 * @param th1
	 *            int[]{minimum,maximum} thresholds of channel 1
	 * @param th2
	 *            int[]{minimum,maximum} thresholds of channel 2
	 * @param th3
	 *            int[]{minimum,maximum} thresholds of channel 3
	 * @param colorspace
	 */
	public ColorThresholder(boolean pass1, boolean pass2, boolean pass3,
			int[] th1, int[] th2, int[] th3, ColorSpaces colorspace) {
		super();
		this.pass1 = pass1;
		this.pass2 = pass2;
		this.pass3 = pass3;
		this.th1 = th1;
		this.th2 = th2;
		this.th3 = th3;
		this.colorspace = colorspace;
	}

	@Override
	public BooleanImage getBinaryImage(ImageProcessor ip) {
		if (th1 == null || !isSupported(ip))
			return new BooleanImage(ip.getWidth(), ip.getHeight());
		int width = ip.getWidth();
		int height = ip.getHeight();
		byte[] ch1, ch2, ch3;

		ch1 = new byte[ip.getWidth() * ip.getHeight()];
		ch2 = new byte[ip.getWidth() * ip.getHeight()];
		ch3 = new byte[ip.getWidth() * ip.getHeight()];

		ColorProcessor cp = (ColorProcessor) ip;

		switch (colorspace) {
		case RGB:
			cp.getRGB(ch1, ch2, ch3);
			break;
		case HSB:
			cp.getHSB(ch1, ch2, ch3);
			break;
		case Lab:
			getLab(cp, ch1, ch2, ch3);
			break;
		case YUV:
			getYUV(cp, ch1, ch2, ch3);
			break;
		default:
			break;
		}
		boolean[] pix = new boolean[width * height];

		for (int i = 0; i < ch1.length; i++) {
			boolean c1 = th1[0] <= (ch1[i] & 0xff) && (ch1[i] & 0xff) <= th1[1];
			if (pass1 ? !c1 : c1)
				continue;
			boolean c2 = th2[0] <= (ch2[i] & 0xff) && (ch2[i] & 0xff) <= th2[1];
			if (pass2 ? !c2 : c2)
				continue;
			boolean c3 = th3[0] <= (ch3[i] & 0xff) && (ch3[i] & 0xff) <= th3[1];
			if (pass3 ? !c3 : c3)
				continue;
			pix[i] = true;
		}

		pix = applyMask(pix);
		return new BooleanImage(width, height, pix);
	}

	/**
	 * Based on the ImageJ source code.
	 * 
	 * @param ip
	 * @param L
	 * @param a
	 * @param b
	 */
	protected void getLab(ImageProcessor ip, byte[] L, byte[] a, byte[] b) {
		// Returns Lab in 3 byte arrays.
		// http://www.brucelindbloom.com/index.html?WorkingSpaceInfo.html#Specifications
		// http://www.easyrgb.com/math.php?MATH=M7#text7
		int c, x, y, i = 0;
		double rf, gf, bf;
		double X, Y, Z, fX, fY, fZ;
		double La, aa, bb;
		double ot = 1 / 3.0, cont = 16 / 116.0;

		int width = ip.getWidth();
		int height = ip.getHeight();

		for (y = 0; y < height; y++) {
			for (x = 0; x < width; x++) {
				c = ip.getPixel(x, y);

				// RGB to XYZ
				rf = ((c & 0xff0000) >> 16) / 255.0; // R 0..1
				gf = ((c & 0x00ff00) >> 8) / 255.0; // G 0..1
				bf = (c & 0x0000ff) / 255.0; // B 0..1

				// gamma = 1.0?
				// white reference D65 PAL/SECAM
				X = 0.430587 * rf + 0.341545 * gf + 0.178336 * bf;
				Y = 0.222021 * rf + 0.706645 * gf + 0.0713342 * bf;
				Z = 0.0201837 * rf + 0.129551 * gf + 0.939234 * bf;

				// XYZ to Lab
				if (X > 0.008856)
					fX = Math.pow(X, ot);
				else
					fX = (7.78707 * X) + cont;
				// 7.7870689655172

				if (Y > 0.008856)
					fY = Math.pow(Y, ot);
				else
					fY = (7.78707 * Y) + cont;

				if (Z > 0.008856)
					fZ = Math.pow(Z, ot);
				else
					fZ = (7.78707 * Z) + cont;

				La = (116 * fY) - 16;
				aa = 500 * (fX - fY);
				bb = 200 * (fY - fZ);

				// rescale
				La = (int) (La * 2.55);
				aa = (int) (Math.floor((1.0625 * aa + 128) + 0.5));
				bb = (int) (Math.floor((1.0625 * bb + 128) + 0.5));

				// hsb = Color.RGBtoHSB(r, g, b, hsb);
				// a* and b* range from -120 to 120 in the 8 bit space

				// L[i] = (byte)((int)(La*2.55) & 0xff);
				// a[i] = (byte)((int)(Math.floor((1.0625 * aa + 128) + 0.5)) &
				// 0xff);
				// b[i] = (byte)((int)(Math.floor((1.0625 * bb + 128) + 0.5)) &
				// 0xff);

				L[i] = (byte) ((int) (La < 0 ? 0 : (La > 255 ? 255 : La)) & 0xff);
				a[i] = (byte) ((int) (aa < 0 ? 0 : (aa > 255 ? 255 : aa)) & 0xff);
				b[i] = (byte) ((int) (bb < 0 ? 0 : (bb > 255 ? 255 : bb)) & 0xff);
				i++;
			}
		}
	}

	/**
	 * Based on the ImageJ source code.
	 * 
	 * @param ip
	 * @param L
	 * @param a
	 * @param b
	 */
	protected void getYUV(ImageProcessor ip, byte[] Y, byte[] U, byte[] V) {
		// Returns YUV in 3 byte arrays.

		// RGB <--> YUV Conversion Formulas from
		// http://www.cse.msu.edu/~cbowen/docs/yuvtorgb.html
		// R = Y + (1.4075 * (V - 128));
		// G = Y - (0.3455 * (U - 128) - (0.7169 * (V - 128));
		// B = Y + (1.7790 * (U - 128);
		//
		// Y = R * .299 + G * .587 + B * .114;
		// U = R * -.169 + G * -.332 + B * .500 + 128.;
		// V = R * .500 + G * -.419 + B * -.0813 + 128.;

		int c, x, y, i = 0, r, g, b;
		double yf;

		int width = ip.getWidth();
		int height = ip.getHeight();

		for (y = 0; y < height; y++) {
			for (x = 0; x < width; x++) {
				c = ip.getPixel(x, y);

				r = ((c & 0xff0000) >> 16);// R
				g = ((c & 0x00ff00) >> 8);// G
				b = (c & 0x0000ff); // B

				// Kai's plugin
				yf = (0.299 * r + 0.587 * g + 0.114 * b);
				Y[i] = (byte) ((int) Math.floor(yf + 0.5));
				U[i] = (byte) (128 + (int) Math.floor((0.493 * (b - yf)) + 0.5));
				V[i] = (byte) (128 + (int) Math.floor((0.877 * (r - yf)) + 0.5));

				// Y[i] = (byte) (Math.floor( 0.299 * r + 0.587 * g + 0.114 *
				// b)+.5);
				// U[i] = (byte) (Math.floor(-0.169 * r - 0.332 * g + 0.500 * b
				// + 128.0)+.5);
				// V[i] = (byte) (Math.floor( 0.500 * r - 0.419 * g - 0.0813 * b
				// + 128.0)+.5);

				i++;
			}
		}
	}

	@Override
	public CustomisableProcess getNewInstance() {
		ColorThresholder thresholder = new ColorThresholder(pass1, pass2,
				pass3, th1, th2, th3, colorspace);

		if (mask != null)
			thresholder.mask = Arrays.copyOf(mask, mask.length);
		if (th1 != null)
			thresholder.th1 = Arrays.copyOf(th1, th1.length);
		if (th2 != null)
			thresholder.th2 = Arrays.copyOf(th2, th2.length);
		if (th3 != null)
			thresholder.th3 = Arrays.copyOf(th3, mask.length);

		return thresholder;
	}

	@Override
	public String getName() {
		return "Color Thresholder";
	}

	@Override
	public void showGUI() {
		ColorThersholderCtrl thersholderCtrl = new ColorThersholderCtrl();
		thersholderCtrl
				.addColorThersholderCtrlListener(new ColorThersholderCtrlListener() {

					@Override
					public void returnThresholds(ColorSpaces colorSpacex,
							int[] thr1x, int[] thr2x, int[] thr3x,
							boolean pass1x, boolean pass2x, boolean pass3x) {
						th1 = thr1x;
						th2 = thr2x;
						th3 = thr3x;
						pass1 = pass1x;
						pass2 = pass2x;
						pass3 = pass3x;
						colorspace = colorSpacex;

					}
				});

	}

	@Override
	public int getSupportedImageType() {
		return SourceClassifier.COLOR_IMAGE;
	}

	@Override
	public String toString() {
		return getName();
	}

}
