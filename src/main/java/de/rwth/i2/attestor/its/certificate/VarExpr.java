package de.rwth.i2.attestor.its.certificate;

import org.w3c.dom.Element;

public class VarExpr implements Expression {
    private final String variable;

    public VarExpr(String variable) {
        this.variable = variable;
    }

    @Override
    public String toString() {
        return variable;
    }

    public static VarExpr readVarExpr(Element element) {
        if (element.getTagName() != "variableId") {
            throw new IllegalArgumentException("Invalid tag name");
        }

        return new VarExpr(element.getTextContent());

    }
}
