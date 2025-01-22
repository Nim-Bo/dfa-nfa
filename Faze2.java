import java.util.*;

class NFA {
    Set<String> states;
    Set<String> alphabet;
    String startState;
    Set<String> acceptStates;
    Map<String, Set<String>> transitions;

    public NFA(Set<String> states, Set<String> alphabet, String startState, Set<String> acceptStates, Map<String, Set<String>> transitions) {
        this.states = states;
        this.alphabet = alphabet;
        this.startState = startState;
        this.acceptStates = acceptStates;
        this.transitions = transitions;
    }
}

public class Faze2 {
    private static DFA convert(NFA nfa) {
        Set<Set<String>> dfaStates = new HashSet<>();
        Map<String, String> dfaTransitions = new HashMap<>();
        Set<String> dfaAcceptStates = new HashSet<>();
        Set<String> alphabet = nfa.alphabet;

        Map<Set<String>, String> stateMapping = new HashMap<>();
        Set<String> startState = new HashSet<>(Collections.singletonList(nfa.startState));
        Queue<Set<String>> queue = new LinkedList<>();
        queue.add(startState);

        dfaStates.add(startState);
        stateMapping.put(startState, String.join(",", startState));

        while (!queue.isEmpty()) {
            Set<String> currentStates = queue.poll();

            for (String symbol : alphabet) {
                Set<String> nextStates = new HashSet<>();

                for (String state : currentStates) {
                    String key = state + "," + symbol;
                    if (nfa.transitions.containsKey(key)) {
                        nextStates.addAll(nfa.transitions.get(key));
                    }
                }

                if (!nextStates.isEmpty()) {
                    if (!dfaStates.contains(nextStates)) {
                        dfaStates.add(nextStates);
                        queue.add(nextStates);
                        stateMapping.put(nextStates, String.join(",", nextStates));
                    }

                    String currentStateName = stateMapping.get(currentStates);
                    String nextStateName = stateMapping.get(nextStates);
                    dfaTransitions.put(currentStateName + "," + symbol, nextStateName);
                }
            }
        }

        for (Set<String> stateSet : dfaStates) {
            for (String state : stateSet) {
                if (nfa.acceptStates.contains(state)) {
                    dfaAcceptStates.add(stateMapping.get(stateSet));
                    break;
                }
            }
        }

        Set<String> dfaStateNames = new HashSet<>(stateMapping.values());
        String dfaStartState = stateMapping.get(startState);

        return new DFA(dfaStateNames, alphabet, dfaStartState, dfaAcceptStates, dfaTransitions);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("NFA Configuration");
        System.out.print("Enter states (comma-separated): ");
        Set<String> states = new HashSet<>(Arrays.asList(scanner.nextLine().split(",")));

        System.out.print("Enter alphabet (comma-separated): ");
        Set<String> alphabet = new HashSet<>(Arrays.asList(scanner.nextLine().split(",")));

        System.out.print("Enter start state: ");
        String startState = scanner.nextLine();

        System.out.print("Enter accept states (comma-separated): ");
        Set<String> acceptStates = new HashSet<>(Arrays.asList(scanner.nextLine().split(",")));

        Map<String, Set<String>> transitions = new HashMap<>();
        System.out.println("Enter transitions in the format 'state,input,next_state1,next_state2,...' (one per line). Type 'done' to finish:");
        while (true) {
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase("done")) {
                break;
            }
            String[] parts = line.split(",");
            String key = parts[0] + "," + parts[1];
            Set<String> nextStates = new HashSet<>(Arrays.asList(parts).subList(2, parts.length));
            transitions.put(key, nextStates);
        }

        NFA nfa = new NFA(states, alphabet, startState, acceptStates, transitions);
        DFA dfaDto = convert(nfa);

        System.out.println("\nConverted DFA:");
        dfaDto.printDFA();
    }
}
