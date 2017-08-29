package de.rwth.i2.attestor.io.jsonExport;

import de.rwth.i2.attestor.graph.SelectorLabel;
import de.rwth.i2.attestor.graph.heap.HeapConfiguration;
import de.rwth.i2.attestor.graph.heap.HeapConfigurationExporter;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.array.TIntArrayList;
import org.json.JSONWriter;

import java.io.Writer;

public class JsonHeapConfigurationExporter implements HeapConfigurationExporter {

    protected Writer writer;

    public JsonHeapConfigurationExporter(Writer writer) {

        this.writer = writer;
    }

    @Override
    public void export(HeapConfiguration heapConfiguration){

        JSONWriter jsonWriter = new JSONWriter(writer);

        jsonWriter.object()
                .key("elements")
                .object()
                .key("nodes")
                .array();

        writeNodes(jsonWriter, heapConfiguration);
        writeNonterminalHyperedges(jsonWriter, heapConfiguration);
        writeVariables(jsonWriter, heapConfiguration);

        jsonWriter.endArray()
                .key("edges")
                .array();

        writeSelectors(jsonWriter, heapConfiguration);
        writeNonterminalTentacles(jsonWriter, heapConfiguration);
        writeVariableTentacles(jsonWriter, heapConfiguration);

        jsonWriter.endArray()
                .endObject()
                .endObject();

    }

    private void writeNodes(JSONWriter jsonWriter, HeapConfiguration heapConfiguration) {

        TIntIterator iter = heapConfiguration.nodes().iterator();
        while(iter.hasNext()) {
            int node = iter.next();
            jsonWriter.object().key("data").object();
            jsonWriter.key("id").value(node);
            jsonWriter.key("type").value("node");
            jsonWriter.key("nodeType").value( heapConfiguration.nodeTypeOf(node).toString() );
            jsonWriter.endObject().endObject();
        }
    }

    public void writeNonterminalHyperedges(JSONWriter jsonWriter, HeapConfiguration heapConfiguration) {

        TIntIterator iter = heapConfiguration.nonterminalEdges().iterator();
        while(iter.hasNext()) {
            int edge= iter.next();
            jsonWriter.object().key("data").object();
            jsonWriter.key("id").value(edge);
            jsonWriter.key("type").value("hyperedge");
            jsonWriter.key("label").value( heapConfiguration.labelOf(edge).toString() );
            jsonWriter.key("annotation").value( heapConfiguration.labelOf(edge).toString()  );
            jsonWriter.endObject().endObject();
        }
    }

    public void writeVariables(JSONWriter jsonWriter, HeapConfiguration heapConfiguration) {

        TIntIterator iter = heapConfiguration.variableEdges().iterator();
        while(iter.hasNext()) {
            int variable = iter.next();
            jsonWriter.object().key("data").object();
            jsonWriter.key("id").value(variable);
            jsonWriter.key("type").value("variable");
            jsonWriter.key("label").value( heapConfiguration.nameOf(variable) );
            jsonWriter.endObject().endObject();
        }
    }

    public void writeSelectors(JSONWriter jsonWriter, HeapConfiguration heapConfiguration) {

        TIntIterator iter = heapConfiguration.nodes().iterator();
        while(iter.hasNext()) {
            int source = iter.next();
            for(SelectorLabel sel : heapConfiguration.selectorLabelsOf(source)) {
                int target = heapConfiguration.selectorTargetOf(source, sel);
                writeEdge(jsonWriter, source, target, sel.toString(), "selector");
            }
        }
    }

    public void writeNonterminalTentacles(JSONWriter jsonWriter, HeapConfiguration heapConfiguration) {

        TIntIterator iter = heapConfiguration.nonterminalEdges().iterator();
        while (iter.hasNext()) {
            int source = iter.next();
            TIntArrayList att = heapConfiguration.attachedNodesOf(source);
            for(int i=0; i < att.size(); i++) {
                int target = att.get(i);
                String label = String.valueOf(i);
                writeEdge(jsonWriter, source, target, label, "tentacle");
            }
        }
    }

    public void writeVariableTentacles(JSONWriter jsonWriter, HeapConfiguration heapConfiguration) {

        TIntIterator iter = heapConfiguration.variableEdges().iterator();
        while(iter.hasNext()) {
            int source = iter.next();
            int target = heapConfiguration.targetOf(source);
            writeEdge(jsonWriter, source, target, "", "variable");
        }
    }

    private void writeEdge(JSONWriter jsonWriter, int source, int target, String label, String type) {

        jsonWriter.object()
                .key("data")
                .object()
                .key("source").value(source)
                .key("target").value(target)
                .key("label").value(label)
                .key("type").value(type)
                .endObject()
                .endObject();
    }
}