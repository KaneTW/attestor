package de.rwth.i2.attestor.its.certificate;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "freshVariableAddition")
public class FreshVariableAdditionProof extends CooperationProof {
    @XmlElementRef(required = false)
    private final CooperationProof nextProof;

    private FreshVariableAdditionProof() {
        this.nextProof = null;
    }

    public CooperationProof getNextProof() {
        return nextProof;
    }
}
