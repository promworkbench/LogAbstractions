package org.processmining.logabstractions.factories;

import org.processmining.logabstractions.models.ActivityCountAbstraction;
import org.processmining.logabstractions.models.DirectlyFollowsAbstraction;
import org.processmining.logabstractions.models.TwoWayLengthTwoLoopAbstraction;
import org.processmining.logabstractions.models.UnrelatedAbstraction;
import org.processmining.logabstractions.models.implementations.UnrelatedAbstractionImpl;

public class UnrelatedAbstractionFactory {

	public final static double BOOLEAN_DEFAULT_THRESHOLD = 1.0d;

	public static strictfp double[][] constructBooleanUnrelatedAbstractionMatrix(DirectlyFollowsAbstraction<?> dfa) {
		double[][] ua = new double[dfa.getNumberOfRows()][dfa.getNumberOfColumns()];
		for (int r = 0; r < ua.length; r++) {
			for (int c = 0; c < ua[r].length; c++) {
				if (dfa.getValue(r, c) < dfa.getThreshold()) {
					if (dfa.getValue(c, r) < dfa.getThreshold()) {
						ua[r][c] = BOOLEAN_DEFAULT_THRESHOLD;
					}
				}
			}
		}
		return ua;
	}

	public static <D extends DirectlyFollowsAbstraction<E>, E> UnrelatedAbstraction<E> constructBooleanUnrelatedAbstraction(
			D dfa) {
		return new UnrelatedAbstractionImpl<>(dfa.getEventClasses(), constructBooleanUnrelatedAbstractionMatrix(dfa),
				BOOLEAN_DEFAULT_THRESHOLD);
	}

	public static <D extends DirectlyFollowsAbstraction<E>, E> UnrelatedAbstraction<E> constructAlphaClassicUnrelatedAbstraction(
			D dfa) {
		return constructBooleanUnrelatedAbstraction(dfa);
	}
	
	// NEW FOR ROBUST
	public static <D extends DirectlyFollowsAbstraction<E>, E> UnrelatedAbstraction<E> constructAlphaRobustUnrelatedAbstraction(
			D dfa, double noiseThresholdLeastFreq, double noiseThresholdMostFreq, ActivityCountAbstraction<?> ac) {
		return new UnrelatedAbstractionImpl<>(dfa.getEventClasses(),
				constructRobustUnrelatedAbstractionMatrix(dfa, noiseThresholdLeastFreq, noiseThresholdMostFreq, ac),
				BOOLEAN_DEFAULT_THRESHOLD);
	}
	
	public static strictfp double[][] constructRobustUnrelatedAbstractionMatrix(DirectlyFollowsAbstraction<?> dfa,
			double noiseThresholdLeastFreq, double noiseThresholdMostFreq, ActivityCountAbstraction<?> ac) {
		double[][] ua = new double[dfa.getNumberOfRows()][dfa.getNumberOfColumns()];
		double realNoiseThreshold;
		for (int r = 0; r < ua.length; r++) {
			for (int c = 0; c < ua[r].length; c++) {
				realNoiseThreshold = Math.max(
						noiseThresholdLeastFreq * Math.min(ac.getValue(r), ac.getValue(c)),
						noiseThresholdMostFreq * Math.max(ac.getValue(r), ac.getValue(c))
						);
				if (dfa.getValue(r, c) <= realNoiseThreshold) {
					if (dfa.getValue(c, r) <= realNoiseThreshold) {
						ua[r][c] = BOOLEAN_DEFAULT_THRESHOLD;
					}
				}
			}
		}
		return ua;
	}
	
	//END

	public static <D extends DirectlyFollowsAbstraction<E>, E> UnrelatedAbstraction<E> constructAlphaPlusUnrelatedAbstraction(
			D dfa) {
		return constructAlphaClassicUnrelatedAbstraction(dfa);
	}

	public static <E> UnrelatedAbstraction<E> constructAlphaSharpUnrelatedAbstraction(
			DirectlyFollowsAbstraction<E> dfa, TwoWayLengthTwoLoopAbstraction<E> twltla) {
		return new UnrelatedAbstractionImpl<>(dfa.getEventClasses(), constructAlphaSharpUnrelatedAbstractionMatrix(dfa, twltla),
				BOOLEAN_DEFAULT_THRESHOLD);
	}

	private static strictfp double[][] constructAlphaSharpUnrelatedAbstractionMatrix(DirectlyFollowsAbstraction<?> dfa,
			TwoWayLengthTwoLoopAbstraction<?> twltla) {
		double[][] ua = new double[dfa.getNumberOfRows()][dfa.getNumberOfColumns()];
		for (int r = 0; r < dfa.getMatrix().length; r++) {
			for (int c = 0; c < dfa.getMatrix()[r].length; c++) {
				if (c == r) {
					ua[r][c] = BOOLEAN_DEFAULT_THRESHOLD;
				} else {
					if ((!dfa.holds(r, c) && !dfa.holds(c, r)) || twltla.holds(r, c)) {
						ua[r][c] = BOOLEAN_DEFAULT_THRESHOLD;
					}
				}
			}
		}
		return ua;
	}

}
