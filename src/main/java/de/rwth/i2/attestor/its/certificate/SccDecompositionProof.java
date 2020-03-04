package de.rwth.i2.attestor.its.certificate;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "sccDecomposition")
public class SccDecompositionProof extends CooperationProof {

    @XmlElement(name = "sccWithProof")
    private final List<SccWithProof> containedSccs;

    private SccDecompositionProof() {
        this.containedSccs = null;
    }

    public List<SccWithProof> getContainedSccs() {
        return containedSccs;
    }
}
