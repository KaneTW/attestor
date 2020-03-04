package de.rwth.i2.attestor.its.certificate;


import org.eclipse.persistence.oxm.annotations.XmlValueExtension;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TransitionId  extends Transition {
    @XmlValue
    @XmlValueExtension
    private final int transition;

    @Override
    public int getTransition() {
        return transition;
    }

    @Override
    public boolean isDuplicate() {
        return false;
    }

    @Override
    public String toString() {
        return "flat transition: " + transition;
    }

    private TransitionId() {
        this.transition = -1;
    }

    public TransitionId(int transitionId) {
        this.transition = transitionId;
    }
}
