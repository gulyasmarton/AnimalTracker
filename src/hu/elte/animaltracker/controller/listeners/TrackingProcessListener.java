package hu.elte.animaltracker.controller.listeners;

public interface TrackingProcessListener {
	void trackingCanceled(int frame);
	void trackingFinished();
}
