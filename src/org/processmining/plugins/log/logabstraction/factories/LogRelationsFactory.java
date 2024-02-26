package org.processmining.plugins.log.logabstraction.factories;

import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.model.XLog;
import org.processmining.framework.plugin.Progress;
import org.processmining.plugins.log.logabstraction.LogRelations;
import org.processmining.plugins.log.logabstraction.implementations.AlphaLogRelationsImpl;
import org.processmining.plugins.log.logabstraction.implementations.HeuristicsLogRelationsImpl;

public class LogRelationsFactory {

	public static LogRelations constructAlphaLogRelations(XLog log) {
		return new AlphaLogRelationsImpl(log);
	}
	
	@Deprecated // -> TYPO
	public static LogRelations constructAlphabLogRelations(XLog log) {
		return new AlphaLogRelationsImpl(log);
	}

	public static LogRelations constructAlphaLogRelations(XLog log, XLogInfo summary) {
		return new AlphaLogRelationsImpl(log, summary);
	}

	public static LogRelations constructAlphaLogRelations(XLog log, Progress progress) {
		return new AlphaLogRelationsImpl(log, progress);
	}

	public static LogRelations constructAlphaLogRelations(XLog log, XLogInfo summary, Progress progress) {
		return new AlphaLogRelationsImpl(log, summary, progress);
	}

	public static LogRelations constructHeuristicsLogRelations(XLog log) {
		return new HeuristicsLogRelationsImpl(log);
	}

	public static LogRelations constructHeuristicsLogRelations(XLog log, XLogInfo summary) {
		return new HeuristicsLogRelationsImpl(log, summary);
	}

	public static LogRelations constructHeuristicsLogRelations(XLog log, Progress progress) {
		return new HeuristicsLogRelationsImpl(log, progress);
	}

	public static LogRelations constructHeuristicsLogRelations(XLog log, XLogInfo summary, Progress progress) {
		return new HeuristicsLogRelationsImpl(log, summary, progress);
	}

}
