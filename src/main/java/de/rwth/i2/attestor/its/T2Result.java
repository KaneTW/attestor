package de.rwth.i2.attestor.its;

import de.rwth.i2.attestor.its.certificate.Expression;
import de.rwth.i2.attestor.its.certificate.RankingFunction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class T2Result {

    private T2Status status;
    private Path outputDirectory;
    private Document certificate;

    public T2Result(T2Status status, Path outputDirectory, Document certificate) {
        this.status = status;
        this.outputDirectory = outputDirectory;
        this.certificate = certificate;
    }

    public T2Status getStatus() {
        return status;
    }

    public Path getOutputDirectory() {
        return outputDirectory;
    }

    public Document getCertificate() {
        return certificate;
    }

    public List<RankingFunction> getRankingFunctions() {
        NodeList nodes = certificate.getElementsByTagName("rankingFunction");
        ArrayList<RankingFunction> rankingFunctions = new ArrayList<RankingFunction>();

        for (int i = 0; i < nodes.getLength(); i++) {
            Element element = (Element) nodes.item(i);
            rankingFunctions.add(RankingFunction.readRankingFunction(element));
        }

        return rankingFunctions;
    }
}
