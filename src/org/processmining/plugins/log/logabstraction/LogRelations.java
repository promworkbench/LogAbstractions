package org.processmining.plugins.log.logabstraction;

import java.util.Map;
import java.util.Set;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.framework.util.Pair;

public interface LogRelations extends EventRelations<XLog> {
	/**
	 * Returns the log on which these abstractions are based.
	 *
	 * @return the log on which these abstractions are based.
	 */
	@Deprecated // -> use getSource();
	XLog getLog();

	/**
	 * Returns a map from pairs of event classes to double, such that if a pair
	 * (x,y) of two event classes x and y are in the map, the double value
	 * attached to this pair indicated the strength of a causal dependency from
	 * x to y. The range and meaning of the value depends on the implementing
	 * class!
	 *
	 * @return a map from pairs of event classes to doubles
	 */
	Map<Pair<XEventClass, XEventClass>, Double> causalDependencies();

	/**
	 * Tells for each event class how often it appears at the startEvents of a
	 * trace in the log, if any, i.e. the returned integer is always greater
	 * than 0.
	 *
	 * @return the number of times each event class appears at the startEvents
	 *         of a trace.
	 */
	@Deprecated // -> use getStartEvents();
	Map<XEventClass, Integer> getStartTraceInfo();

	/**
	 * Tells for each event class how often it appears at the endEvents of a
	 * trace in the log, if any, i.e. the returned integer is always greater
	 * than 0.
	 *
	 * @return the number of times each event class appears at the endEvents of
	 *         a trace.
	 */
	@Deprecated // -> use getEndEvents();
	Map<XEventClass, Integer> getEndTraceInfo();

	/**
	 * Returns the summary of the log, corresponding to the getEventClasses
	 * method
	 *
	 * @return the summary
	 */
	XLogInfo getSummary();

	@Deprecated
	Map<Pair<XEventClass, XEventClass>, Set<XTrace>> getCountDirect();

}
