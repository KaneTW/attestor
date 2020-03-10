package de.rwth.i2.attestor.its.certificate;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@XmlRootElement(name = "product")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductExpr extends Expression {
    @XmlElementRef
    private final List<Expression> operands;

    private ProductExpr() {
        this.operands = null;
    }

    public ProductExpr(List<Expression> operands) {
        this.operands = operands;
    }

    @Override
    public String toString() {
        return "(" + operands.stream().map(Expression::toString).collect(Collectors.joining(" * ")) + ")";
    }


    public List<Expression> getOperands() {
        return operands;
    }

    @Override
    public Set<String> getOccurringVariables() {
        return operands.stream().flatMap(expression -> expression.getOccurringVariables().stream()).collect(Collectors.toSet());
    }
}

