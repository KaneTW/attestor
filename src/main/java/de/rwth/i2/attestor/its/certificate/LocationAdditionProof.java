package de.rwth.i2.attestor.its.certificate;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "locationAddition")
public class LocationAdditionProof extends CooperationProof {

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
}
