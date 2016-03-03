package hu.elte.animaltracker.examples.watermaze;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import hu.elte.animaltracker.controller.analyzing.TrackAnalyzerController;
import hu.elte.animaltracker.controller.listeners.TrackAnalyzerControllerListener;
import hu.elte.animaltracker.controller.listeners.TrackerControllerListener;
import hu.elte.animaltracker.controller.tracking.TrackerController;
import hu.elte.animaltracker.model.TrackingIO;
import hu.elte.animaltracker.model.analyzing.AbstractTrackingParameter;
import hu.elte.animaltracker.model.analyzing.DataPoint;
import hu.elte.animaltracker.model.analyzing.TrackingParameterDistance;
import hu.elte.animaltracker.model.analyzing.TrackingParameterImmobilityTime;
import hu.elte.animaltracker.model.analyzing.TrackingParameterTime;
import hu.elte.animaltracker.model.analyzing.TrackingParameterVelocityVector;
import hu.elte.animaltracker.model.tracking.CoreTracker;
import hu.elte.animaltracker.model.tracking.ObjectLocation;
import hu.elte.animaltracker.model.tracking.TrackSequence;
import hu.elte.animaltracker.model.tracking.TrackingTask;
import hu.elte.animaltracker.model.tracking.Vector2D;
import hu.elte.animaltracker.model.zones.Primitive;
import hu.elte.animaltracker.model.zones.SetIntersection;
import hu.elte.animaltracker.model.zones.SetSubtraction;
import hu.elte.animaltracker.model.zones.ZoneUnit;
import hu.elte.animaltracker.view.analyzing.TrackAnalyzerView;
import hu.elte.animaltracker.view.tracking.TrackerView;
import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.Arrow;
import ij.gui.GenericDialog;
import ij.gui.Line;
import ij.gui.OvalRoi;
import ij.gui.Overlay;
import ij.gui.Roi;
import ij.gui.TextRoi;
import ij.plugin.PlugIn;

public class WaterMazeController implements PlugIn {

	protected ImagePlus imp;
	protected ZoneUnit base, innerCircle, outerCircle, A, B, C, D, inA, inB,
			inC, inD, outA, outB, outC, outD;
	protected List<ZoneUnit> zoneUnits;
	protected Rectangle platform;
	protected Rectangle pool;
	double innerRatio = 0.75;

	protected TrackingTask task;

	protected List<AbstractTrackingParameter> parameters;

	protected boolean platformVisible = true;
	protected boolean poolVisible = true;
	protected boolean trackVisible = true;
	protected boolean vectorVisible;
	protected boolean angleVisible;

	public WaterMazeController() {

	}

	@Override
	public void run(String arg) {
		WaterMazeView view = new WaterMazeView(this);
		view.setVisible(true);
	}

	public void setActiveImage() {
		imp = WindowManager.getCurrentImage();
	}

	public void setPool() {
		if (imp == null) {
			IJ.noImage();
			return;
		}
		Roi r = imp.getRoi();

		if (r == null || r.getType() != Roi.OVAL) {
			IJ.error("No oval roi!");
			return;
		}

		GenericDialog gd = new GenericDialog("Inner circle");
		gd.addNumericField("inner/outer radius ratio: ", innerRatio, 3);
		gd.showDialog();
		if (gd.wasCanceled())
			return;
		innerRatio = gd.getNextNumber();
		innerRatio = Math.min(0.9, innerRatio);
		innerRatio = Math.max(0.1, innerRatio);

		setPool((OvalRoi) r);
	}

	protected void setPool(Rectangle pool) {
		setPool(new OvalRoi(pool.x, pool.y, pool.width, pool.height));
	}

	protected void setPool(OvalRoi r) {

		pool = r.getBounds();

		base = Primitive.createPrimitive(r, true);

		double w = pool.getWidth() * innerRatio;
		double h = pool.getHeight() * innerRatio;
		double x = pool.getX() + pool.getWidth() * ((1 - innerRatio) / 2);
		double y = pool.getY() + pool.getHeight() * ((1 - innerRatio) / 2);

		Rectangle recA = new Rectangle(pool.x, pool.y, pool.width / 2,
				pool.height / 2);
		Rectangle recB = new Rectangle(pool.x + pool.width / 2, pool.y,
				pool.width / 2, pool.height / 2);
		Rectangle recC = new Rectangle(pool.x + pool.width / 2, pool.y
				+ pool.height / 2, pool.width / 2, pool.height / 2);
		Rectangle recD = new Rectangle(pool.x, pool.y + pool.height / 2,
				pool.width / 2, pool.height / 2);

		innerCircle = Primitive.createPrimitive(new OvalRoi(x, y, w, h), false);

		outerCircle = new SetSubtraction(base.duplicate(),
				innerCircle.duplicate());

		A = new SetIntersection(base.duplicate(), Primitive.createPrimitive(
				new Roi(recA), false));
		B = new SetIntersection(base.duplicate(), Primitive.createPrimitive(
				new Roi(recB), false));
		C = new SetIntersection(base.duplicate(), Primitive.createPrimitive(
				new Roi(recC), false));
		D = new SetIntersection(base.duplicate(), Primitive.createPrimitive(
				new Roi(recD), false));

		inA = new SetIntersection(innerCircle.duplicate(), A.duplicate());
		inB = new SetIntersection(innerCircle.duplicate(), B.duplicate());
		inC = new SetIntersection(innerCircle.duplicate(), C.duplicate());
		inD = new SetIntersection(innerCircle.duplicate(), D.duplicate());

		outA = new SetSubtraction(A.duplicate(), inA.duplicate());
		outB = new SetSubtraction(B.duplicate(), inB.duplicate());
		outC = new SetSubtraction(C.duplicate(), inC.duplicate());
		outD = new SetSubtraction(D.duplicate(), inD.duplicate());

		zoneUnits = new ArrayList<ZoneUnit>();

		zoneUnits.add(inA);
		zoneUnits.add(inB);
		zoneUnits.add(inC);
		zoneUnits.add(inD);
		zoneUnits.add(outA);
		zoneUnits.add(outB);
		zoneUnits.add(outC);
		zoneUnits.add(outD);
		zoneUnits.add(base);
		zoneUnits.add(A);
		zoneUnits.add(B);
		zoneUnits.add(C);
		zoneUnits.add(D);
		zoneUnits.add(innerCircle);
		zoneUnits.add(outerCircle);

		A.setName("A");
		B.setName("B");
		C.setName("C");
		D.setName("D");
		inA.setName("inA");
		inB.setName("inB");
		inC.setName("inC");
		inD.setName("inD");
		outA.setName("outA");
		outB.setName("outB");
		outC.setName("outC");
		outD.setName("outD");
		base.setName("base");
		innerCircle.setName("innerCircle");
		outerCircle.setName("outerCircle");

		updateView();
	}

	public void setPlatform() {
		if (imp == null) {
			IJ.noImage();
			return;
		}
		Roi r = imp.getRoi();

		if (r == null || r.getType() != Roi.OVAL) {
			IJ.error("No oval roi!");
			return;
		}
		platform = r.getBounds();
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

	public void showSummary() {
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

			Point2D.Float p = new Point2D.Float((float) platform.getBounds()
					.getCenterX(), (float) platform.getBounds().getCenterY());
			TrackingParameterAnglePreference angle = new TrackingParameterAnglePreference(
					p, distance);
			parameters.add(angle);
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

	protected void updateView() {
		if (imp == null)
			return;

		Overlay ov = new Overlay();

		if (zoneUnits != null && isPoolVisible())
			for (int i = 0; i < 8; i++) {
				Roi r = zoneUnits.get(i).getRoi();
				r.setStrokeColor(Color.green);
				ov.add(r);
			}

		if (platform != null && isPlatformVisible()) {
			OvalRoi r = new OvalRoi(platform.x, platform.y, platform.width,
					platform.height);
			r.setStrokeColor(Color.red);
			r.setStrokeWidth(2f);
			ov.add(r);
		}

		if (task != null && task.getPoint() != null) {
			List<TrackSequence> track = TrackSequence
					.getConnectedSequences(task.getPoint());

			TrackingParameterAnglePreference p = null;
			TrackingParameterVelocityVector v = null;
			if (parameters != null)
				for (AbstractTrackingParameter cp : parameters)
					if (cp instanceof TrackingParameterAnglePreference)
						p = (TrackingParameterAnglePreference) cp;
					else if (cp instanceof TrackingParameterVelocityVector)
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
					vector.multiply(1d * v.getScaleDistance());
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

			if (p != null && platform != null && isAngleVisible()) {
				List<DataPoint<Vector2D>> list = p.getValues(track);
				String degree = "";
				for (DataPoint<Vector2D> dataPoint : list) {
					Vector2D vector = dataPoint.getData();
					double deg = Math.abs(vector.getDegree());
					if (deg > 180)
						deg = 360 - deg;
					degree = "degree: " + Math.round(deg) + "°";
					vector.multiply(1d / p.getScaleDistance());
					Arrow arrow = new Arrow(vector.getP1().x, vector.getP1().y,
							platform.getCenterX(), platform.getCenterY());
					arrow.setPosition(dataPoint.getFrame());
					arrow.setStrokeWidth(2f);
					arrow.setHeadSize(5);
					arrow.setStrokeColor(Color.red);
					ov.add(arrow);

					TextRoi t = new TextRoi(10, textY, degree);
					t.setPosition(dataPoint.getFrame());
					ov.add(t);
				}
			}
		}

		imp.setOverlay(ov);
		imp.killRoi();
	}

	public void load() {
		String path = TrackingIO.commonOpenDialog("Open WaterMaze Config",
				null, "config.bin");
		if (path == null)
			return;
		WaterMazeConfig config = WaterMazeConfig.open(path);
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
		innerRatio = config.getInnerRatio();
		platform = config.getPlatform();
		setPool(config.getPool());
		updateView();
	}

	public void save() {
		if (imp == null) {
			IJ.noImage();
			return;
		}
		// if(platform==null){
		// IJ.error("No platform");
		// return;
		// }
		if (zoneUnits == null) {
			IJ.error("No pool");
			return;
		}
		WaterMazeConfig config = new WaterMazeConfig(pool, platform,
				innerRatio, task, parameters);

		String path = TrackingIO.commonSaveDialog("Save WaterMaze Config",
				null, "config", ".bin");
		if (path != null)
			config.save(path);

	}

	public void setPlatformVisible(boolean platformVisible) {
		this.platformVisible = platformVisible;
		updateView();
	}

	public boolean isPoolVisible() {
		return poolVisible;
	}

	public void setPoolVisible(boolean poolVisible) {
		this.poolVisible = poolVisible;
		updateView();
	}

	public boolean isTrackVisible() {
		return trackVisible;
	}

	public void setTrackVisible(boolean trackVisible) {
		this.trackVisible = trackVisible;
		updateView();
	}

	public boolean isVectorVisible() {
		return vectorVisible;
	}

	public void setVectorVisible(boolean vectorVisible) {
		this.vectorVisible = vectorVisible;
		updateView();
	}

	public boolean isAngleVisible() {
		return angleVisible;
	}

	public void setAngleVisible(boolean angleVisible) {
		this.angleVisible = angleVisible;
		updateView();
	}

	public boolean isPlatformVisible() {
		return platformVisible;
	}
}
