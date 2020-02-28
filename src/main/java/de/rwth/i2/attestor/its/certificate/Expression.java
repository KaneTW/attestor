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

}
