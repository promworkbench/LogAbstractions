package org.processmining.plugins.log.logabstraction.implementations;

import java.util.HashMap;
import java.util.Map;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.model.XLog;
import org.processmining.framework.plugin.Progress;
import org.processmining.framework.util.Pair;
import org.processmining.plugins.log.logabstraction.abstracts.AbstractLogRelations;

public class AlphaLogRelationsImpl extends AbstractLogRelations {

	protected double[][] parallelMatrix;

	public AlphaLogRelationsImpl(XLog log) {
		this(log, XLogInfoFactory.createLogInfo(log), null);
	}

	public AlphaLogRelationsImpl(XLog log, XLogInfo summary) {
		this(log, summary, null);
	}

	public AlphaLogRelationsImpl(XLog log, Progress progress) {
		this(log, XLogInfoFactory.createLogInfo(log), progress);
	}

	public AlphaLogRelationsImpl(XLog log, XLogInfo summary, Progress progress) {
		super(log, summary, progress);
	}

	@Override
	protected void calculateLogRelations() {
		this.parallelMatrix = new double[this.eventClasses.size()][this.eventClasses.size()];
		fillDirectSuccessionMatrices();
		calculateMetrics();

	}

	@Override
	protected void expandProgress() {
		if (super.progress != null) {
			super.progress
					.setMaximum(super.progress.getMaximum() + (super.eventClasses.size() * super.eventClasses.size()));
		}
	}

	@Override
	protected void calculateMetrics() {
		for (int fromIndex = 0; fromIndex < super.absoluteDirectlyFollowsMatrix.length; fromIndex++) {
			for (int toIndex = 0; toIndex < super.absoluteDirectlyFollowsMatrix.length; toIndex++) {
				if (super.absoluteDirectlyFollowsMatrix[fromIndex][toIndex] > 0) {
					if (super.absoluteDirectlyFollowsMatrix[toIndex][fromIndex] > 0) {
						if (super.absoluteLengthTwoLoopMatrix[fromIndex][toIndex] > 0
								|| super.absoluteLengthTwoLoopMatrix[toIndex][fromIndex] > 0) {
							this.causalMatrix[fromIndex][toIndex] = 1d;
							this.causalMatrix[toIndex][fromIndex] = 1d;
						} else {
							this.parallelMatrix[fromIndex][toIndex] = 1d;
							this.parallelMatrix[toIndex][fromIndex] = 1d;
						}
					} else {
						this.causalMatrix[fromIndex][toIndex] = 1d;
					}
				}
			}
			increaseProgress();
		}
	}

	@Override
	public Map<Pair<XEventClass, XEventClass>, Double> getParallelRelations() {
		Map<Pair<XEventClass, XEventClass>, Double> result = new HashMap<Pair<XEventClass, XEventClass>, Double>();
		for (int i = 0; i < this.parallelMatrix.length; i++) {
			for (int j = 0; j < this.parallelMatrix[i].length; j++) {
				if (this.parallelMatrix[i][j] > 0) {
					XEventClass from = this.eventClasses.get(i);
					XEventClass to = this.eventClasses.get(j);
					result.put(new Pair<XEventClass, XEventClass>(from, to), this.parallelMatrix[i][j]);
				}
			}
		}
		return result;
	}
}
