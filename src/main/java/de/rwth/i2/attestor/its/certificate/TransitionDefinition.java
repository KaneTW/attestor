package de.rwth.i2.attestor.its.certificate;

import de.rwth.i2.attestor.util.SingleElementUtil;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

public class TransitionDefinition {
    @XmlElementRef
    private final Transition transition;

    @XmlElementWrapper
    @XmlElementRef
    private final List<Location> source;

    @XmlElementWrapper
    @XmlElementRef
    private final List<Location> target;

    @XmlElement
    private final Object formula;

    private TransitionDefinition() {
        this.transition = null;
        this.source = null;
        this.target = null;
        this.formula = null;
    }

    public TransitionDefinition(Transition transition, Location source, Location target) {
        this.transition = transition;
        this.source = SingleElementUtil.createList(source);
        this.target = SingleElementUtil.createList(target);
        this.formula = null;
    }

    public Transition getTransition() {
        return transition;
    }

    public Location getSource() {
        return source.get(0);
    }

    public Location getTarget() {
        return target.get(0);
    }

    @Override
    public String toString() {
        return getTransition() + " from " + getSource() + " to " + getTarget();
    }
}
