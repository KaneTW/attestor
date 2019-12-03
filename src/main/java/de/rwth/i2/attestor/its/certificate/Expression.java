package de.rwth.i2.attestor.its.certificate;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public interface Expression {

    static Expression readTopLevelExpression(Element element) {
        if (element.getTagName() != "expression") {
            throw new IllegalArgumentException("Invalid tag name");
        }

        Element child = (Element) element.getFirstChild();
        return readExpression(child);
    }
    static Expression readExpression(Element child) {
        if (child.getTagName() == "sum") {
            return SumExpr.readSumExpr(child);
        } else if (child.getTagName() == "product") {
            return ProductExpr.readProductExpr(child);
        } else if (child.getTagName() == "constant") {
            return ConstExpr.readConstExpr(child);
        } else if (child.getTagName() == "variable") {
            throw new UnsupportedOperationException("Variable expressions not supported yet (since unused in T2)");
        } else {
            throw new IllegalArgumentException("Unknown tag name " + child.getTagName());
        }
    }

}
