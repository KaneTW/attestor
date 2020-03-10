package de.rwth.i2.attestor.its.certificate;

import com.google.common.graph.Graphs;
import com.google.common.graph.MutableNetwork;
import com.google.common.graph.Network;
import com.google.common.graph.NetworkBuilder;
import org.eclipse.persistence.oxm.annotations.XmlPath;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name="transitionRemoval")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransitionRemovalProof extends CooperationProof implements HasGraph {
    @XmlElementWrapper(required = true)
    @XmlElementRef
    private final List<RankingFunction> rankingFunctions;

    @XmlPath("bound/constant")
    private final ConstExpr bound;

    @XmlElementWrapper(name="remove")
    @XmlElementRef
    private final List<Transition> remove;

    @XmlElement
    private final List<Object> hints;

    @XmlElementRef(required = false)
    private final CooperationProof nextProof;

    private TransitionRemovalProof() {
        this.rankingFunctions = null;
        this.bound = null;
        this.remove = null;
        this.nextProof = null;
        this.hints = null;
    }

    public TransitionRemovalProof(List<RankingFunction> rankingFunctions, ConstExpr bound, List<Transition> removed, CooperationProof nextProof) {
        this.rankingFunctions = rankingFunctions;
        this.bound = bound;
        this.remove = removed;
        this.nextProof = nextProof;
        this.hints = null;
    }

    public List<RankingFunction> getRankingFunctions() {
        return rankingFunctions;
    }

    public ConstExpr getBound() {
        return bound;
    }

    public List<Transition> getRemove() {
        return remove;
    }

    public CooperationProof getNextProof() {
        return nextProof;
    }

    @Override
    public Network<String, Integer> getCurrentGraph(Network<String, Integer> previous) {
        MutableNetwork<String, Integer> graph = Graphs.copyOf(previous);

        remove.stream().forEach(transition -> graph.removeEdge(transition.getTransition()));

        return graph;
    }
}
