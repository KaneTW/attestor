package de.rwth.i2.attestor.its.certificate;

import com.google.common.graph.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@XmlRootElement(name = "locationAddition")
public class LocationAdditionProof extends CooperationProof implements HasGraph {

    @XmlElement
    private final TransitionDefinition transition;

    @XmlElementRef(required = false)
    private final CooperationProof nextProof;

    private LocationAdditionProof() {
        this.transition = null;
        this.nextProof = null;
    }

    public TransitionDefinition getTransition() {
        return transition;
    }

    public CooperationProof getNextProof() {
        return nextProof;
    }

    @Override
    public Network<String, Integer> getCurrentGraph(Network<String, Integer> previous) {
        MutableNetwork<String, Integer> graph = Graphs.copyOf(previous);

        String source = transition.getSource().getLocation();
        String target = transition.getTarget().getLocation();

        // logic taken straight from CeTA's Isabelle code
        if (!graph.nodes().contains(source)) {
            // if the source is new, change all transitions targetting target to the new location
            for (Integer edge : previous.inEdges(target)) { //nb: make sure to iterate over previous here, to avoid concurrent modification
                EndpointPair<String> pair = graph.incidentNodes(edge);
                graph.removeEdge(edge);
                graph.addEdge(pair.nodeU(), source, edge);
            }
        } else {
            // otherwise the target is new, so change the source
            for (Integer edge : previous.outEdges(source)) {
                EndpointPair<String> pair = graph.incidentNodes(edge);
                graph.removeEdge(edge);
                graph.addEdge(target, pair.nodeV(), edge);
            }
        }

        graph.addEdge(source, target, transition.getTransition().getTransition());
        return graph;
    }
}
