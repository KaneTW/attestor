package de.rwth.i2.attestor.its;

import de.rwth.i2.attestor.its.certificate.*;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.helpers.DefaultValidationEventHandler;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class T2Result {

    private T2Status status;
    private Path outputDirectory;
    private Document certificate;
    private JAXBContext context;
    private Unmarshaller unmarshaller;

    public T2Result(T2Status status, Path outputDirectory, Document certificate) {
        this.status = status;
        this.outputDirectory = outputDirectory;
        this.certificate = certificate;

        try {
            this.context = JAXBContextFactory.createContext(new Class[] { TransitionRemovalProof.class, LTS.class, CooperationTermination.class  }, null);
            this.unmarshaller = this.context.createUnmarshaller();
            //this.unmarshaller.setEventHandler(new DefaultValidationEventHandler());
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }
    }

    public T2Status getStatus() {
        return status;
    }

    public Path getOutputDirectory() {
        return outputDirectory;
    }

    public Document getCertificate() {
        return certificate;
    }

    public LTS getLts() {
        // there should be exactly one lts
        Node node = getCertificate().getElementsByTagName("lts").item(0);
        try {
            return (LTS) unmarshaller.unmarshal(node);
        } catch (JAXBException ex) {
            ex.printStackTrace();
            return null;
        }
    }


    public List<TransitionRemovalProof> getTransitionRemovalProofs() {
        ArrayList<TransitionRemovalProof> proofs = new ArrayList<>();
        NodeList elements = getCertificate().getElementsByTagName("transitionRemoval");
        for (int i = 0; i < elements.getLength(); i++) {
            Node node = elements.item(i);
            try {
                proofs.add((TransitionRemovalProof)unmarshaller.unmarshal(node));
            } catch (JAXBException ex) {
                ex.printStackTrace();
            }
        }
        return proofs;
    }

    public List<LocationAdditionProof> getLocationAdditionProofs() {
        ArrayList<LocationAdditionProof> proofs = new ArrayList<>();
        NodeList elements = getCertificate().getElementsByTagName("locationAddition");
        for (int i = 0; i < elements.getLength(); i++) {
            Node node = elements.item(i);
            try {
                proofs.add((LocationAdditionProof)unmarshaller.unmarshal(node));
            } catch (JAXBException ex) {
                ex.printStackTrace();
            }
        }
        return proofs;
    }

    public CooperationTermination getCooperationTermination() {
        Node proof = getCertificate().getElementsByTagName("switchToCooperationTermination").item(0);
        try {
            return (CooperationTermination) unmarshaller.unmarshal(proof);
        } catch (JAXBException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
