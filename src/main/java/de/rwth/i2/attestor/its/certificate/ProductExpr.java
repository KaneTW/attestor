package de.rwth.i2.attestor.its.certificate;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductExpr implements Expression {
    private final List<Expression> operands;

    public ProductExpr(List<Expression> operands) {
        this.operands = operands;
    }

    @Override
    public String toString() {
        return "(" + operands.stream().map(Expression::toString).collect(Collectors.joining(" * ")) + ")";
    }

    public static ProductExpr readProductExpr(Element element) {
        if (element.getTagName() != "product") {
            throw new IllegalArgumentException("Invalid tag name");
        }

        NodeList children = element.getChildNodes();
        List<Expression> expressions = new ArrayList<>();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child instanceof Element) {
                expressions.add(Expression.readExpression((Element) child));
            }
        }

        return new ProductExpr(expressions);
    }
}
