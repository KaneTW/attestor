package de.rwth.i2.attestor.its.certificate;


import org.eclipse.persistence.oxm.annotations.XmlValueExtension;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LocationDuplicate extends Location {
    @XmlValue
    @XmlValueExtension
    private final String location;

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public boolean isDuplicate() {
        return true;
    }

    @Override
    public String toString() {
        return "sharp location: " + location;
    }

    private LocationDuplicate() {
        this.location = null;
    }

    public LocationDuplicate(String location) {
        this.location = location;
    }
}
