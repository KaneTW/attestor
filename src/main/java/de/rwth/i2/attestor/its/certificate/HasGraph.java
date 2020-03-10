package de.rwth.i2.attestor.its.certificate;

import com.google.common.graph.Network;

public interface HasGraph {
    Network<String, Integer> getCurrentGraph(Network<String, Integer> previous);
}
