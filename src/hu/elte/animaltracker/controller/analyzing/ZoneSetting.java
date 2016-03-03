package hu.elte.animaltracker.controller.analyzing;

import hu.elte.animaltracker.model.analyzing.AbstractTrackingParameter;
import hu.elte.animaltracker.model.zones.ZoneUnit;

import java.util.ArrayList;
import java.util.List;

public class ZoneSetting {
	protected List<ParameterController> parameterControllers;
	protected ZoneUnit zone;

	public ZoneSetting(List<AbstractTrackingParameter> parameterConfigs,
			ZoneUnit zone) {
		super();
		setParameters(parameterConfigs);
		this.zone = zone;
	}

	public List<ParameterController> getParameterControllers() {
		return parameterControllers;
	}

	public ZoneUnit getZone() {
		return zone;
	}

	public void setParameters(List<AbstractTrackingParameter> parameters) {
		parameterControllers = new ArrayList<ParameterController>();
		for (AbstractTrackingParameter parameter : parameters) {
			parameterControllers.add(new ParameterController(parameter));
		}
	}

	public List<AbstractTrackingParameter> getParameterConfigs() {
		List<AbstractTrackingParameter> parameters = new ArrayList<AbstractTrackingParameter>();

		for (ParameterController controller : parameterControllers)
			parameters.add(controller.getParameter());

		return parameters;
	}
}
