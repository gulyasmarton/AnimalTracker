package hu.elte.animaltracker.examples.radialmaze;

import hu.elte.animaltracker.controller.analyzing.TrackAnalyzerController;
import hu.elte.animaltracker.controller.listeners.TrackAnalyzerControllerListener;
import hu.elte.animaltracker.controller.listeners.TrackerControllerListener;
import hu.elte.animaltracker.controller.tracking.TrackerController;
import hu.elte.animaltracker.examples.watermaze.TrackingParameterAnglePreference;
import hu.elte.animaltracker.examples.watermaze.WaterMazeConfig;
import hu.elte.animaltracker.model.TrackingIO;
import hu.elte.animaltracker.model.analyzing.AbstractTrackingParameter;
import hu.elte.animaltracker.model.analyzing.DataPoint;
import hu.elte.animaltracker.model.analyzing.TrackingParameterDistance;
import hu.elte.animaltracker.model.analyzing.TrackingParameterImmobilityTime;
import hu.elte.animaltracker.model.analyzing.TrackingParameterTime;
import hu.elte.animaltracker.model.analyzing.TrackingParameterVelocityVector;
import hu.elte.animaltracker.model.tracking.ObjectLocation;
import hu.elte.animaltracker.model.tracking.TrackSequence;
import hu.elte.animaltracker.model.tracking.TrackingTask;
import hu.elte.animaltracker.model.tracking.Vector2D;
import hu.elte.animaltracker.model.zones.Primitive;
import hu.elte.animaltracker.model.zones.SetUnion;
import hu.elte.animaltracker.model.zones.ZoneUnit;
import hu.elte.animaltracker.view.analyzing.TrackAnalyzerView;
import hu.elte.animaltracker.view.tracking.TrackerView;
import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.Arrow;
import ij.gui.Overlay;
import ij.gui.PolygonRoi;
import ij.gui.Roi;
import ij.gui.TextRoi;
import ij.plugin.PlugIn;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class RadialMazeController implements PlugIn {

	protected ImagePlus imp;
	protected ZoneUnit base, center, arms;
	protected List<ZoneUnit> zoneUnits;
	protected Polygon maze;

	protected TrackingTask task;

	protected List<AbstractTrackingParameter> parameters;

	protected boolean mazeVisible = true;
	protected boolean trackVisible;
	protected boolean vectorVisible;

	public void setActiveImage() {
		imp = WindowManager.getCurrentImage();
	}

	public void setMaze() {
		if (imp == null) {
			IJ.noImage();
			return;
		}
		Roi r = imp.getRoi();

		if (r == null || r.getType() != Roi.POLYGON) {
			IJ.error("No polygon roi!");
			return;
		}
		setMaze(r.getPolygon());
	}

	protected void setMaze(Polygon polygon) {
		if (polygon.npoints % 3 != 0) {
			IJ.error("You need a polygon with a number of corners that can be divided by three. Show documentation.");
			return;
		}

		int armsNum = polygon.npoints / 3;

		maze = polygon;
		zoneUnits = new ArrayList<ZoneUnit>();
		List<ZoneUnit> armsList = new ArrayList<ZoneUnit>(armsNum);

		int[] xp = maze.xpoints;
		int[] yp = maze.ypoints;
		int[] nxp = new int[armsNum];
		int[] nyp = new int[armsNum];
		int i = 0;
		for (int j = 0; j < polygon.npoints; j += 3) {
			nxp[i] = xp[j];
			nyp[i++] = yp[j];
		}

		center = Primitive.createPrimitive(new PolygonRoi(new Polygon(nxp, nyp,
				armsNum), Roi.POLYGON), true);
		center.setName("Center");
		zoneUnits.add(center);

		for (int j = 0; j < polygon.npoints; j += 3) {
			nxp = new int[4];
			nyp = new int[4];
			for (int k = 0; k < 4; k++) {
				if (j + k == xp.length) {
					nxp[k] = xp[0];
					nyp[k] = yp[0];
				} else {
					nxp[k] = xp[j + k];
					nyp[k] = yp[j + k];
				}
			}
			ZoneUnit arm = Primitive.createPrimitive(new PolygonRoi(
					new Polygon(nxp, nyp, 4), Roi.POLYGON), true);
			armsList.add(arm);
			zoneUnits.add(arm);
			arm.setName("Arm - "+(j+1));
		}
		arms = new SetUnion(armsList);
		arms.setName("Arms");

		base = new SetUnion(center.duplicate(), arms.duplicate());
		base.setName("All");
		zoneUnits.add(base);
		

		updateView();
	}

	public void showTracker() {
		if (imp == null) {
			IJ.noImage();
			return;
		}
		if (base == null) {
			IJ.error("No pool!");
		}

		final TrackerController trackerController = task == null ? new TrackerController(
				imp) : new TrackerController(task);
		trackerController.setTrackigArea(base);
		trackerController
				.addTrackerControllerListener(new TrackerControllerListener() {

					@Override
					public void activeImageChanged(ImagePlus imp) {
					}

					@Override
					public void TrackerControllerReturned() {
						// positions = task.getPoint();
						task = trackerController.getTrackingTask();
					}
				});

		TrackerView view = new TrackerView(trackerController);
		view.setVisible(true);

	}

	public void showAnalyzer() {
		if (task == null || task.getPoint() == null) {
			IJ.error("No track");
			return;
		}
		if (zoneUnits == null) {
			IJ.error("No zones");
			return;
		}

		if (parameters == null) {
			parameters = new ArrayList<AbstractTrackingParameter>();

			TrackingParameterDistance distance = new TrackingParameterDistance(
					"cm");
			distance.setScale(1d);
			parameters.add(distance);

			TrackingParameterTime time = new TrackingParameterTime("sec");
			time.setScale(1d / 15d);
			parameters.add(time);

			TrackingParameterImmobilityTime imtime = new TrackingParameterImmobilityTime(
					time);
			imtime.setMinDistance(10);
			imtime.setMinTimeInFrame(15);
			parameters.add(imtime);

			TrackingParameterVelocityVector vel = new TrackingParameterVelocityVector(
					distance, time);
			parameters.add(vel);

		}

		TrackAnalyzerController analyzerController = new TrackAnalyzerController(
				parameters, zoneUnits, task.getPoint());
		analyzerController.setActiveImage(imp);

		TrackAnalyzerView view = new TrackAnalyzerView(analyzerController);
		view.setVisible(true);

		analyzerController
				.addTrackAnalyzerControllerListener(new TrackAnalyzerControllerListener() {

					@Override
					public void analyzingDone() {
						// TODO Auto-generated method stub

					}
				});
	}

	public void setMazeVisible(boolean mazeVisible) {
		this.mazeVisible = mazeVisible;
		updateView();
	}

	public void setTrackVisible(boolean trackVisible) {
		this.trackVisible = trackVisible;
		updateView();
	}

	public void setVectorVisible(boolean vectorVisible) {
		this.vectorVisible = vectorVisible;
		updateView();
	}

	public void load() {
		String path = TrackingIO.commonOpenDialog("Open RadialMaze Config",
				null, "config.bin");
		if (path == null)
			return;
		RadialMazeConfig config = RadialMazeConfig.open(path);
		if (config == null) {
			IJ.error("loading error");
			return;
		}
		task = config.getTask();
		if (task != null)
			imp = task.getImage();
		if (imp != null)
			imp.show();
		parameters = config.getParameters();
		setMaze(config.getMaze());
	}

	public void save() {
		if (imp == null) {
			IJ.noImage();
			return;
		}

		if (maze == null) {
			IJ.error("No maze");
			return;
		}
		RadialMazeConfig config = new RadialMazeConfig(maze, task, parameters);

		String path = TrackingIO.commonSaveDialog("Save RadialMaze Config",
				null, "config", ".bin");
		if (path != null)
			config.save(path);

	}

	public boolean isMazeVisible() {
		return mazeVisible;
	}

	public boolean isTrackVisible() {
		return trackVisible;
	}

	public boolean isVectorVisible() {
		return vectorVisible;
	}

	protected void updateView() {
		if (imp == null)
			return;

		Overlay ov = new Overlay();

		if (zoneUnits != null && isMazeVisible())
			for (int i = 0; i < maze.npoints/3+1; i++) {
				Roi r = zoneUnits.get(i).getRoi();
				r.setStrokeColor(Color.green);
				ov.add(r);
			}

		if (task != null && task.getPoint() != null) {
			List<TrackSequence> track = TrackSequence
					.getConnectedSequences(task.getPoint());

			TrackingParameterVelocityVector v = null;
			if (parameters != null)
				for (AbstractTrackingParameter cp : parameters)
					if (cp instanceof TrackingParameterVelocityVector)
						v = (TrackingParameterVelocityVector) cp;

			if (isTrackVisible())
				for (TrackSequence ts : track) {
					if (ts.size() < 2)
						continue;
					for (ObjectLocation ol : ts) {
						Roi r = ts.getRoiByIndex(ol.frame);
						r.setStrokeColor(Color.yellow);
						r.setPosition(ol.frame);
						ov.add(r);
					}
				}
			int textY = 10;
			if (v != null && isVectorVisible()) {
				List<DataPoint<Vector2D>> list = v.getValues(track);
				String velocity = "";
				for (DataPoint<Vector2D> dataPoint : list) {
					Vector2D vector = dataPoint.getData();
					velocity = "velocity: " + Math.round(vector.getLength())
							+ " " + v.getUnit();
					vector.multiply(1d / v.getScaleDistance());
					Arrow arrow = new Arrow(vector.getP1().x, vector.getP1().y,
							vector.getP2().x, vector.getP2().y);
					arrow.setPosition(dataPoint.getFrame());
					arrow.setStrokeWidth(2f);
					arrow.setHeadSize(5);
					ov.add(arrow);

					TextRoi t = new TextRoi(10, textY, velocity);
					t.setPosition(dataPoint.getFrame());
					ov.add(t);
				}
				textY += 20;
			}
		}

		imp.setOverlay(ov);
		imp.killRoi();
	}

	@Override
	public void run(String arg) {
		RadialMazeView view = new RadialMazeView(this);
		view.setVisible(true);

	}

}
