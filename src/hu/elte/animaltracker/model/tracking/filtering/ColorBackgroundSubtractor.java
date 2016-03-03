package hu.elte.animaltracker.model.tracking.filtering;

import hu.elte.animaltracker.controller.tracking.ColorBackgroundSubtractorController;
import hu.elte.animaltracker.view.tracking.ColorBackgroundSubtractorView;

import ij.process.ColorProcessor;
import ij.process.ImageProcessor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The color version of the general background. You have to set a threshold
 * between 0 and 1 (0%-100%) and a general background color. If the difference
 * between a background pixel (in a specific position) and a pixel in the
 * current frame (in the same position) is less than the threshold value, the
 * pixel of the current frame will be changed to the adjusted background color.
 * The algorithm performs this operation pixel by pixel.
 * 
 * @see hu.elte.animaltracker.model.tracking.filtering.BackgroundSubtractor
 ** 
 */
public class ColorBackgroundSubtractor extends BackgroundSubtractor {

	private static final long serialVersionUID = -4458895639165017640L;

	protected int[] filterInteger;

	protected double threshold = 0.1d;
	protected int background = -16777216;

	public ColorBackgroundSubtractor(List<AbstractFilter> otherFilters) {
		super(otherFilters);

	}

	private void readObject(ObjectInputStream ois)
			throws ClassNotFoundException, IOException {
		ois.defaultReadObject();
		if (filterInteger != null)
			ip = new ColorProcessor(width, height, filterInteger);

	}

	@Override
	public void addFrame(ImageProcessor ip) {
		if (ip instanceof ColorProcessor)
			super.addFrame(ip);
	}

	@Override
	protected void generateFilter() {
		if (frames.size() == 0)
			return;

		List<ImageProcessor> list = new ArrayList<ImageProcessor>(frames.size());

		for (ImageProcessor ip : frames)
			list.add(ip.duplicate());

		for (AbstractFilter processor : otherFilters) {
			if (processor.equals(this))
				break;
			for (int i = 0; i < list.size(); i++) {
				ImageProcessor ip = list.get(i);
				ip = processor.processImage(ip);
				list.set(i, ip);
			}
		}
		filterInteger = new int[width * height];
		int size = list.size();
		int[] r = new int[size];
		int[] g = new int[size];
		int[] b = new int[size];

		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++) {
				for (int i = 0; i < size; i++) {
					int[] pix = (int[]) list.get(i).getPixels();

					int c = pix[x + y * width];
					r[i] = (c & 0xff0000) >> 16;
					g[i] = (c & 0xff00) >> 8;
					b[i] = c & 0xff;
				}

				Arrays.sort(r);
				Arrays.sort(g);
				Arrays.sort(b);

				filterInteger[x + y * width] = 0xff000000
						| ((r[size / 2] & 0xff) << 16)
						| ((g[size / 2] & 0xff) << 8) | b[size / 2] & 0xff;
			}
		ip = new ColorProcessor(width, height, filterInteger);

	}

	@Override
	public void clearFilters() {
		super.clearFilters();
		filterInteger = null;
	}

	@Override
	public ImageProcessor processImage(ImageProcessor ip) {
		if (!isSupported(ip) && getFilterImage() == null)
			return ip;

		if (!(ip instanceof ColorProcessor) || ip.getWidth() != width
				|| ip.getHeight() != height)
			return ip;

		int[] pixel = (int[]) ip.getPixels();

		for (int i = 0; i < pixel.length; i++) {

			float org = convertRGBtoFloat(filterInteger[i]);

			float dif = Math.abs(convertRGBtoFloat(pixel[i]) - org);

			double ratio = dif / org;

			if (ratio < threshold)
				pixel[i] = background;

		}
		return new ColorProcessor(width, height, pixel);

	}

	protected float convertRGBtoFloat(int pix) {
		double w = 1d / 3d;
		int r = (pix & 0xff0000) >> 16;
		int g = (pix & 0xff00) >> 8;
		int b = pix & 0xff;
		return (float) (r * w + g * w + b * w);
	}

	/**
	 * Returns the threshold value.
	 * 
	 * @return
	 */
	public double getThreshold() {
		return threshold;
	}

	/**
	 * Sets the threshold value.
	 * 
	 * @param threshold
	 *            between 0-1.
	 */
	public void setThreshold(double threshold) {
		this.threshold = Math.max(Math.min(1, threshold), 0);
	}

	/**
	 * Returns the RGB value of the background.
	 * 
	 * @return You can convert it to java.awt.Color(int rgb)
	 * @see java.awt.Color
	 */
	public int getBackground() {
		return background;
	}

	/**
	 * Sets the color background to the specified combined RGB value.
	 * 
	 * @see java.awt.Color#Color(int)
	 * @param background
	 */
	public void setBackground(int background) {
		this.background = background;
	}

	@Override
	public String getName() {
		return "Color Background Subtractor";
	}

	@Override
	public void showGUI() {
		ColorBackgroundSubtractorController controller = new ColorBackgroundSubtractorController(
				this);
		ColorBackgroundSubtractorView view = new ColorBackgroundSubtractorView(
				controller);
		view.setVisible(true);
	}

	@Override
	public AbstractFilter getNewInstance() {
		ColorBackgroundSubtractor c = new ColorBackgroundSubtractor(
				otherFilters);
		c.setBackground(getBackground());
		c.setThreshold(threshold);
		c.frames = frames;
		c.ip = ip;
		return c;
	}

}
