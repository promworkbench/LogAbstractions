package org.processmining.plugins.log.logabstraction;

import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.model.XLog;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;

@Plugin(name = "Construct Log Relations", parameterLabels = {"Log", "Summary"}, returnLabels = "Log Relations", returnTypes = LogRelations.class, userAccessible = false)
public class LogRelationProvider {

    @PluginVariant(variantLabel = "Default Summary", requiredParameterLabels = {0})
    public LogRelations constructBasicLogAbstraction(PluginContext context, XLog log) {
        context.getProgress().setIndeterminate(false);
        LogRelations relations = new BasicLogRelations(log, context.getProgress());
        context.addConnection(new LogAbstractionConnection(log, relations));
        return relations;
    }

    @PluginVariant(variantLabel = "Given Summary", requiredParameterLabels = {0, 1})
    public LogRelations constructBasicLogAbstraction(PluginContext context, XLog log, XLogInfo summary) {
        context.getProgress().setIndeterminate(false);
        LogRelations relations = new BasicLogRelations(log, summary, context.getProgress());
        context.addConnection(new LogAbstractionConnection(log, relations));
        return relations;
    }

}
