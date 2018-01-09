package de.rwth.i2.attestor.main.phases.symbolicExecution.interprocedural;

import de.rwth.i2.attestor.graph.heap.HeapConfiguration;
import de.rwth.i2.attestor.interprocedural.PartialStateSpace;
import de.rwth.i2.attestor.interprocedural.ProcedureCall;
import de.rwth.i2.attestor.main.phases.symbolicExecution.StateSpaceGeneratorFactory;
import de.rwth.i2.attestor.procedures.Method;
import de.rwth.i2.attestor.procedures.contracts.InternalContract;
import de.rwth.i2.attestor.procedures.methodExecution.Contract;
import de.rwth.i2.attestor.stateSpaceGeneration.ProgramState;
import de.rwth.i2.attestor.stateSpaceGeneration.StateSpace;
import de.rwth.i2.attestor.stateSpaceGeneration.StateSpaceGenerationAbortedException;

import java.util.ArrayList;
import java.util.List;

public class InternalProcedureCall implements ProcedureCall {

    private Method method;
    private ProgramState preconditionState;
    private StateSpaceGeneratorFactory factory;
    private StateSpace stateSpace;

    private final static int PRIME = 31;

    public InternalProcedureCall(Method method, ProgramState preconditionState, StateSpaceGeneratorFactory factory) {

        this.method = method;
        this.preconditionState = preconditionState;
        this.factory = factory;
    }


    @Override
    public PartialStateSpace execute() {

        try {
            stateSpace = factory.create(method.getBody(), preconditionState).generate();

            List<HeapConfiguration> finalHeaps = new ArrayList<>();
            stateSpace.getFinalStates().forEach( finalState -> finalHeaps.add(finalState.getHeap()) );
            Contract contract = new InternalContract(preconditionState.getHeap(), finalHeaps);
            method.addContract(contract);

            return new InternalPartialStateSpace(preconditionState, factory);
        } catch (StateSpaceGenerationAbortedException e) {
            throw new IllegalStateException("Procedure call execution failed.");
        }
    }

    @Override
    public Method getMethod() {

        return method;
    }

    @Override
    public ProgramState getInput() {

        return preconditionState;
    }

    public StateSpace getStateSpace() {

        return stateSpace;
    }

    @Override
    public int hashCode() {

        /*int result = PRIME + ((preconditionState == null) ? 0 : preconditionState.hashCode());
        result = PRIME * result + ((method == null) ? 0 : method.hashCode());
        return result;
        */
        return PRIME;
    }

    @Override
    public boolean equals(Object otherOject) {

        if(this == otherOject) {
            return true;
        }
        if(otherOject == null) {
            return false;
        }
        if(otherOject.getClass() != InternalProcedureCall.class) {
            return false;
        }
        InternalProcedureCall call = (InternalProcedureCall) otherOject;
        return method.equals(call.method) &&
                preconditionState.equals(call.preconditionState);
    }
}
