package de.rwth.i2.attestor.its.certificate;


import org.eclipse.persistence.oxm.annotations.XmlValueExtension;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TransitionDuplicate extends Transition {
    @XmlValue
    @XmlValueExtension
    private final int transition;

    @Override
    public int getTransition() {
        return transition;
    }

    @Override
    public boolean isDuplicate() {
        return true;
    }

    @Override
    public String toString() {
        return "sharp transition: " + transition;
    }


    private TransitionDuplicate() {
        this.transition = -1;
    }

    public TransitionDuplicate(int transitionId) {
        this.transition = transitionId;
    }
}
