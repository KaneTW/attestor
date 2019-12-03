package de.rwth.i2.attestor.its.certificate;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Location {
    private final String location;
    private final boolean duplicate;

    public Location(String location, boolean duplicate) {
        this.location = location;
        this.duplicate = duplicate;
    }

    public static Location readLocation(Element element) {
        if (element.getTagName() != "location") {
            throw new IllegalArgumentException("Invalid tag name");
        }

        NodeList locationIds = element.getElementsByTagName("locationId");
        if (locationIds.getLength() > 0) {
            return new Location(locationIds.item(0).getTextContent(), false);
        } else {
            Node duplicate = element.getElementsByTagName("locationDuplicate").item(0);
            return new Location(duplicate.getTextContent(), true);
        }
    }
}
