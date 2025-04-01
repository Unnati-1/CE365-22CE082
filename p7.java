import java.util.*;

public class p7 {
    public static void main(String[] args) {
        // Define the grammar
        Map<String, List<String>> grammar = new HashMap<>();
        grammar.put("S", Arrays.asList("ABC", "D"));
        grammar.put("A", Arrays.asList("a", "ε"));
        grammar.put("B", Arrays.asList("b", "ε"));
        grammar.put("C", Arrays.asList("(S)", "c"));
        grammar.put("D", Arrays.asList("AC"));
        
        // Define terminals and non-terminals
        Set<String> terminals = new HashSet<>(Arrays.asList("a", "b", "(", ")", "c", "ε"));
        Set<String> nonTerminals = grammar.keySet();
        
        // Compute First sets
        Map<String, Set<String>> firstSets = computeFirstSets(grammar, terminals, nonTerminals);
        
        // Compute Follow sets
        Map<String, Set<String>> followSets = computeFollowSets(grammar, terminals, nonTerminals, firstSets);
        
        // Print First Sets
        System.out.println("First Sets:");
        for (String nt : nonTerminals) {
            System.out.println("First(" + nt + ") = " + firstSets.get(nt));
        }
        
        // Print Follow Sets
        System.out.println("\nFollow Sets:");
        for (String nt : nonTerminals) {
            System.out.println("Follow(" + nt + ") = " + followSets.get(nt));
        }
    }

    private static Map<String, Set<String>> computeFirstSets(Map<String, List<String>> grammar, Set<String> terminals, Set<String> nonTerminals) {
        Map<String, Set<String>> first = new HashMap<>();
        for (String nt : nonTerminals) first.put(nt, new HashSet<>());
        for (String t : terminals) first.put(t, new HashSet<>(Collections.singletonList(t)));
        
        boolean changed;
        do {
            changed = false;
            for (String head : grammar.keySet()) {
                for (String body : grammar.get(head)) {
                    if (body.equals("ε")) {
                        if (first.get(head).add("ε")) changed = true;
                        continue;
                    }
                    
                    boolean canDeriveEpsilon = true;
                    for (char ch : body.toCharArray()) {
                        String symbol = String.valueOf(ch);
                        if (terminals.contains(symbol)) {
                            if (first.get(head).add(symbol)) changed = true;
                            canDeriveEpsilon = false;
                            break;
                        }
                        if (nonTerminals.contains(symbol)) {
                            for (String s : first.get(symbol)) {
                                if (!s.equals("ε") && first.get(head).add(s)) changed = true;
                            }
                            if (!first.get(symbol).contains("ε")) {
                                canDeriveEpsilon = false;
                                break;
                            }
                        }
                    }
                    if (canDeriveEpsilon && first.get(head).add("ε")) changed = true;
                }
            }
        } while (changed);
        return first;
    }

    private static Map<String, Set<String>> computeFollowSets(Map<String, List<String>> grammar, Set<String> terminals, Set<String> nonTerminals, Map<String, Set<String>> firstSets) {
        Map<String, Set<String>> follow = new HashMap<>();
        for (String nt : nonTerminals) follow.put(nt, new HashSet<>());
        follow.get("S").add("$"); // Start symbol follow set contains $
        
        boolean changed;
        do {
            changed = false;
            for (String head : grammar.keySet()) {
                for (String body : grammar.get(head)) {
                    for (int i = 0; i < body.length(); i++) {
                        String symbol = String.valueOf(body.charAt(i));
                        if (!nonTerminals.contains(symbol)) continue;
                        
                        Set<String> firstOfRemainder = new HashSet<>();
                        boolean canDeriveEpsilon = true;
                        for (int j = i + 1; j < body.length(); j++) {
                            String nextSymbol = String.valueOf(body.charAt(j));
                            firstOfRemainder.addAll(firstSets.get(nextSymbol));
                            if (!firstSets.get(nextSymbol).contains("ε")) {
                                canDeriveEpsilon = false;
                                break;
                            }
                        }
                        firstOfRemainder.remove("ε");
                        
                        if (follow.get(symbol).addAll(firstOfRemainder)) changed = true;
                        if (canDeriveEpsilon || i == body.length() - 1) {
                            if (follow.get(symbol).addAll(follow.get(head))) changed = true;
                        }
                    }
                }
            }
        } while (changed);
        return follow;
    }
}
