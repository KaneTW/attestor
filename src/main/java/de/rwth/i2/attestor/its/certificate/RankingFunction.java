package de.rwth.i2.attestor.its.certificate;


import org.eclipse.persistence.oxm.annotations.XmlPath;
import org.w3c.dom.Element;
import soot.jimple.Expr;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "rankingFunction")
@XmlAccessorType(XmlAccessType.FIELD)
public class RankingFunction {
    // workaround for wrapping requirement
    @XmlElementWrapper
    @XmlElementRef
    private final List<Location> location;

    @XmlElementWrapper
    @XmlElementRef
    private final List<Expression> expression;


    private RankingFunction() {
        this.location = null;
        this.expression = null;
    }

    public RankingFunction(Location location, Expression expression) {
        this.location = new ArrayList<Location>();
        this.location.add(location);

        this.expression = new ArrayList<Expression>();
        this.expression.add(expression);
    }

    public Location getLocation() {
        return location.get(0);
    }

    public Expression getExpression() {
        return expression.get(0);
    }

    @Override
    public String toString() {
        return "rank for " + getLocation() + ": " + getExpression();
    }
}
