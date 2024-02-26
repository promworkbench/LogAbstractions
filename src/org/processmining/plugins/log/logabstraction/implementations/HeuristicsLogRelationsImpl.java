package org.processmining.plugins.log.logabstraction.implementations;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.model.XLog;
import org.processmining.framework.plugin.Progress;
import org.processmining.framework.util.Pair;
import org.processmining.plugins.log.logabstraction.LogRelations;
import org.processmining.plugins.log.logabstraction.abstracts.AbstractLogRelations;
import org.processmining.plugins.log.logabstraction.factories.LogRelationsFactory;

import java.util.HashMap;
import java.util.Map;

public class HeuristicsLogRelationsImpl extends AbstractLogRelations {

    protected double[] lengthOneLoopColumn;
    protected double[][] causalLengthTwoLoopMatrix;
    protected LogRelations delegate = null;

    public HeuristicsLogRelationsImpl(XLog log) {
        this(log, XLogInfoFactory.createLogInfo(log), null);
    }

    public HeuristicsLogRelationsImpl(XLog log, XLogInfo summary) {
        this(log, summary, null);
    }

    public HeuristicsLogRelationsImpl(XLog log, Progress progress) {
        this(log, XLogInfoFactory.createLogInfo(log), progress);
    }

    public HeuristicsLogRelationsImpl(XLog log, XLogInfo summary, Progress progress) {
        super(log, summary, progress);
    }

    @Override
    protected void calculateLogRelations() {
        this.lengthOneLoopColumn = new double[super.eventClasses.size()];
        this.causalLengthTwoLoopMatrix = new double[this.eventClasses.size()][this.eventClasses.size()];
        fillDirectSuccessionMatrices();
        calculateMetrics();
    }

    protected void instantiateDelegate() {
        if (this.delegate == null) {
            this.delegate = LogRelationsFactory.constructAlphaLogRelations(log, summary, progress);
        }
    }


    @Override
    protected void expandProgress() {
        if (this.progress != null) {
            int complexity = 2 * (this.eventClasses.size() * this.eventClasses.size()) + this.eventClasses.size();
            this.progress.setMaximum(this.progress.getMaximum() + complexity);
        }
    }

    @Override
    protected void calculateMetrics() {
        calculateCausalMatrix();
        calculateSelfLoopMatrix();
        calculateLenghtTwoLoopMatrix();
    }

    protected void calculateCausalMatrix() {
        for (int from = 0; from < super.eventClasses.size(); from++) {
            for (int to = 0; to < super.eventClasses.size(); to++) {
                if (from != to) {
                    double numerator = super.absoluteDirectlyFollowsMatrix[from][to] - super.absoluteDirectlyFollowsMatrix[to][from];
                    double divisor = super.absoluteDirectlyFollowsMatrix[from][to] + super.absoluteDirectlyFollowsMatrix[to][from] + 1;
                    this.causalMatrix[from][to] = numerator / divisor;
                }
                super.increaseProgress();
            }
        }
    }

    protected void calculateSelfLoopMatrix() {
        for (int i = 0; i < super.eventClasses.size(); i++) {
            double numerator = super.absoluteDirectlyFollowsMatrix[i][i];
            double divisor = numerator + 1;
            this.lengthOneLoopColumn[i] = numerator / divisor;
            super.increaseProgress();
        }
    }

    protected void calculateLenghtTwoLoopMatrix() {
        for (int from = 0; from < super.eventClasses.size(); from++) {
            for (int to = 0; to < super.eventClasses.size(); to++) {
                if (from != to) {
                    double numerator = super.absoluteLengthTwoLoopMatrix[from][to] + super.absoluteLengthTwoLoopMatrix[to][from];
                    double divisor = numerator + 1;
                    this.causalLengthTwoLoopMatrix[from][to] = numerator / divisor;
                }
                super.increaseProgress();
            }
        }
    }

    @Override
    public Map<Pair<XEventClass, XEventClass>, Double> getParallelRelations() {
        this.instantiateDelegate();
        return delegate.getParallelRelations();
    }

    @Override
    public Map<XEventClass, Integer> getLengthOneLoops() {
        this.instantiateDelegate();
        return delegate.getLengthOneLoops();
    }

    @Override
    public Map<XEventClass, Double> lengthOneLoops() {
        Map<XEventClass, Double> result = new HashMap<XEventClass, Double>();
        for (int i = 0; i < super.eventClasses.size(); i++) {
            result.put(super.eventClasses.get(i), this.lengthOneLoopColumn[i]);
        }
        return result;
    }

    @Override
    public Map<Pair<XEventClass, XEventClass>, Double> lengthTwoLoops() {
        Map<Pair<XEventClass, XEventClass>, Double> result = new HashMap<Pair<XEventClass, XEventClass>, Double>();
        for (int i = 0; i < this.causalLengthTwoLoopMatrix.length; i++) {
            for (int j = 0; j < this.causalLengthTwoLoopMatrix[i].length; j++) {
                XEventClass from = this.eventClasses.get(i);
                XEventClass to = this.eventClasses.get(j);
                result.put(new Pair<XEventClass, XEventClass>(from, to), this.causalLengthTwoLoopMatrix[i][j]);
            }
        }
        return result;
    }
}
