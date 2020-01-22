package de.rwth.i2.attestor.its.certificate;


import org.w3c.dom.Element;

public class RankingFunction {
    private final Location location;
    private final Expression expression;

    public RankingFunction(Location location, Expression expression) {
        this.location = location;
        this.expression = expression;
    }

    public static RankingFunction readRankingFunction(Element element) {
        if (element.getTagName() != "rankingFunction") {
            throw new IllegalArgumentException("Invalid tag name");
        }

        Location location = Location.readLocation((Element) element.getElementsByTagName("location").item(0));
        Expression expression = Expression.readTopLevelExpression((Element) element.getElementsByTagName("expression").item(0));

        return new RankingFunction(location, expression);
    }
}
