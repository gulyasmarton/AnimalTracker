package hu.elte.animaltracker.examples.radialmaze;

import hu.elte.animaltracker.model.TrackingIO;
import hu.elte.animaltracker.model.analyzing.AbstractTrackingParameter;
import hu.elte.animaltracker.model.tracking.TrackingTask;

import java.awt.Polygon;
import java.io.Serializable;
import java.util.List;

public class RadialMazeConfig implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6814856499970610067L;
	protected Polygon maze;
	protected TrackingTask task;
	protected List<AbstractTrackingParameter> parameters;

	public RadialMazeConfig(Polygon maze, TrackingTask task,
			List<AbstractTrackingParameter> parameters) {
		super();
		this.maze = maze;
		this.task = task;
		this.parameters = parameters;
	}

	public Polygon getMaze() {
		return maze;
	}

	public TrackingTask getTask() {
		return task;
	}

	public List<AbstractTrackingParameter> getParameters() {
		return parameters;
	}

	public boolean save(String path) {
		return TrackingIO.saveObject(path, this);
	}

	public static RadialMazeConfig open(String path) {
		return (RadialMazeConfig) TrackingIO.openObject(path);
	}

}
