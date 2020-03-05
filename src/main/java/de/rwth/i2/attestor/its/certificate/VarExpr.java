package de.rwth.i2.attestor.its.certificate;

import org.eclipse.persistence.oxm.annotations.XmlValueExtension;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "variableId")
@XmlAccessorType(XmlAccessType.FIELD)
public class VarExpr extends Expression {

    @XmlValue
    @XmlValueExtension
    private final String variableId;

    private VarExpr() {
        variableId = null;
    }

    public VarExpr(String variable) {
        this.variableId = variable;
    }

    @Override
    public String toString() {
        return variableId;
    }
}
