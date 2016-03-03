package hu.elte.animaltracker.model.tracking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import ij.gui.PolygonRoi;
import ij.gui.Roi;

/**
 * This class contains the sequence of tracking points which are continuous in
 * time.
 * 
 */
public class TrackSequence implements List<ObjectLocation>, Serializable {

	private static final long serialVersionUID = -4044248007632288347L;
	protected List<ObjectLocation> locations;
	protected transient int[] xpf, ypf;

	/**
	 * Returns a list of TrackSequences located within an area.
	 * 
	 * @param locations
	 *            array of coordinates
	 * @param roi
	 *            Region Of Interest
	 */
	public static List<TrackSequence> getInnerSequences(
			ObjectLocation[] locations, Roi roi) {
		List<TrackSequence> pnt = new ArrayList<TrackSequence>();
		TrackSequence ps = null;

		for (ObjectLocation ol : locations) {
			if (ol != null && roi.contains(Math.round(ol.x), Math.round(ol.y))) {
				if (ps == null)
					ps = new TrackSequence();
				ps.add(ol);
			} else {
				if (ps != null)
					pnt.add(ps);
				ps = null;
			}
		}

		if (ps != null)
			pnt.add(ps);

		return pnt;
	}

	/**
	 * Returns a list of TrackSequences.
	 * 
	 * @param locations
	 *            array of coordinates
	 */
	public static List<TrackSequence> getConnectedSequences(
			ObjectLocation[] locations) {
		boolean first = false;
		TrackSequence sequence = new TrackSequence();
		List<TrackSequence> sequences = new ArrayList<TrackSequence>();

		for (int i = 0; i < locations.length; i++) {
			if (!first && locations[i] != null) {
				sequence = new TrackSequence();
				sequence.add(locations[i]);
				first = true;
			} else if (first && locations[i] != null) {
				sequence.add(locations[i]);
			} else if (first && locations[i] == null) {
				first = false;
				sequences.add(sequence);
			}
		}
		if (first) {
			sequences.add(sequence);
		}

		return sequences;
	}

	public TrackSequence() {
		locations = new ArrayList<ObjectLocation>();
	}

	/**
	 * Returns the first coordinate of the TrackSequence.
	 * 
	 */
	public ObjectLocation getFirstPoint() {
		return locations.get(0);
	}

	/**
	 * Returns the last coordinate of the TrackSequence.
	 * 
	 */
	public ObjectLocation getLastPoint() {
		return locations.get(locations.size() - 1);
	}

	/**
	 * Returns the frame index of the first coordinate.
	 * 
	 */
	public int getFirstFrameIndex() {
		return getFirstPoint().frame;
	}

	/**
	 * Returns the frame index of the last coordinate.
	 * 
	 */
	public int getLastFrameIndex() {
		return getLastPoint().frame;
	}

	/**
	 * Returns a polyline of the track.
	 * 
	 */
	public PolygonRoi getRoi() {
		return getRoi(locations.size());
	}

	/**
	 * Returns a polyline of the track until the given length.
	 */
	public PolygonRoi getRoi(int length) {
		if (xpf == null) {
			xpf = new int[locations.size()];
			ypf = new int[locations.size()];
			for (int i = 0; i < xpf.length; i++) {
				xpf[i] = Math.round(locations.get(i).x);
				ypf[i] = Math.round(locations.get(i).y);
			}
		}
		length = Math.max(Math.min(locations.size(), length), 1);
		return new PolygonRoi(xpf, ypf, length, Roi.POLYLINE);
	}

	/**
	 * Returns a polyline of the track until a given frame index.
	 * 
	 * @param index
	 *            frame index (zero-based).
	 */
	public PolygonRoi getRoiByIndex(int index) {
		return getRoi(index - getFirstFrameIndex());
	}

	@Override
	public boolean add(ObjectLocation e) {
		xpf = null;
		return locations.add(e);
	}

	@Override
	public void add(int index, ObjectLocation element) {
		xpf = null;
		locations.add(index, element);

	}

	@Override
	public boolean addAll(Collection<? extends ObjectLocation> c) {
		xpf = null;
		return locations.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends ObjectLocation> c) {
		xpf = null;
		return locations.addAll(index, c);
	}

	@Override
	public void clear() {
		xpf = null;
		locations.clear();
	}

	@Override
	public boolean contains(Object o) {
		return locations.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return locations.containsAll(c);
	}

	@Override
	public ObjectLocation get(int index) {
		return locations.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return locations.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return locations.isEmpty();
	}

	@Override
	public Iterator<ObjectLocation> iterator() {
		return locations.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return locations.lastIndexOf(o);
	}

	@Override
	public ListIterator<ObjectLocation> listIterator() {
		return locations.listIterator();
	}

	@Override
	public ListIterator<ObjectLocation> listIterator(int index) {
		return locations.listIterator(index);
	}

	@Override
	public boolean remove(Object o) {
		return locations.remove(o);
	}

	@Override
	public ObjectLocation remove(int index) {
		return locations.remove(index);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return locations.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return locations.retainAll(c);
	}

	@Override
	public ObjectLocation set(int index, ObjectLocation element) {
		return locations.set(index, element);
	}

	@Override
	public int size() {
		return locations.size();
	}

	@Override
	public List<ObjectLocation> subList(int fromIndex, int toIndex) {
		return locations.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return locations.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return locations.toArray(a);
	}

}
