package hu.elte.animaltracker.model.tracking.filtering;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hu.elte.animaltracker.controller.tracking.BackgroundSubtractorController;
import hu.elte.animaltracker.model.tracking.SourceClassifier;
import hu.elte.animaltracker.view.tracking.BackgroundSubtractorView;
import ij.process.ByteProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import ij.process.ShortProcessor;

/**
 * This class removes the image background from the grayscale image. Before
 * using this filter you have to select a few frames from the video recording
 * where the observed object is in a different location. Based on these frames,
 * the general background is calculated and removed from the input image.
 * 
 */
public class BackgroundSubtractor extends AbstractFilter {
	private static final long serialVersionUID = -3903349009111920252L;

	protected Integer width, height;
	protected List<AbstractFilter> otherFilters;
	protected byte[] filterByte;
	protected short[] filterShort;
	protected float[] filterFloat;

	protected transient ImageProcessor ip;
	protected transient List<ImageProcessor> frames = new ArrayList<ImageProcessor>();

	/**
	 * Default constructor.
	 * 
	 * @param otherFilters
	 *            List of all filters which are used before this filter.
	 */
	public BackgroundSubtractor(List<AbstractFilter> otherFilters) {
		super();
		this.otherFilters = otherFilters;
	}

	private void readObject(ObjectInputStream ois)
			throws ClassNotFoundException, IOException {
		ois.defaultReadObject();

		if (filterByte != null)
			ip = new ByteProcessor(width, height, filterByte);
		else if (filterShort != null)
			ip = new ShortProcessor(width, height, filterShort, null);
		else if (filterFloat != null)
			ip = new FloatProcessor(width, height, filterFloat);
	}

	/**
	 * Adds a new frame to the general background.
	 * 
	 * @param ip
	 */
	public void addFrame(ImageProcessor ip) {
		if (width == null) {
			width = ip.getWidth();
			height = ip.getHeight();
		}
		if (width != ip.getWidth())
			return;

		if (frames.size() > 0
				&& !frames.get(0).getClass().equals(ip.getClass()))
			return;

		frames.add(ip);

		generateFilter();
	}

	/**
	 * Generates the general background from the added frames.
	 */
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

		// ByteProcessor
		if (list.get(0) instanceof ByteProcessor) {
			byte[] col = new byte[list.size()];
			filterByte = new byte[width * height];
			for (int y = 0; y < height; y++)
				for (int x = 0; x < width; x++) {
					for (int i = 0; i < col.length; i++) {
						byte[] pix = (byte[]) list.get(i).getPixels();
						col[i] = pix[x + y * width];
					}
					Arrays.sort(col);
					filterByte[x + y * width] = col[col.length / 2];
				}
			ip = new ByteProcessor(width, height, filterByte);
		}
		// ShortProcessor
		else if (list.get(0) instanceof ShortProcessor) {
			short[] col = new short[list.size()];
			filterShort = new short[width * height];
			for (int y = 0; y < height; y++)
				for (int x = 0; x < width; x++) {
					for (int i = 0; i < col.length; i++) {
						short[] pix = (short[]) list.get(i).getPixels();
						col[i] = pix[x + y * width];
					}
					Arrays.sort(col);
					filterShort[x + y * width] = col[col.length / 2];
				}
			ip = new ShortProcessor(width, height, filterShort, null);
		}
		// FloatProcessor
		else if (list.get(0) instanceof FloatProcessor) {
			float[] col = new float[list.size()];
			filterFloat = new float[width * height];
			for (int y = 0; y < height; y++)
				for (int x = 0; x < width; x++) {
					for (int i = 0; i < col.length; i++) {
						float[] pix = (float[]) list.get(i).getPixels();
						col[i] = pix[x + y * width];
					}
					Arrays.sort(col);
					filterFloat[x + y * width] = col[col.length / 2];
				}
			ip = new FloatProcessor(width, height, filterFloat);
		}

	}

	/**
	 * Removes the general background.
	 */
	public void clearFilters() {
		if (frames != null)
			frames.clear();
		width = null;
		ip = null;
		filterByte = null;
		filterShort = null;
		filterFloat = null;
	}

	@Override
	public ImageProcessor processImage(ImageProcessor ip) {
		if (!isSupported(ip) && getFilterImage() == null)
			return ip;

		if (ip.getWidth() != width || ip.getHeight() != height)
			return ip;

		// ByteProcessor
		if (ip instanceof ByteProcessor) {
			byte[] pixel = (byte[]) ip.getPixels();
			for (int i = 0; i < pixel.length; i++) {
				int p = Math.abs((pixel[i] & 0xFF) - (filterByte[i] & 0xFF));
				pixel[i] = (byte) p;
			}
			return new ByteProcessor(width, height, pixel);
		}
		// ShortProcessor
		else if (ip instanceof ShortProcessor) {
			short[] pixel = (short[]) ip.getPixels();
			for (int i = 0; i < pixel.length; i++) {
				pixel[i] = (short) Math.abs((pixel[i]) - (filterShort[i]));
			}
			return new ShortProcessor(width, height, pixel, null);
		}
		// FloatProcessor
		else {
			float[] pixel = (float[]) ip.getPixels();
			for (int i = 0; i < pixel.length; i++) {
				pixel[i] = Math.abs((pixel[i]) - (filterFloat[i]));
			}
			return new FloatProcessor(width, height, pixel);
		}

	}

	/**
	 * Returns the general background image.
	 * 
	 * @return
	 */
	public ImageProcessor getFilterImage() {
		return ip;
	}

	@Override
	public int getSupportedImageType() {
		if (filterByte != null)
			return SourceClassifier.BYTE_IMAGE;
		if (filterShort != null)
			return SourceClassifier.SHORT_IMAGE;
		if (filterFloat != null)
			return SourceClassifier.FLOAT_IMAGE;
		else
			return SourceClassifier.BYTE_IMAGE | SourceClassifier.SHORT_IMAGE
					| SourceClassifier.FLOAT_IMAGE;
	}

	@Override
	public String getName() {
		return "Background Subtractor";
	}

	@Override
	public void showGUI() {
		BackgroundSubtractorController controller = new BackgroundSubtractorController(
				this);
		BackgroundSubtractorView view = new BackgroundSubtractorView(controller);
		view.setVisible(true);
	}

	@Override
	public AbstractFilter getNewInstance() {
		return new BackgroundSubtractor(otherFilters);
	}
}
