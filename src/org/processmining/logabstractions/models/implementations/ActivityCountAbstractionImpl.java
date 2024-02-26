package org.processmining.logabstractions.models.implementations;

import org.processmining.logabstractions.models.ActivityCountAbstraction;
import org.processmining.logabstractions.models.abstr.AbstractColumnAbstraction;

public class ActivityCountAbstractionImpl<E> extends AbstractColumnAbstraction<E>
		implements ActivityCountAbstraction<E> {

	public ActivityCountAbstractionImpl(E[] eventClasses, double[] column, double threshold) {
		super(eventClasses, column, threshold);
	}

}
