package de.rwth.i2.attestor.its.certificate;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SumExpr implements Expression {
    private final List<Expression> operands;

    public SumExpr(List<Expression> operands) {
        this.operands = operands;
    }

    @Override
    public String toString() {
        return "(" + operands.stream().map(Expression::toString).collect(Collectors.joining(" + ")) + ")";
    }

    public static SumExpr readSumExpr(Element element) {
        if (element.getTagName() != "sum") {
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

        return new SumExpr(expressions);
    }
}
