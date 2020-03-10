package de.rwth.i2.attestor.its.certificate;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

public class CutTransitionsWithProof {
    @XmlElementWrapper(name="cutTransitions")
    @XmlElementRef
    private final List<Transition> cutTransitions;

    @XmlElementRef
    private final CooperationProof nextProof;

    private CutTransitionsWithProof() {
        this.cutTransitions = null;
        this.nextProof = null;
    }

    public List<Transition> getCutTransitions() {
        return cutTransitions;
    }

    public CooperationProof getNextProof() {
        return nextProof;
    }
}
