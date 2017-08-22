package de.rwth.i2.attestor.io.jsonExport;

import de.rwth.i2.attestor.grammar.Grammar;
import de.rwth.i2.attestor.grammar.GrammarExporter;

import java.io.File;
import java.io.FileWriter;

import de.rwth.i2.attestor.graph.Nonterminal;
import de.rwth.i2.attestor.graph.heap.HeapConfiguration;
import de.rwth.i2.attestor.graph.heap.HeapConfigurationExporter;
import org.json.JSONWriter;
import java.io.IOException;
import java.io.Writer;

import de.rwth.i2.attestor.util.FileUtils;

/**
 * Created by christina on 18.08.17.
 */
public class JsonGrammarExporter implements GrammarExporter {

    @Override
    public void export(String directory, Grammar grammar) throws IOException {

        FileUtils.createDirectories(directory);
        FileWriter writer = new FileWriter(directory + File.separator + "grammarExport.json");
        exportGrammar(writer, grammar);
        writer.close();

        for(Nonterminal nt : grammar.getAllLeftHandSides()){
            int count = 1;
            for(HeapConfiguration hc : grammar.getRightHandSidesFor(nt)){
                exportHeapConfiguration(directory + File.separator + nt.getLabel() + "Rule" + count + ".json",
                        hc);
                count++;
            }

        }



    }

    private void exportGrammar(Writer writer, Grammar grammar) {

        JSONWriter jsonWriter = new JSONWriter(writer);

        jsonWriter.array();
        for(Nonterminal nonterminal : grammar.getAllLeftHandSides()) {

            String nonterminalName = nonterminal.toString();
            int ruleNumber = grammar.getRightHandSidesFor(nonterminal).size();


            jsonWriter.object()
                    .key("nonterminal").value(nonterminalName)
                    .key("numberRules").value(ruleNumber)
                    .key("rules").array();
            for (int count = 1; count <= ruleNumber; count++) {

                jsonWriter.value("rule" + count);

            }
            jsonWriter.endArray();
            jsonWriter.endObject();
        }
        jsonWriter.endArray();

    }

    private void exportHeapConfiguration(String filename, HeapConfiguration hc)
            throws IOException {

        FileWriter writer = new FileWriter(filename);
        HeapConfigurationExporter exporter = new JsonExtendedHeapConfigurationExporter(writer);
        exporter.export(hc);
        writer.close();
    }

}
