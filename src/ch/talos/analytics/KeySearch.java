package ch.talos.analytics;

import ch.talos.HelperMethods;
import ch.talos.model.ModifiableNode;
import ch.talos.model.Node;
import javafx.util.Pair;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class KeySearch {
    public static List<ModifiableNode> search(Set<ModifiableNode> graph, String key) {
        List<Pair<ModifiableNode, Integer>> pairs = new LinkedList<>();

        for(ModifiableNode node : graph) {
            int lcs = longestCommonSubstring(key, node.name());
            if(lcs > 0) pairs.add(new Pair<>(node, lcs));
        }

        pairs.sort(matchingAndStringComparator());
        List<ModifiableNode> result = new LinkedList<>();
        int i = 20;
        for(Pair<ModifiableNode, Integer> p : pairs) {
            result.add(p.getKey());
            --i;
            if(i < 0) break;
        }

//        System.out.println(HelperMethods.printCollection(pairs));

        return result;
    }

    private static Comparator<? super Pair<ModifiableNode, Integer>> matchingAndStringComparator() {
        return (p1, p2) -> {
            int lcsComp = Integer.compare(p2.getValue(), p1.getValue());
            if(lcsComp == 0) {
                int lengthComp = Integer.compare(p1.getKey().name().length(), p2.getKey().name().length());
                if(lengthComp == 0)
                    return p1.getKey().name().compareTo(p2.getKey().name());
                else return lengthComp;
            }
            else return lcsComp;
        };
    }

    private static int longestCommonSubstring(String s, String t) {
        s = s.toLowerCase();
        t = t.toLowerCase();
        int[][] d = new int[s.length() + 1][t.length() + 1];
        int answer = 0;
        for(int i = 1; i <= s.length(); ++i) {
            for(int j = 1; j <= t.length(); ++j) {
                if(s.charAt(i-1) == t.charAt(j-1)){
                    d[i][j] = d[i-1][j-1] + 1;
                    answer = Math.max(answer, d[i][j]);
                }
            }
        }

//        System.out.println("LCS btw " + s + " and " + t + " is " + answer + " long");
        return answer;
    }
}
