package de.rwth.i2.attestor.stateSpaceGeneration;

import gnu.trove.list.array.TIntArrayList;

import java.util.Set;

public interface StateSpace {

    Set<ProgramState> getStates();
    Set<ProgramState> getInitialStates();
    Set<ProgramState> getFinalStates();

    Set<ProgramState> getControlFlowSuccessorsOf(ProgramState state);
    Set<ProgramState> getMaterializationSuccessorsOf(ProgramState state);

    TIntArrayList getControlFlowSuccessorsIdsOf(int stateSpaceId);
    TIntArrayList getMaterializationSuccessorsIdsOf(int stateSpaceId);

    boolean addState(ProgramState state);
    boolean addStateIfAbsent(ProgramState state);
    void addInitialState(ProgramState state);
    void setFinal(ProgramState state);

    void addMaterializationTransition(ProgramState from, ProgramState to);
    void addControlFlowTransition(ProgramState from, ProgramState to);

    int getMaximalStateSize();
}
