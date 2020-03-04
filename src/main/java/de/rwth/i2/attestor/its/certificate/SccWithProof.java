package de.rwth.i2.attestor.its.certificate;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="sccWithProof")
public class SccWithProof {

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
}
