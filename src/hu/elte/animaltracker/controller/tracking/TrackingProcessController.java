package hu.elte.animaltracker.controller.tracking;

import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ImageProcessor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

import hu.elte.animaltracker.controller.listeners.TrackingProcessListener;
import hu.elte.animaltracker.model.tracking.CoreTracker;
import hu.elte.animaltracker.model.tracking.ObjectLocation;
import hu.elte.animaltracker.model.tracking.TrackingTask;

public class TrackingProcessController implements PropertyChangeListener {

	int idx;
	List<TrackingProcessListener> listeners = new ArrayList<TrackingProcessListener>();

	class Task extends SwingWorker<Void, Void> {
		TrackingTask trackingTask;
		CoreTracker coreTracker;
		ImagePlus imp;
		int lastFrame;
		int firstFrame;

		public Task(ImagePlus imp, CoreTracker coreTracker) {
			super();
			this.coreTracker = coreTracker;
			this.imp = imp;
			lastFrame = imp.getStackSize();
		}

		public Task(TrackingTask trackingTask) {
			this(trackingTask.getImage(), trackingTask.getCoreTracker());
			this.trackingTask = trackingTask;
			firstFrame = trackingTask.getFirstFrame();
			lastFrame = trackingTask.getLastFrame();
		}

		@Override
		public Void doInBackground() {

			setProgress(0);

			ImageStack st = imp.getStack();

			ObjectLocation[] point = new ObjectLocation[st.getSize()];

			if (lastFrame > st.getSize())
				lastFrame = st.getSize();

			for (int i = firstFrame; i <= lastFrame; i++) {
				idx = i;
				if (isCancelled()) {
					return null;
				}
				ImageProcessor ip = st.getProcessor(i);
				point[i - 1] = coreTracker.getBlobPosition(ip);

				if (point[i - 1] != null)
					point[i - 1].frame = i - 1;

				float idx = (i - firstFrame) * 100 / (lastFrame - firstFrame);
				setProgress((int) idx);
			}
			if (trackingTask != null)
				trackingTask.setPoint(point);
			return null;
		}

		@Override
		public void done() {
			// progressMonitor.setProgress(0);
		}
	}

	private ProgressMonitor progressMonitor;
	private Task task;

	public void addTrackingProcessListener(TrackingProcessListener listener) {
		listeners.add(listener);
	}

	public void removeTrackingProcessListener(TrackingProcessListener listener) {
		listeners.remove(listener);
	}

	protected void firedtrackingCanceled(int frame) {
		for (TrackingProcessListener listener : listeners) {
			listener.trackingCanceled(frame);
		}
	}

	protected void firedtrackingFinished() {
		for (TrackingProcessListener listener : listeners) {
			listener.trackingFinished();
		}
	}

	public TrackingProcessController(TrackingTask trackingTask) {
		progressMonitor = new ProgressMonitor(null, "Tracking - "
				+ trackingTask.getImage().getTitle(), "", 0, 100);
		progressMonitor.setProgress(0);
		task = new Task(trackingTask);
		task.addPropertyChangeListener(this);
		task.execute();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			progressMonitor.setProgress(progress);
			String message = String.format("Completed %d%%.\n", progress);
			progressMonitor.setNote(message);

			if (progressMonitor.isCanceled() || task.isDone()) {

				if (progressMonitor.isCanceled()) {
					task.cancel(true);
					firedtrackingCanceled(idx);
				} else {
					firedtrackingFinished();
				}

			}
		}

	}
}
