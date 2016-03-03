package hu.elte.animaltracker.controller.analyzing;

import java.util.List;

import hu.elte.animaltracker.model.analyzing.AbstractTrackingParameter;
import hu.elte.animaltracker.model.analyzing.TrackingResult;
import hu.elte.animaltracker.model.tracking.TrackSequence;

public class ParameterController {
	protected AbstractTrackingParameter parameter;
	public boolean needRaw, needSum, needMean, needSd, needMin, needMax;

	public ParameterController(
			AbstractTrackingParameter parameter) {
		super();
		this.parameter = parameter;
	}

	public AbstractTrackingParameter getParameter() {
		return parameter;
	}

	public void setParameter(AbstractTrackingParameter parameter) {
		this.parameter = parameter;
	}

	public boolean isNeed() {
		return needRaw || needSum || needMean || needSd || needMin || needMax;
	}

	protected Object getMean(List<Object> raws) {
		if (needMean)
			return parameter.getMean(raws);
		return null;
	}

	protected Object getSd(List<Object> raws) {
		if (needSd)
			return parameter.getSd(raws);
		return null;
	}

	protected Object getSum(List<Object> raws) {
		if (needSum)
			return parameter.getSum(raws);
		return null;
	}

	protected Object getMin(List<Object> raws) {
		if (needMin)
			return parameter.getMin(raws);
		return null;
	}

	protected Object getMax(List<Object> raws) {
		if (needMax)
			return parameter.getMax(raws);
		return null;
	}

	public TrackingResult setSequences(List<TrackSequence> sequences) {
		if(!isNeed())
			return null;
		List<Object> raws = parameter.getValues(sequences);

		return new TrackingResult(raws, getSum(raws), getMean(raws),
				getSd(raws), getMin(raws), getMax(raws));
	}

	public String getName() {
		return parameter.getName();
	}

}
