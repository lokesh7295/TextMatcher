import java.util.*;

public class Matcher {
    public static Map<String, List<MatcherResult>> findMatches(String text, List<String> searchStrings, int startingLineNumber) {
        Map<String, List<MatcherResult>> matches = new HashMap<>();
        String[] lines = text.split("\n");

        for (String searchString : searchStrings) {
            matches.put(searchString, new ArrayList<>());
            for (int i = 0; i < lines.length; i++) {
                int index = lines[i].indexOf(searchString);
                while (index >= 0) {
                    matches.get(searchString).add(new MatcherResult(startingLineNumber + i + 1, index));
                    index = lines[i].indexOf(searchString, index + 1);
                }
            }
        }

        return matches;
    }
}
