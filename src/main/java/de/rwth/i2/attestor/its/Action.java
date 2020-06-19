package de.rwth.i2.attestor.its;

import java.util.Set;

/**
 * An ITS action -- corresponds to an expression between FROM...TO
 */
public interface Action {

    Set<ITSVariable> occurringVariables();
}
