import model.DFA;

import java.util.*;

public class Minimizer {

    public static DFA minimize(DFA dfa) {

        Set<Set<String>> partitions = new HashSet<>();
        partitions.add(new HashSet<>(dfa.getAcceptStates()));
        Set<String> nonAcceptStates = new HashSet<>(dfa.getStates());
        nonAcceptStates.removeAll(dfa.getAcceptStates());
        partitions.add(nonAcceptStates);

        boolean changed;
        do {
            changed = false;
            Set<Set<String>> newPartitions = new HashSet<>();

            for (Set<String> group : partitions) {
                Map<Map<String, Set<String>>, Set<String>> groupsByBehavior = new HashMap<>();

                for (String state : group) {
                    Map<String, Set<String>> behavior = new HashMap<>();
                    for (String symbol : dfa.getAlphabet()) {
                        String transitionKey = state + "," + symbol;
                        String nextState = dfa.getTransitions().get(transitionKey);

                        if (nextState != null) {
                            for (Set<String> p : partitions) {
                                if (p.contains(nextState)) {
                                    behavior.put(symbol, p);
                                    break;
                                }
                            }
                        }
                    }
                    groupsByBehavior.computeIfAbsent(behavior, k -> new HashSet<>()).add(state);
                }

                newPartitions.addAll(groupsByBehavior.values());
            }

            if (!partitions.equals(newPartitions)) {
                changed = true;
                partitions = newPartitions;
            }
        } while (changed);

        Map<String, String> stateMapping = new HashMap<>();
        int counter = 0;

        for (Set<String> group : partitions) {
            String newState = "q" + counter++;
            for (String oldState : group) {
                stateMapping.put(oldState, newState);
            }
        }

        Set<String> minimizedStates = new HashSet<>(stateMapping.values());
        String minimizedStartState = stateMapping.get(dfa.getStartState());

        Set<String> minimizedAcceptStates = new HashSet<>();
        for (String state : dfa.getAcceptStates()) {
            minimizedAcceptStates.add(stateMapping.get(state));
        }

        Map<String, String> minimizedTransitions = new HashMap<>();
        for (Map.Entry<String, String> entry : dfa.getTransitions().entrySet()) {
            String[] parts = entry.getKey().split(",");
            String fromState = stateMapping.get(parts[0]);
            String symbol = parts[1];
            String toState = stateMapping.get(entry.getValue());
            minimizedTransitions.put(fromState + "," + symbol, toState);
        }

        return new DFA(minimizedStates, dfa.getAlphabet(), minimizedStartState, minimizedAcceptStates, minimizedTransitions);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("DFA Configuration");
        System.out.print("Enter states (comma-separated): ");
        Set<String> states = new HashSet<>(Arrays.asList(scanner.nextLine().split(",")));

        System.out.print("Enter alphabet (comma-separated): ");
        Set<String> alphabet = new HashSet<>(Arrays.asList(scanner.nextLine().split(",")));

        System.out.print("Enter start state: ");
        String startState = scanner.nextLine();

        System.out.print("Enter accept states (comma-separated): ");
        Set<String> acceptStates = new HashSet<>(Arrays.asList(scanner.nextLine().split(",")));

        Map<String, String> transitions = new HashMap<>();
        System.out.println("Enter transitions in the format 'state,input,next_state' (one per line). Type 'done' to finish:");
        while (true) {
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase("done")) {
                break;
            }
            String[] parts = line.split(",");
            transitions.put(parts[0] + "," + parts[1], parts[2]);
        }

        DFA dfa = new DFA(states, alphabet, startState, acceptStates, transitions);

        System.out.println("\nOriginal DFA:");
        dfa.printDFA();

        DFA minimizedDFA = minimize(dfa);

        System.out.println("\nMinimized DFA:");
        minimizedDFA.printDFA();
    }

}
