package de.rwth.i2.attestor.its.certificate;

import javax.xml.bind.annotation.XmlSeeAlso;

@XmlSeeAlso({
        TransitionRemovalProof.class,
        SccDecompositionProof.class,
        LocationAdditionProof.class
})
public abstract class CooperationProof {
}
