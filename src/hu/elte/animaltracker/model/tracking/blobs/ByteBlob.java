package hu.elte.animaltracker.model.tracking.blobs;

import ij.process.ImageProcessor;

import java.awt.Point;
import java.io.Serializable;
import java.util.HashSet;

/**
 * This class can store the 8-bit pixel information.
 * 
 */
public class ByteBlob extends BaseBlob implements Serializable {

	private static final long serialVersionUID = -856545582026501048L;

	protected byte[] pixels;

	protected void setImageInfo() {
		mean = 0d;
		int size = 0;

		for (int y = 0; y < bound.height; y++)
			for (int x = 0; x < bound.width; x++)
				if (mask[x + y * bound.width]) {
					mean += pixels[x + y * bound.width];
					size++;
				}

		if (size == 0) {
			mean = 0d;
			sd = 0d;
		}

		mean /= size;
		sd = 0d;
		for (int y = 0; y < bound.height; y++)
			for (int x = 0; x < bound.width; x++)
				if (mask[x + y * bound.width])
					sd += (pixels[x + y * bound.width] - mean)
							* (pixels[x + y * bound.width] - mean);
		sd = Math.sqrt(sd / size);
	}

	public ByteBlob(HashSet<Point> points) {
		super(points);
	}

	public ByteBlob(BaseBlob blob, ImageProcessor ip) {
		super(blob);
		byte[] img = (byte[]) ip.getPixels();
		pixels = new byte[bound.width * bound.height];

		for (int y = 0; y < bound.height; y++)
			for (int x = 0; x < bound.width; x++)
				pixels[x + y * bound.width] = img[x + bound.x + (y + bound.y)
						* ip.getWidth()];
	}

	public Double getMean() {
		if (mean == null)
			setImageInfo();
		return mean;
	}

	public Double getSd() {
		if (sd == null)
			setImageInfo();
		return sd;
	}
}
