package de.rwth.i2.attestor.its.certificate;

import com.google.common.graph.Graphs;
import com.google.common.graph.Network;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.stream.Collectors;

@XmlRootElement(name="sccWithProof")
public class SccWithProof implements HasGraph {

    @XmlElementWrapper(name="scc")
    @XmlElementRef
    private final List<Location> sccs;

    @XmlElementRef
    private final CooperationProof nextProof;

    private SccWithProof() {
        this.sccs = null;
        this.nextProof = null;
    }

    public List<Location> getSccs() {
        return sccs;
    }

    public CooperationProof getNextProof() {
        return nextProof;
    }

    @Override
    public Network<String, Integer> getCurrentGraph(Network<String, Integer> previous) {
        return Graphs.inducedSubgraph(previous, sccs.stream().map(Location::getLocation).collect(Collectors.toList()));
    }
}
