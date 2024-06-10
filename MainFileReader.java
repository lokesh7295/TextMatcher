import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class MainFileReader {
    private static final int LINES_SPLIT_SIZE = 1000;
    private static final String COMMON_NAMES = "James,John,Robert,Michael,William,David,Richard,Charles,Joseph,Thomas,Christopher,Daniel,Paul,Mark,Donal" +
            "d,George,Kenneth,Steven,Edward,Brian,Ronald,Anthony,Kevin,Jason,Matthew,Gary,Timothy,Jose,Larry,Jeffrey," +
            "Frank,Scott,Eric,Stephen,Andrew,Raymond,Gregory,Joshua,Jerry,Dennis,Walter,Patrick,Peter,Harold,Douglas,H" +
            "enry,Carl,Arthur,Ryan,Roger";
    private static final List<String> SEARCH_STRINGS = Arrays.asList(COMMON_NAMES.split(","));

    public static void main(String[] args) {
        String filePath = "/Users/lokeshpersonal/Desktop/TextMatcher/sample_string.txt";
        List<Future<Map<String, List<MatcherResult>>>> futures = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        try  {
            String line;
            StringBuilder searchText = new StringBuilder();
            int lineNumber = 0;
            int totalLinesRead = 0;
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            while ((line = reader.readLine()) != null) {
                searchText.append(line).append("\n");
                lineNumber++;

                if (lineNumber % LINES_SPLIT_SIZE == 0) {
                    String chunkText = searchText.toString();
                    int startingLineNumber = totalLinesRead;
                    futures.add(executorService.submit(() -> Matcher.findMatches(chunkText, SEARCH_STRINGS, startingLineNumber)));
                    searchText.setLength(0);
                    totalLinesRead += lineNumber;
                    lineNumber = 0;
                }
            }


            if (!searchText.isEmpty()) {
                String chunkText = searchText.toString();
                int startingLineNumber = totalLinesRead;
                futures.add(executorService.submit(() -> Matcher.findMatches(chunkText, SEARCH_STRINGS, startingLineNumber)));
            }

            executorService.shutdown();

            List<Map<String, List<MatcherResult>>> allResults = new ArrayList<>();
            for (Future<Map<String, List<MatcherResult>>> future : futures) {
                try {
                    allResults.add(future.get());
                } catch (InterruptedException | ExecutionException e) {
                    System.out.println("Exception: " + e);
                }
            }

            Aggregator.aggregateAndPrintResults(allResults);
        } catch (IOException e) {
            System.out.println("Exception: " + e);
        }
    }
}
