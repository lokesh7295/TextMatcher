package main;

import aggregator.AggregatorService;
import matcher.MatcherService;
import matcher.MatcherResult;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class MainFileReader {
    private static final int LINES_SPLIT_SIZE = 1000;
    private static final List<String> SEARCH_STRINGS = Arrays.asList(("James,John,Robert,Michael,William,David,Richard,Charles,Joseph,Thomas" +
            ",Christopher,Daniel,Paul,Mark,Donald,George,Kenneth,Steven,Edward,Brian,Ronald,Anthony,Kevin,Jason,Matthew,Gary,Timothy,Jose,Larry" +
            ",Jeffrey,Frank,Scott,Eric,Stephen,Andrew,Raymond,Gregory,Joshua,Jerry,Dennis,Walter,Patrick,Peter,Harold,Douglas,Henry,Carl" +
            ",Arthur,Ryan,Roger").split(","));

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        String filePath = "/Users/lokeshpersonal/Desktop/TextMatcher/sample_string.txt";
        List<Future<Map<String, List<MatcherResult>>>> futures = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        try  {
            String line;
            StringBuilder searchText = new StringBuilder();
            List<Integer> charCountByLine = new ArrayList<>();
            int totalCharsTillNow = 0;
            int lineNumber = 0;
            int totalLinesRead = 0;
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            while ((line = reader.readLine()) != null) {
                totalCharsTillNow += line.length();
                charCountByLine.add(totalCharsTillNow);
                searchText.append(line).append("\n");
                lineNumber++;

                if (lineNumber % LINES_SPLIT_SIZE == 0) {
                    String chunkText = searchText.toString();
                    int startingLineNumber = totalLinesRead;
                    futures.add(executorService.submit(() -> MatcherService.findMatches(chunkText, SEARCH_STRINGS, startingLineNumber, charCountByLine)));
                    searchText.setLength(0);
                    totalLinesRead += lineNumber;
                    lineNumber = 0;
                }
            }


            if (!searchText.isEmpty()) {
                String chunkText = searchText.toString();
                int startingLineNumber = totalLinesRead;
                futures.add(executorService.submit(() -> MatcherService.findMatches(chunkText, SEARCH_STRINGS, startingLineNumber, charCountByLine)));
            }

            executorService.shutdown();

            var allResults = new ArrayList();
            for (Future<Map<String, List<MatcherResult>>> future : futures) {
                try {
                    allResults.add(future.get());
                } catch (InterruptedException | ExecutionException e) {
                    System.out.println("Exception: " + e);
                }
            }

            AggregatorService.aggregateAndPrintResults(allResults);
        } catch (IOException e) {
            System.out.println("Exception: " + e);
        }
        System.out.println("time taken: " + (System.currentTimeMillis() - startTime));
    }
}
