package de.rwth.i2.attestor.its.certificate;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;


@XmlSeeAlso({
        ConstExpr.class,
        VarExpr.class,
        SumExpr.class,
        ProductExpr.class
})
public abstract class Expression {

    public boolean isConstant() {
        if (this instanceof ConstExpr) {
            return true;
        }

        boolean isConst = true;
        if (this instanceof SumExpr) {
            SumExpr sum = (SumExpr) this;
            for (Expression op :  sum.getOperands()) {
                if (!(op instanceof ConstExpr)) {
                    isConst = false;
                }
            }
        }

        if (this instanceof ProductExpr) {
            ProductExpr product = (ProductExpr) this;
            for (Expression op : product.getOperands()) {
                if (!(op instanceof ConstExpr)) {
                    isConst = false;
                }
            }
        }

        return isConst;
    }

}
