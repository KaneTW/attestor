package de.rwth.i2.attestor.its;

import de.rwth.i2.attestor.its.certificate.LTS;
import de.rwth.i2.attestor.its.certificate.LocationAdditionProof;
import de.rwth.i2.attestor.its.certificate.TransitionDefinition;
import de.rwth.i2.attestor.its.certificate.TransitionRemovalProof;

import java.util.*;
import java.util.stream.Collectors;

public class CertificateMapping {
    private final LTS lts;
    private final ITS its;

    private final Map<Integer, ITSTransition> transitionMap = new HashMap<>();

    // this associates added transitions with their origin
    private final Map<Integer, Integer> transitionSnapshotMap = new HashMap<>();

    public CertificateMapping(ITS its, LTS lts, List<LocationAdditionProof> newLocations) {
        this.lts = lts;
        this.its = its;

        Set<ITSTransition> itsTransitions = new HashSet<>(its.getTransitions());

        for (TransitionDefinition td : lts.getTransitions()) {
            int id = td.getTransition().getTransition();

            try {
                int from = Integer.parseInt(td.getSource().getLocation());
                int to = Integer.parseInt(td.getTarget().getLocation());

                List<ITSTransition> matching = itsTransitions
                        .stream()
                        .filter(itsTransition -> itsTransition.getFrom() == from
                                && itsTransition.getTo() == to)
                        .collect(Collectors.toList());

                if (matching.size() < 1) {
                    throw new RuntimeException("Expected at least one transition from " + from + " to " + to);
                }

                if (matching.size() > 1) {
                    throw new RuntimeException("Expected at most one transition from " + from + " to " + to + ", found "  + matching.size());
                }

                transitionMap.put(id, matching.get(0));
            } catch (NumberFormatException ex) {
                throw new RuntimeException("Failure when parsing " + td + ", locations should have been ints");
            }
        }

        for (LocationAdditionProof newLoc : newLocations) {
            TransitionDefinition td =  newLoc.getTransition();

            int id = td.getTransition().getTransition();

            Integer from = null;
            Integer to = null;

            try {
                from = Integer.parseInt(td.getSource().getLocation());
            } catch (NumberFormatException ex) {
                // pass
            }

            try {
                to = Integer.parseInt(td.getTarget().getLocation());
            } catch (NumberFormatException ex) {
                // pass
            }

            if (from == null && to == null) {
                throw new RuntimeException("Weird transition " + td + ", check assumptions; we shouldn't have transitions from added location to added location");
            }

            if (from != null && to != null) {
                // we can't have both non-null, as that implies they're both integers (= not dummy or snapshot)
                throw new RuntimeException("A location was added that's not a dummy or snapshot: " + td);
            }

            int state = from == null ? from : to;

            transitionSnapshotMap.put(id, state);
        }
    }

    public Map<Integer, ITSTransition> getTransitionMap() {
        return transitionMap;
    }

    public Map<Integer, Integer> getTransitionSnapshotMap() {
        return transitionSnapshotMap;
    }
}
