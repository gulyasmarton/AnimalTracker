package hu.elte.animaltracker.examples.watermaze;

import hu.elte.animaltracker.model.TrackingIO;
import hu.elte.animaltracker.model.analyzing.AbstractTrackingParameter;
import hu.elte.animaltracker.model.tracking.TrackingTask;

import java.awt.Rectangle;
import java.io.Serializable;
import java.util.List;

public class WaterMazeConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8518017105254630437L;
	protected Rectangle pool;
	protected Rectangle platform;
	protected double innerRatio;
	protected TrackingTask task;
	protected List<AbstractTrackingParameter> parameters;

	public WaterMazeConfig(Rectangle pool, Rectangle platform,
			double innerRatio, TrackingTask task,
			List<AbstractTrackingParameter> parameters) {
		super();
		this.pool = pool;
		this.platform = platform;
		this.innerRatio = innerRatio;
		this.task = task;
		this.parameters = parameters;
	}

	public Rectangle getPool() {
		return pool;
	}

	public Rectangle getPlatform() {
		return platform;
	}

	public double getInnerRatio() {
		return innerRatio;
	}

	public TrackingTask getTask() {
		return task;
	}

	public boolean save(String path) {
		return TrackingIO.saveObject(path, this);
	}

	public static WaterMazeConfig open(String path) {
		return (WaterMazeConfig) TrackingIO.openObject(path);
	}

	public List<AbstractTrackingParameter> getParameters() {
		return parameters;
	}

}
