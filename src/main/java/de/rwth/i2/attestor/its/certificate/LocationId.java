package de.rwth.i2.attestor.its.certificate;


import org.eclipse.persistence.oxm.annotations.XmlValueExtension;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LocationId extends Location {
    @XmlValue
    @XmlValueExtension
    private final String location;

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public boolean isDuplicate() {
        return false;
    }

    @Override
    public String toString() {
        return "flat location: " + location;
    }


    private LocationId() {
        this.location = null;
    }

    public LocationId(String location) {
        this.location = location;
    }
}
