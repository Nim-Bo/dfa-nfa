import model.DFA;

import java.util.*;

public class DFAProcessor {
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
            if (parts.length == 3) {
                String key = parts[0] + "," + parts[1];
                transitions.put(key, parts[2]);
            }
        }

        DFA dfa = new DFA(states, alphabet, startState, acceptStates, transitions);

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Check if a string is accepted");
            System.out.println("2. Generate accepted strings");
            System.out.println("3. Find the shortest accepted string");
            System.out.println("4. Find the longest accepted string");
            System.out.println("5. Generate accepted strings of a specific length");
            System.out.println("6. Exit");

            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter a string: ");
                    String input = scanner.nextLine();
                    if (dfa.isAccepted(input)) {
                        System.out.println("The string is accepted.");
                    } else {
                        System.out.println("The string is not accepted.");
                    }
                    break;
                case "2":
                    System.out.print("Enter the maximum length of strings: ");
                    int maxLength = Integer.parseInt(scanner.nextLine());
                    List<String> strings = dfa.generateAcceptedStrings(maxLength, 20);
                    System.out.println("Accepted strings: " + strings);
                    break;
                case "3":
                    String shortest = dfa.findShortestAcceptedString();
                    System.out.println("Shortest accepted string: " + (shortest != null ? shortest : "None"));
                    break;
                case "4":
                    String longest = dfa.findLongestAcceptedString();
                    System.out.println("Longest accepted string: " + (longest != null ? longest : "None"));
                    break;
                case "5":
                    System.out.print("Enter the specific length of strings: ");
                    int length = Integer.parseInt(scanner.nextLine());
                    List<String> stringsOfLength = dfa.generateAcceptedStringsOfLength(length);
                    System.out.println("Accepted strings of length " + length + ": " + stringsOfLength);
                    break;
                case "6":
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
