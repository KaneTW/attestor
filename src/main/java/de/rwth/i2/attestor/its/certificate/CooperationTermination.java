package de.rwth.i2.attestor.its.certificate;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "switchToCooperationTermination")
public class CooperationTermination {
    @XmlElementWrapper(name="cutPoints")
    private final List<Object> cutPoints;

    @XmlElementRef
    private final CooperationProof proof;

    private CooperationTermination() {
        this.cutPoints = null;
        this.proof = null;
    }

    public List<Object> getCutPoints() {
        return cutPoints;
    }

    public CooperationProof getProof() {
        return proof;
    }
}
