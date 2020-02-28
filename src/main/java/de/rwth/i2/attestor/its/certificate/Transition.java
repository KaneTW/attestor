package de.rwth.i2.attestor.its.certificate;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

@XmlSeeAlso({TransitionId.class, TransitionDuplicate.class})
public abstract class Transition {
    public abstract int getTransition();
    public abstract boolean isDuplicate();
}
