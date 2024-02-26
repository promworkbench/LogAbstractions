package org.processmining.logabstractions.factories;

import org.processmining.logabstractions.models.ActivityCountAbstraction;
import org.processmining.logabstractions.models.DirectlyFollowsAbstraction;
import org.processmining.logabstractions.models.ParallelAbstraction;
import org.processmining.logabstractions.models.TwoWayLengthTwoLoopAbstraction;
import org.processmining.logabstractions.models.implementations.ParallelAbstractionImpl;

public class ParallelAbstractionFactory {

	public final static double BOOLEAN_DEFAULT_THRESHOLD = 1.0d;

	public static strictfp <D extends DirectlyFollowsAbstraction<E>, E> ParallelAbstraction<E> constructAlphaClassicParallelAbstraction(
			D dfa) {
		return constructBooleanParallelAbstraction(dfa);
	}

	public static strictfp <D extends DirectlyFollowsAbstraction<E>, L extends TwoWayLengthTwoLoopAbstraction<E>, E> ParallelAbstraction<E> constructAlphaPlusParallelAbstraction(
			D dfa, L twltla) {
		return new ParallelAbstractionImpl<>(dfa.getEventClasses(),
				constructAlphaPlusParallelAbstractionMatrix(dfa, twltla), BOOLEAN_DEFAULT_THRESHOLD);
	}

	public static strictfp <T> double[][] constructAlphaPlusParallelAbstractionMatrix(DirectlyFollowsAbstraction<T> dfa,
			TwoWayLengthTwoLoopAbstraction<T> twltla) {
		double[][] pa = new double[dfa.getNumberOfRows()][dfa.getNumberOfColumns()];
		for (int r = 0; r < pa.length; r++) {
			for (int c = 0; c < pa[r].length; c++) {
				if (dfa.getValue(r, c) >= dfa.getThreshold()) {
					if (dfa.getValue(c, r) >= dfa.getThreshold()) {
						if (!twltla.holds(r, c)) {
							pa[r][c] = BOOLEAN_DEFAULT_THRESHOLD;
							pa[c][r] = BOOLEAN_DEFAULT_THRESHOLD;
						}
					}
				}
			}
		}
		return pa;
	}

	public static strictfp <D extends DirectlyFollowsAbstraction<E>, L extends TwoWayLengthTwoLoopAbstraction<E>, E> ParallelAbstraction<E> constructAlphaPlusPlusParallelAbstraction(
			D dfa, L twltla) {
		return constructAlphaPlusParallelAbstraction(dfa, twltla);
	}

	public static strictfp <T> double[][] constructAlphaPlusPlusParallelAbstractionMatrix(
			DirectlyFollowsAbstraction<T> dfa, TwoWayLengthTwoLoopAbstraction<T> twltla) {
		return constructAlphaPlusParallelAbstractionMatrix(dfa, twltla);

	}

	public static strictfp <E> ParallelAbstraction<E> constructAlphaSharpParallelAbstraction(
			DirectlyFollowsAbstraction<E> directlyFollowsAbstraction,
			TwoWayLengthTwoLoopAbstraction<E> twoWayLengthTwoLoopAbstraction) {
		return new ParallelAbstractionImpl<>(directlyFollowsAbstraction.getEventClasses(),
				constructAlphaSharpParallelAbstractionMatrix(directlyFollowsAbstraction,
						twoWayLengthTwoLoopAbstraction),
				BOOLEAN_DEFAULT_THRESHOLD);
	}

	private static strictfp double[][] constructAlphaSharpParallelAbstractionMatrix(DirectlyFollowsAbstraction<?> dfa,
			TwoWayLengthTwoLoopAbstraction<?> twltla) {
		double[][] pa = new double[dfa.getNumberOfRows()][dfa.getNumberOfColumns()];
		for (int r = 0; r < pa.length; r++) {
			for (int c = 0; c < pa[r].length; c++) {
				if (pa[r][c] < BOOLEAN_DEFAULT_THRESHOLD) {
					if (dfa.holds(r, c) && dfa.holds(c, r) && !twltla.holds(r, c) && r != c) {
						pa[r][c] = BOOLEAN_DEFAULT_THRESHOLD;
						pa[c][r] = BOOLEAN_DEFAULT_THRESHOLD;
					}
				}
			}
		}
		return pa;
	}

	public static strictfp <D extends DirectlyFollowsAbstraction<E>, E> ParallelAbstraction<E> constructBooleanParallelAbstraction(
			D dfa) {
		return new ParallelAbstractionImpl<>(dfa.getEventClasses(), constructBooleanParallelAbstractionMatrix(dfa),
				BOOLEAN_DEFAULT_THRESHOLD);
	}

	public static strictfp double[][] constructBooleanParallelAbstractionMatrix(DirectlyFollowsAbstraction<?> dfa) {
		double[][] pa = new double[dfa.getNumberOfRows()][dfa.getNumberOfColumns()];
		for (int r = 0; r < pa.length; r++) {
			for (int c = 0; c < pa[r].length; c++) {
				if (dfa.getValue(r, c) >= dfa.getThreshold()) {
					if (dfa.getValue(c, r) >= dfa.getThreshold()) {
						pa[r][c] = BOOLEAN_DEFAULT_THRESHOLD;
					}
				}
			}
		}
		return pa;
	}
	
	// NEW FOR ROBUST
	public static strictfp <D extends DirectlyFollowsAbstraction<E>, E> ParallelAbstraction<E> constructAlphaRobustParallelAbstraction(
			D dfa, double noiseThresholdLeastFreq, double noiseThresholdMostFreq,
			double causalThreshold, ActivityCountAbstraction<?> ac) {
		return new ParallelAbstractionImpl<>(dfa.getEventClasses(),
				constructRobustParallelAbstractionMatrix(dfa, noiseThresholdLeastFreq, noiseThresholdMostFreq,
						causalThreshold, ac),
				BOOLEAN_DEFAULT_THRESHOLD);
	}

	public static strictfp double[][] constructRobustParallelAbstractionMatrix(DirectlyFollowsAbstraction<?> dfa,
			double noiseThresholdLeastFreq, double noiseThresholdMostFreq,
			double causalThreshold, ActivityCountAbstraction<?> ac) {
		double[][] pa = new double[dfa.getNumberOfRows()][dfa.getNumberOfColumns()];
		double realNoiseThreshold;
		for (int r = 0; r < pa.length; r++) {
			for (int c = 0; c < pa[r].length; c++) {
				realNoiseThreshold = Math.max(
						noiseThresholdLeastFreq * Math.min(ac.getValue(r), ac.getValue(c)),
						noiseThresholdMostFreq * Math.max(ac.getValue(r), ac.getValue(c))
						);
				if (dfa.getValue(r, c) >= dfa.getThreshold()) {
					if (dfa.getValue(c, r) >= dfa.getThreshold()) {
						if (dfa.getValue(r, c) > realNoiseThreshold || dfa.getValue(c, r) > realNoiseThreshold) {
							if (dfa.getValue(r, c)/dfa.getValue(c, r) < causalThreshold) {
								if (dfa.getValue(c, r)/dfa.getValue(r, c) < causalThreshold) {
									pa[r][c] = BOOLEAN_DEFAULT_THRESHOLD;
								}
							}
						}
					}
				}
			}
		}
		return pa;
	}
	// END
}
