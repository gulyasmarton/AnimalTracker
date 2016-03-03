package hu.elte.animaltracker.controller.analyzing;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.TableModel;

import hu.elte.animaltracker.controller.listeners.TrackAnalyzerControllerListener;
import hu.elte.animaltracker.model.TrackingIO;
import hu.elte.animaltracker.model.analyzing.AbstractTrackingParameter;
import hu.elte.animaltracker.model.analyzing.DataPoint;
import hu.elte.animaltracker.model.analyzing.TrackingParameterDistance;
import hu.elte.animaltracker.model.analyzing.TrackingParameterImmobilityTime;
import hu.elte.animaltracker.model.analyzing.TrackingParameterTime;
import hu.elte.animaltracker.model.analyzing.TrackingParameterVelocityVector;
import hu.elte.animaltracker.model.analyzing.TrackingResult;
import hu.elte.animaltracker.model.analyzing.TrackingResultTable;
import hu.elte.animaltracker.model.tracking.ObjectLocation;
import hu.elte.animaltracker.model.tracking.TrackSequence;
import hu.elte.animaltracker.model.zones.ZoneUnit;
import hu.elte.animaltracker.view.analyzing.*;
import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.GenericDialog;
import ij.gui.Roi;
import ij.measure.Calibration;
import ij.measure.ResultsTable;
import ij.plugin.PlugIn;

public class TrackAnalyzerController implements PlugIn {
	protected List<ZoneUnit> zones;
	protected TrackParametersTableModel model;
	private ImagePlus imp;

	protected List<AbstractTrackingParameter> parameterConfigs;
	protected List<ZoneSetting> zoneSettings;
	protected ObjectLocation[] points;

	protected List<TrackAnalyzerControllerListener> listeners = new ArrayList<TrackAnalyzerControllerListener>();

	public TrackAnalyzerController() {

		List<AbstractTrackingParameter> parameters = new ArrayList<AbstractTrackingParameter>();

		TrackingParameterDistance distance = new TrackingParameterDistance(
				"pixel");
		parameters.add(distance);

		TrackingParameterTime time = new TrackingParameterTime("sec");
		time.setScale(1d / 15d);
		parameters.add(time);

		TrackingParameterImmobilityTime imtime = new TrackingParameterImmobilityTime(
				time);		
		
		imtime.setMinDistance(10);
		imtime.setMinTimeInFrame(15);
		parameters.add(imtime);
		
		TrackingParameterVelocityVector velocityVector = new TrackingParameterVelocityVector(distance, time);
		parameters.add(velocityVector);
		

		Init(parameters, zones, null);
	}

	public TrackAnalyzerController(List<AbstractTrackingParameter> parameters,
			List<ZoneUnit> zones, ObjectLocation[] points) {
		Init(parameters, zones, points);
	}

	protected void Init(List<AbstractTrackingParameter> parameters,
			List<ZoneUnit> zones, ObjectLocation[] points) {
		setParameterConfigs(parameters);
		this.points = points;
		setZones(zones);
	}

	public void addTrackAnalyzerControllerListener(
			TrackAnalyzerControllerListener listener) {
		if (!listeners.contains(listener))
			listeners.add(listener);
	}

	public void removeTrackAnalyzerControllerListener(
			TrackAnalyzerControllerListener listener) {
		listeners.remove(listener);
	}

	protected void fireanalyzingDone() {
		for (TrackAnalyzerControllerListener listener : listeners) {
			listener.analyzingDone();
		}
	}

	protected void setZones(List<ZoneUnit> zones) {
		this.zones = zones;
		zoneSettings = new ArrayList<ZoneSetting>();
		if (zones != null)
			for (ZoneUnit zone : zones) {
				zoneSettings.add(new ZoneSetting(parameterConfigs, zone));

			}
		updateTable();
	}

	public void loadTrack() {
		String path = TrackingIO.commonOpenDialog("Open Tracking...", null,
				null);
		if (path != null)
			points = TrackingIO.openTrack(path);
	}

	public void loadZones() {
		String path = TrackingIO.commonOpenDialog("Open Zones...", null, null);
		if (path != null)
			setZones(TrackingIO.openZoneUnits(path));
	}

	public TableModel getModel() {
		return model;
	}

	public TrackingResultTable getData() {
		TrackingResultTable table = new TrackingResultTable();
		for (ZoneSetting selector : zoneSettings) {
			List<TrackSequence> sequences = TrackSequence.getInnerSequences(
					points, selector.getZone().getRoi());

			for (ParameterController pc : selector.getParameterControllers()) {
				TrackingResult result = pc.setSequences(sequences);
				table.add(selector.getZone(), pc.getParameter(), result);
			}

		}
		return table;
	}

	public void showResult() {
		GenericDialog gd = new GenericDialog("Show settings");
		String[] params = new String[] { "Zones", "Parameters" };
		gd.addChoice("Grouped by", params, params[0]);
		gd.showDialog();
		if (gd.wasCanceled())
			return;

		TrackingResultTable table2 = getData();

		// Grouped by Zones
		if (gd.getNextChoice().equals(params[0])) {

			for (ZoneSetting selector : zoneSettings) {
				ResultsTable table = new ResultsTable();

				for (AbstractTrackingParameter parameter : parameterConfigs) {

					Object sum = table2.getValue(selector.getZone(), parameter,
							TrackingResultTable.DataType.SUM);
					Object mean = table2.getValue(selector.getZone(),
							parameter, TrackingResultTable.DataType.MEAN);
					Object sd = table2.getValue(selector.getZone(), parameter,
							TrackingResultTable.DataType.SD);
					Object max = table2.getValue(selector.getZone(), parameter,
							TrackingResultTable.DataType.MAX);
					Object min = table2.getValue(selector.getZone(), parameter,
							TrackingResultTable.DataType.MIN);
					Object raw = table2.getValue(selector.getZone(), parameter,
							TrackingResultTable.DataType.RAW);

					if (sum == null && mean == null && sd == null
							&& max == null && min == null && raw == null)
						continue;

					if (sum != null) {
						table.incrementCounter();
						table.addValue("Parameter", parameter.getName() + "("
								+ parameter.getUnit() + ") - SUM");
						table.addValue("Value", sum.toString());
					}

					if (mean != null) {
						table.incrementCounter();
						table.addValue("Parameter", parameter.getName() + "("
								+ parameter.getUnit() + ") - Mean");
						table.addValue("Value", mean.toString());
					}

					if (sd != null) {
						table.incrementCounter();
						table.addValue("Parameter", parameter.getName() + "("
								+ parameter.getUnit() + ") - SD");
						table.addValue("Value", sd.toString());
					}

					if (max != null) {
						table.incrementCounter();
						table.addValue("Parameter", parameter.getName() + "("
								+ parameter.getUnit() + ") - Max");
						table.addValue("Value", max.toString());
					}

					if (min != null) {
						table.incrementCounter();
						table.addValue("Parameter", parameter.getName() + "("
								+ parameter.getUnit() + ") - Min");
						table.addValue("Value", min.toString());
					}
				}

				if (table.getCounter() == 0)
					continue;
				table.show(selector.getZone().getName());
			}
		} else {
			for (AbstractTrackingParameter parameter : parameterConfigs) {
				ResultsTable table = new ResultsTable();
				for (ZoneSetting selector : zoneSettings) {

					Object sum = table2.getValue(selector.getZone(), parameter,
							TrackingResultTable.DataType.SUM);
					Object mean = table2.getValue(selector.getZone(),
							parameter, TrackingResultTable.DataType.MEAN);
					Object sd = table2.getValue(selector.getZone(), parameter,
							TrackingResultTable.DataType.SD);
					Object max = table2.getValue(selector.getZone(), parameter,
							TrackingResultTable.DataType.MAX);
					Object min = table2.getValue(selector.getZone(), parameter,
							TrackingResultTable.DataType.MIN);
					Object raw = table2.getValue(selector.getZone(), parameter,
							TrackingResultTable.DataType.RAW);

					if (sum == null && mean == null && sd == null
							&& max == null && min == null && raw == null)
						continue;
					if (sum != null) {
						table.incrementCounter();
						table.addValue("Zone", selector.getZone().getName()
								+ " - SUM");
						table.addValue("Value", sum.toString());
					}

					if (mean != null) {
						table.incrementCounter();
						table.addValue("Zone", selector.getZone().getName()
								+ " - Mean");
						table.addValue("Value", mean.toString());
					}

					if (sd != null) {
						table.incrementCounter();
						table.addValue("Zone", selector.getZone().getName()
								+ " - SD");
						table.addValue("Value", sd.toString());
					}

					if (max != null) {
						table.incrementCounter();
						table.addValue("Zone", selector.getZone().getName()
								+ " - Max");
						table.addValue("Value", max.toString());
					}

					if (min != null) {
						table.incrementCounter();
						table.addValue("Zone", selector.getZone().getName()
								+ " - Min");
						table.addValue("Value", min.toString());
					}
				}

				if (table.getCounter() == 0)
					continue;
				table.show(parameter.getName() + " (" + parameter.getUnit()
						+ ")");
			}

		}
	}

	public void setActiveImage() {
		// TODO Auto-generated method stub
		ImagePlus imp = WindowManager.getCurrentImage();
		if (imp != null)
			this.imp = imp;
		Calibration calibration = imp.getCalibration();

		// distanceUnit = calibration.getUnit();
		// pixelUnit = calibration.getRawX(1);
		// timeUnit = calibration.getTimeUnit();
		// frameInterval = calibration.frameInterval;
		//
		// time.setUnit(calibration.getTimeUnit());
		// time.setScale(calibration.frameInterval);
		//
		// distance.setUnit(calibration.getUnit());
		// distance.setScale(calibration.getRawX(1));
	}

	public void updateTable() {
		model.clear();
		if (zoneSettings != null)
			model.addRows(zoneSettings);
	}

	public void setSelectedRow(int row) {
		// TODO Auto-generated method stub
		if (imp == null)
			return;
		Roi r = model.getRoi(row);
		imp.setRoi(r);
	}

	public void showSettings() {
		// TODO Auto-generated method stub

	}

	@Override
	public void run(String arg) {
		TrackAnalyzerView view = new TrackAnalyzerView(this);
		view.setVisible(true);

	}

	public void showParameterSelector(int[] idxs) {
		if (idxs.length == 0) {
			IJ.error("No selected row!");
			return;
		}
		List<ZoneSetting> list = new ArrayList<ZoneSetting>();
		for (int i = 0; i < idxs.length; i++) {
			list.add(zoneSettings.get(idxs[i]));
		}
		ZoneSettingsView view = new ZoneSettingsView(this, list);
		view.setVisible(true);
	}

	public void showParametersSettings() {
		ZoneAnalyzeParameterView view = new ZoneAnalyzeParameterView(
				parameterConfigs);
		view.setVisible(true);
	}

	public void done() {
		fireanalyzingDone();
	}

	public void setActiveImage(ImagePlus imp) {
		this.imp = imp;
	}

	public void saveParameterConfigs() {
		String path = TrackingIO.commonSaveDialog("Save parameter config...",
				null, "config", "pcf");
		if (path != null)
			TrackingIO.saveAbstractTrackingParameters(path, parameterConfigs);
	}

	public void saveZoneSettings() {
		String path = TrackingIO.commonSaveDialog("Save zone settings...",
				null, "config", "zsf");
		if (path != null)
			TrackingIO.saveAnalyzeSettings(path, zoneSettings);
	}

	public void openZoneSettings() {
		String path = TrackingIO.commonOpenDialog("Open zone settings...",
				null, "*.zsf");
		if (path != null)
			setAnalyzeSettings(TrackingIO.openAnalyzeSettings(path));
	}

	public void openParameterConfigs() {
		String path = TrackingIO.commonOpenDialog("Open parameter config...",
				null, "*.pcf");
		if (path != null)
			setParameterConfigs(TrackingIO.openAbstractTrackingParameters(path));
	}

	public void setAnalyzeSettings(List<ZoneSetting> zoneSettings) {
		if (zoneSettings == null || zoneSettings.size() == 0)
			return;
		this.zoneSettings = zoneSettings;

		this.parameterConfigs = zoneSettings.get(0).getParameterConfigs();
		model = new TrackParametersTableModel(this.parameterConfigs);

		updateTable();
	}

	public List<AbstractTrackingParameter> getParameterConfigs() {
		return parameterConfigs;
	}

	public void setParameterConfigs(List<AbstractTrackingParameter> parameters) {
		this.parameterConfigs = parameters;
		model = new TrackParametersTableModel(parameters);

		if (zoneSettings != null)
			for (ZoneSetting z : zoneSettings) {
				z.setParameters(parameters);
			}
		updateTable();
	}
}