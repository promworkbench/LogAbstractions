package org.processmining.plugins.log.logabstraction;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.model.XLog;
import org.processmining.framework.connections.annotations.ConnectionObjectFactory;
import org.processmining.framework.connections.impl.AbstractConnection;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;

/**
 * Connect a log to its log abstraction
 * @author michael
 *
 */
@Plugin(name = "Log Abstraction Connection Factory", parameterLabels = { "Log", "Log Abstraction" }, returnTypes = LogAbstractionConnection.class, returnLabels = "Log abstraction connection", userAccessible = false)
@ConnectionObjectFactory
public class LogAbstractionConnection extends AbstractConnection {
	public static final String LOG = "Log";
	public static final String RELATIONS = "LogRelations";

	/**
	 * @param log
	 * @param relations
	 */
	public LogAbstractionConnection(XLog log, LogRelations relations) {
		super("Connection from " + XConceptExtension.instance().extractName(log) + " to " + relations);
		put(LOG, log);
		put(RELATIONS, relations);
	}
	
	/**
	 * @return
	 */
	public XLog getLog() {
		return getObjectWithRole(LOG);
	}
	
	/**
	 * @return
	 */
	public LogRelations getRelations() {
		return getObjectWithRole(RELATIONS);
	}

	@PluginVariant(requiredParameterLabels = { 0, 1 })
	public static LogAbstractionConnection getConnection(PluginContext context, XLog log, LogRelations relations) {
		return new LogAbstractionConnection(log, relations);
	}

}
