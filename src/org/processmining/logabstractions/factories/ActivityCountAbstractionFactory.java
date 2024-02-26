package org.processmining.logabstractions.factories;

import org.processmining.logabstractions.models.ActivityCountAbstraction;
import org.processmining.logabstractions.models.implementations.ActivityCountAbstractionImpl;

public class ActivityCountAbstractionFactory {

	public static final double DEFAULT_THRESHOLD_ABSOLUTE = 1.0d;
	public static final double DEFAULT_THRESHOLD_BOOLEAN = 1.0d;


	public static strictfp <E> ActivityCountAbstraction<E> constructActivityCountAbstraction(E[] eventClasses,
			double[] column, double threshold) {
		return new ActivityCountAbstractionImpl<>(eventClasses, column, threshold);
	}

}
