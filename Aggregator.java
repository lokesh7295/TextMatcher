import java.util.*;

public class Aggregator {
    public static void aggregateAndPrintResults(List<Map<String, List<MatcherResult>>> allResults) {
        Map<String, List<MatcherResult>> aggregatedResults = new HashMap<>();

        for (Map<String, List<MatcherResult>> resultMap : allResults) {
            for (Map.Entry<String, List<MatcherResult>> entry : resultMap.entrySet()) {
                String word = entry.getKey();
                List<MatcherResult> locations = entry.getValue();

                aggregatedResults.putIfAbsent(word, new ArrayList<>());
                aggregatedResults.get(word).addAll(locations);
            }
        }

        for (Map.Entry<String, List<MatcherResult>> entry : aggregatedResults.entrySet()) {
            List<MatcherResult> results = entry.getValue();
            System.out.println("Word: " + entry.getKey() + ", Count: " + results.size());
            System.out.println("Locations: " + results);
        }
    }
}
