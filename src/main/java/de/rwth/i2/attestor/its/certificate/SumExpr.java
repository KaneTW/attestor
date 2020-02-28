package de.rwth.i2.attestor.its.certificate;


import javax.xml.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@XmlRootElement(name = "sum")
@XmlAccessorType(XmlAccessType.FIELD)
public class SumExpr extends Expression {
    @XmlElementRef
    private final List<Expression> operands;

    private SumExpr() {
        this.operands = null;
    }


    public SumExpr(List<Expression> operands) {
        this.operands = operands;
    }

    @Override
    public String toString() {
        return "(" + operands.stream().map(Expression::toString).collect(Collectors.joining(" + ")) + ")";
    }

    public List<Expression> getOperands() {
        return operands;
    }
}
