package de.rwth.i2.attestor.its.certificate;


import javax.xml.bind.annotation.XmlSeeAlso;

@XmlSeeAlso({LocationId.class, LocationDuplicate.class})
public abstract class Location {
    public abstract String getLocation();
    public abstract boolean isDuplicate();
}
