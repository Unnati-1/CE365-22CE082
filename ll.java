import java.util.*;

public class ll {
    
    public static Map<String, Set<String>> computeFirstSets(List<String[]> grammar, Set<String> terminals, Set<String> nonTerminals) {
        Map<String, Set<String>> first = new HashMap<>();
        for (String nt : nonTerminals) first.put(nt, new HashSet<>());
        for (String t : terminals) first.put(t, new HashSet<>(Collections.singleton(t)));
        
        boolean changed = true;
        while (changed) {
            changed = false;
            for (String[] rule : grammar) {
                String head = rule[0], body = rule[1];
                if (body.equals("ε")) {
                    if (first.get(head).add("ε")) changed = true;
                    continue;
                }
                boolean canDeriveEpsilon = true;
                for (char c : body.toCharArray()) {
                    String symbol = String.valueOf(c);
                    if (terminals.contains(symbol)) {
                        if (first.get(head).add(symbol)) changed = true;
                        canDeriveEpsilon = false;
                        break;
                    }
                    for (String s : first.get(symbol)) {
                        if (!s.equals("ε") && first.get(head).add(s)) changed = true;
                    }
                    if (!first.get(symbol).contains("ε")) {
                        canDeriveEpsilon = false;
                        break;
                    }
                }
                if (canDeriveEpsilon && first.get(head).add("ε")) changed = true;
            }
        }
        return first;
    }

    public static Map<String, Set<String>> computeFollowSets(List<String[]> grammar, Set<String> nonTerminals, Map<String, Set<String>> firstSets) {
        Map<String, Set<String>> follow = new HashMap<>();
        for (String nt : nonTerminals) follow.put(nt, new HashSet<>());
        follow.get("S").add("$");
        
        boolean changed = true;
        while (changed) {
            changed = false;
            for (String[] rule : grammar) {
                String head = rule[0], body = rule[1];
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
                    if (canDeriveEpsilon && follow.get(symbol).addAll(follow.get(head))) changed = true;
                }
            }
        }
        return follow;
    }

    public static Map<String, Map<String, String>> constructParsingTable(List<String[]> grammar, Set<String> terminals, Set<String> nonTerminals, Map<String, Set<String>> firstSets, Map<String, Set<String>> followSets) {
        Map<String, Map<String, String>> parsingTable = new HashMap<>();
        for (String nt : nonTerminals) {
            parsingTable.put(nt, new HashMap<>());
            for (String t : terminals) parsingTable.get(nt).put(t, null);
            parsingTable.get(nt).put("$", null);
        }
        for (String[] rule : grammar) {
            String head = rule[0], body = rule[1];
            Set<String> firstBody = new HashSet<>();
            boolean canDeriveEpsilon = true;
            for (char c : body.toCharArray()) {
                String symbol = String.valueOf(c);
                firstBody.addAll(firstSets.get(symbol));
                if (!firstSets.get(symbol).contains("ε")) {
                    canDeriveEpsilon = false;
                    break;
                }
            }
            firstBody.remove("ε");
            for (String terminal : firstBody) parsingTable.get(head).put(terminal, body);
            if (canDeriveEpsilon) {
                for (String terminal : followSets.get(head)) parsingTable.get(head).put(terminal, body);
            }
        }
        return parsingTable;
    }

    public static void printParsingTable(Map<String, Map<String, String>> parsingTable) {
        System.out.println("\nLL(1) Parsing Table:");
        Set<String> headers = parsingTable.values().iterator().next().keySet();
        System.out.println(" "+String.join(" | ", headers));
        System.out.println("-".repeat(10 * headers.size()));
        for (Map.Entry<String, Map<String, String>> entry : parsingTable.entrySet()) {
            List<String> rowEntries = new ArrayList<>();
            for (String t : headers) rowEntries.add(entry.getValue().getOrDefault(t, ""));
            System.out.println(entry.getKey() + " | " + String.join(" | ", rowEntries));
        }
    }

    public static void main(String[] args) {
        List<String[]> grammar = Arrays.asList(
            new String[]{"S", "ABC"}, new String[]{"S", "D"}, new String[]{"A", "a"}, new String[]{"A", "ε"},
            new String[]{"B", "b"}, new String[]{"B", "ε"}, new String[]{"C", "(S)"}, new String[]{"C", "c"}, new String[]{"D", "AC"}
        );
        Set<String> terminals = new HashSet<>(Arrays.asList("a", "b", "(", ")", "c", "ε"));
        Set<String> nonTerminals = new HashSet<>(Arrays.asList("S", "A", "B", "C", "D"));
        Map<String, Set<String>> firstSets = computeFirstSets(grammar, terminals, nonTerminals);
        Map<String, Set<String>> followSets = computeFollowSets(grammar, nonTerminals, firstSets);
        Map<String, Map<String, String>> parsingTable = constructParsingTable(grammar, terminals, nonTerminals, firstSets, followSets);
        printParsingTable(parsingTable);
    }
}
