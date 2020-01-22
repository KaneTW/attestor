package de.rwth.i2.attestor.its.certificate;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

class XMLUtils {
    public static Element getElementChild(Element parent) {
        for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child instanceof Element) {
                return (Element)child;
            }
        }
        return null;
    }
}
