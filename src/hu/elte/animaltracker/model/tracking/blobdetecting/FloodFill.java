package hu.elte.animaltracker.model.tracking.blobdetecting;

import hu.elte.animaltracker.model.tracking.blobs.BaseBlob;
import hu.elte.animaltracker.model.tracking.thresholding.BooleanImage;
import ij.gui.Roi;
import ij.process.ByteProcessor;
import ij.process.ColorProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import ij.process.ShortProcessor;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

/**
 * Finds the 8-connected areas on an image.
 * 
 */
public class FloodFill {
	boolean[] pix;
	int w;
	int h;
	List<BaseBlob> blobs;

	public FloodFill(BooleanImage image) {
		w = image.getWidth();
		h = image.getHeight();
		pix = image.getPixels();
	}

	private void FindBlob(boolean[] pix) {
		blobs = new ArrayList<BaseBlob>();
		for (int y = 0; y < h; y++)
			for (int x = 0; x < w; x++)
				if (pix[x + y * w])
					blobs.add(new BaseBlob(FindBlob(x, y)));
	}

	private HashSet<Point> FindBlob(int x, int y) {

		HashSet<Point> points = new HashSet<Point>();
		Stack<Point> stack = new Stack<Point>();
		Point p = new Point(x, y);
		stack.push(p);
		while (!stack.empty()) {
			p = stack.pop();
			points.add(p);
			pix[p.x + p.y * w] = false;
			for (int xk = -1; xk <= 1; xk++)
				for (int yk = -1; yk <= 1; yk++)
					if (pixCheck(p.x + xk, p.y + yk))
						stack.push(new Point(p.x + xk, p.y + yk));
		}

		return points;
	}

	private boolean pixCheck(int x, int y) {
		if (x < 0 || y < 0 || x >= w || y >= h)
			return false;
		return pix[x + y * w];
	}

	/**
	 * Returns the found blobs.
	 * 
	 * @return
	 */
	public List<BaseBlob> getBlobs() {
		if (blobs == null)
			FindBlob(pix);
		return blobs;
	}

}
