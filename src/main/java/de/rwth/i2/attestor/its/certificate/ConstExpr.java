package de.rwth.i2.attestor.its.certificate;

import org.eclipse.persistence.oxm.annotations.XmlValueExtension;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.bind.annotation.*;
import java.util.Collections;
import java.util.Set;

@XmlRootElement(name = "constant")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConstExpr extends Expression {

    @XmlValueExtension
    @XmlValue
    private final int constant;

    private ConstExpr() {
        this.constant = -1;
    }

    public ConstExpr(int constant) {
        this.constant = constant;
    }

    @Override
    public String toString() {
        return String.valueOf(constant);
    }


    @Override
    public Set<String> getOccurringVariables() {
        return Collections.emptySet();
    }
}
