package model;

import java.util.*;

public class DFA {
    private final Set<String> states;
    private final Set<String> alphabet;
    private final String startState;
    private final Set<String> acceptStates;
    private final Map<String, String> transitions;

    public DFA(Set<String> states, Set<String> alphabet, String startState, Set<String> acceptStates, Map<String, String> transitions) {
        this.states = states;
        this.alphabet = alphabet;
        this.startState = startState;
        this.acceptStates = acceptStates;
        this.transitions = transitions;
    }

    public boolean isAccepted(String input) {
        String currentState = startState;
        for (char ch : input.toCharArray()) {
            String character = String.valueOf(ch);
            if (!alphabet.contains(character)) {
                return false;
            }
            String key = currentState + "," + character;
            if (!transitions.containsKey(key)) {
                return false;
            }
            currentState = transitions.get(key);
        }
        return acceptStates.contains(currentState);
    }

    public List<String> generateAcceptedStrings(int maxLength, int limit) {
        List<String> acceptedStrings = new ArrayList<>();
        generateStringsRecursive("", maxLength, acceptedStrings, limit);
        return acceptedStrings;
    }

    private void generateStringsRecursive(String current, int maxLength, List<String> acceptedStrings, int limit) {
        if (current.length() > maxLength || acceptedStrings.size() >= limit) {
            return;
        }
        if (isAccepted(current)) {
            acceptedStrings.add(current);
        }
        for (String character : alphabet) {
            generateStringsRecursive(current + character, maxLength, acceptedStrings, limit);
        }
    }

    public List<String> generateAcceptedStringsOfLength(int length) {
        List<String> acceptedStrings = new ArrayList<>();
        generateStringsOfLengthRecursive("", length, acceptedStrings);
        return acceptedStrings;
    }

    private void generateStringsOfLengthRecursive(String current, int length, List<String> acceptedStrings) {
        if (current.length() == length) {
            if (isAccepted(current)) {
                acceptedStrings.add(current);
            }
            return;
        }
        for (String character : alphabet) {
            generateStringsOfLengthRecursive(current + character, length, acceptedStrings);
        }
    }

    public String findShortestAcceptedString() {
        List<String> strings = generateAcceptedStrings(1000, 1);
        return strings.isEmpty() ? null : strings.getFirst();
    }

    public String findLongestAcceptedString() {
        int maxLength = 100; // Adjust as needed
        List<String> strings = generateAcceptedStrings(maxLength, Integer.MAX_VALUE);
        return strings.stream().max(Comparator.comparingInt(String::length)).orElse(null);
    }

    public void printDFA() {
        System.out.println("DFA States: " + states);
        System.out.println("DFA Alphabet: " + alphabet);
        System.out.println("DFA Start State: " + startState);
        System.out.println("DFA Accept States: " + acceptStates);
        System.out.println("DFA Transitions: ");
        for (Map.Entry<String, String> entry : transitions.entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
        }
    }
}
