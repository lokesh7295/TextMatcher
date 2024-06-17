package matcher;

import java.util.*;

public class MatcherService {
    public static Map<String, List<MatcherResult>> findMatches(String searchText, List<String> searchStrings, int startingLineNumber, List<Integer> charCountByLine) {
        Map<String, List<MatcherResult>> matches = new HashMap<>();
        String[] lines = searchText.split("\n");

        for (String searchString : searchStrings) {
            matches.put(searchString, new ArrayList<>());
            for (int i = 0; i < lines.length; i++) {
                int prevCharOffset = i > 0 ? charCountByLine.get(i - 1) : 0;
                int index = lines[i].indexOf(searchString); //searchString = Common names
                while (index >= 0) {
                    matches.get(searchString).add(new MatcherResult(startingLineNumber + i + 1, prevCharOffset + index + 1));
                    index = lines[i].indexOf(searchString, index + 1);
                }
            }
        }

        return matches;
    }
}
