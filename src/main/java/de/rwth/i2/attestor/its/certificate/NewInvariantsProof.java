package de.rwth.i2.attestor.its.certificate;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "newInvariants")
public class NewInvariantsProof extends CooperationProof {


    @XmlElementRef(required = false)
    private final CooperationProof nextProof;

    private NewInvariantsProof() {
        this.nextProof = null;
    }

    public CooperationProof getNextProof() {
        return nextProof;
    }
}
