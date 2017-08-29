package de.rwth.i2.attestor.io;

import de.rwth.i2.attestor.graph.heap.HeapConfiguration;

/**
 * Created by christina on 23.08.17.
 */
public class HcLabelPair {

    String label;
    HeapConfiguration hc;

    public HcLabelPair(String label, HeapConfiguration hc){
        this.label = label;
        this.hc = hc;
    }

    public String getLabel() {
        return label;
    }

    public HeapConfiguration getHc() {
        return hc;
    }
}