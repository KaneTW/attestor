package de.rwth.i2.attestor.its.certificate;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ConstExpr implements Expression {
    private final int constant;

    public ConstExpr(int constant) {
        this.constant = constant;
    }

    @Override
    public String toString() {
        return String.valueOf(constant);
    }


    public static ConstExpr readConstExpr(Element element) {
        if (element.getTagName() != "constant") {
            throw new IllegalArgumentException("Invalid tag name");
        }

        String constStr = element.getTextContent();
        return new ConstExpr(Integer.parseInt(constStr));
    }
}
