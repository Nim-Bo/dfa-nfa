package model;

import java.util.Map;
import java.util.Set;

public class NFA {
    private final Set<String> states;
    private final Set<String> alphabet;
    private final String startState;
    private final Set<String> acceptStates;
    private final Map<String, Set<String>> transitions;

    public NFA(Set<String> states, Set<String> alphabet, String startState, Set<String> acceptStates, Map<String, Set<String>> transitions) {
        this.states = states;
        this.alphabet = alphabet;
        this.startState = startState;
        this.acceptStates = acceptStates;
        this.transitions = transitions;
    }

    public Set<String> getStates() {
        return states;
    }

    public Set<String> getAlphabet() {
        return alphabet;
    }

    public String getStartState() {
        return startState;
    }

    public Set<String> getAcceptStates() {
        return acceptStates;
    }

    public Map<String, Set<String>> getTransitions() {
        return transitions;
    }
}
