package de.rwth.i2.attestor.its.certificate;

import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class LTS {
    @XmlElementWrapper
    @XmlElementRef
    private final List<Location> initial;

    @XmlElement(name="transition")
    private final List<TransitionDefinition> transitions;

    private LTS() {
        this.initial = null;
        this.transitions = null;
    }

    public LTS(List<Location> initial, List<TransitionDefinition> transitions) {
        this.initial = initial;
        this.transitions = transitions;
    }

    public List<Location> getInitial() {
        return initial;
    }

    public List<TransitionDefinition> getTransitions() {
        return transitions;
    }

    public MutableNetwork<String, Integer> asMutableNetwork() {
        MutableNetwork<String, Integer> graph = NetworkBuilder
                .directed()
                .allowsParallelEdges(true)
                .allowsSelfLoops(true)
                .build();
        for (TransitionDefinition td : transitions) {
            graph.addEdge( td.getSource().getLocation()
                    , td.getTarget().getLocation()
                    , td.getTransition().getTransition() );
        }

        return graph;
    }
}
