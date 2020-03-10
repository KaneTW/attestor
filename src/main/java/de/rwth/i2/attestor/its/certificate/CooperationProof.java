package de.rwth.i2.attestor.its.certificate;

import com.google.common.graph.*;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

@XmlSeeAlso({
        TransitionRemovalProof.class,
        SccDecompositionProof.class,
        LocationAdditionProof.class,
        NewInvariantsProof.class,
        FreshVariableAdditionProof.class
})
public abstract class CooperationProof {

}
