package org.processmining.plugins.log.logabstraction;

import java.util.List;
import java.util.Map;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventClasses;
import org.processmining.framework.util.Pair;

/**
 * Signifies relations amongst a set of events originating from some source T
 * 
 */
public interface EventRelations<T> {

	T getSource();

	/**
	 * Returns the event classes defined in the log and used in the relations
	 *
	 * @return event classes;
	 */
	XEventClasses getEventClasses();

	/**
	 * Returns a matrix with absolute dfr-frequencies. The first element of the
	 * pair is a List of XEventClasses. The index in the list corresponds to the
	 * index in the matrix. The matrix should be read as
	 * int[fromIndices][toIndices]
	 *
	 * @return pair of eventclass mappings and absolute dfr value-matrix
	 */
	Pair<List<XEventClass>, int[][]> absoluteDirectlyFollowsMatrix();

	/**
	 * Returns a causal matrix with some causal measure. The first element of
	 * the pair is a List of XEvent The first element of the pair is a List of
	 * XEventClasses. The index in the list corresponds to the index in the
	 * matrix.
	 *
	 * @return a causal matrix
	 */
	Pair<List<XEventClass>, double[][]> causalMatrix();

	/**
	 * Returns a map from pairs of event classes to double, such that if a pair
	 * (x,y) of two event classes x and y are in the map, and the double value
	 * attached to this pair is greater than 0, then a causal dependency from x
	 * to y exists.
	 * <p/>
	 * The double value indicates the strength of the causal dependency and
	 * should be between 0 and 1 (inclusive).
	 *
	 * @return a map from pairs of event classes to doubles
	 */
	Map<Pair<XEventClass, XEventClass>, Double> getCausalDependencies();

	/**
	 * Returns a map from pairs of event classes to integers, such that if a
	 * pair (x,y) of two event classes x and y are in the map, and the integer
	 * value attached to this pair is greater than 0, then a dfrPairs follows
	 * dependency from x to y exists.
	 * <p/>
	 * The integer value indicates how often the first event is directly
	 * followed by the second event.
	 *
	 * @return a map from pairs of event classes to integers
	 */
	Map<Pair<XEventClass, XEventClass>, Integer> getDirectFollowsDependencies();

	/**
	 * Returns a map from pairs of event classes to double, such that if a pair
	 * (x,y) of two event classes x and y are in the map, and the double value
	 * attached to this pair is greater than 0, then a parallel relation between
	 * x and y exists.
	 * <p/>
	 * It can be assumed that the double value attached to (x,y) equals the
	 * value attached to (y,x).
	 * <p/>
	 * The double value indicates the strength of the parallel relation and
	 * should be between 0 and 1 (inclusive).
	 *
	 * @return a map from pairs of event classes to doubles
	 */
	Map<Pair<XEventClass, XEventClass>, Double> getParallelRelations();

	/**
	 * Tells for each event class how often it appears at the startEvents of a
	 * trace in the log, if any, i.e. the returned integer is always greater
	 * than 0.
	 *
	 * @return the number of times each event class appears at the startEvents
	 *         of a trace.
	 */
	Map<XEventClass, Integer> getStartEvents();

	/**
	 * Tells for each event class how often it appears at the endEvents of a
	 * trace in the log, if any, i.e. the returned integer is always greater
	 * than 0.
	 *
	 * @return the number of times each event class appears at the endEvents of
	 *         a trace.
	 */
	Map<XEventClass, Integer> getEndEvents();

	/**
	 * Returns a map from self-loop event classes to integers.
	 *
	 * @return a map from self-loop event classes to integers.
	 */
	Map<XEventClass, Integer> getLengthOneLoops();

	/**
	 * Returns a map from self-loop event classes to doubles.
	 *
	 * @return a map from self-loop event classes to doubles.
	 */
	Map<XEventClass, Double> lengthOneLoops();

	/**
	 * Returns a map from pairs of event classes to integers for which it is
	 * known that they are in length-two loop situations.
	 *
	 * @return a map from pairs of event classes to integers
	 */
	Map<Pair<XEventClass, XEventClass>, Integer> getLengthTwoLoops();

	/**
	 * Returns a map from pairs of event classes to doubles for which it is
	 * known that they are in length-two loop situations.
	 *
	 * @return a map from pairs of event classes to doubles
	 */
	Map<Pair<XEventClass, XEventClass>, Double> lengthTwoLoops();
}
