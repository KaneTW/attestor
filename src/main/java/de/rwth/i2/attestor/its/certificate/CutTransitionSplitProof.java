package de.rwth.i2.attestor.its.certificate;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "cutTransitionSplit")
public class CutTransitionSplitProof extends CooperationProof {
    @XmlElement(name = "cutTransitionsWithProof")
    private final List<CutTransitionsWithProof> containedCuts;

    private CutTransitionSplitProof() {
        this.containedCuts = null;
    }

    public List<CutTransitionsWithProof> getContainedCuts() {
        return containedCuts;
    }

}
