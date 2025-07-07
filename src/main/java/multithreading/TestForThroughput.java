package multithreading;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TestForThroughput {
    private static final String INPUT_FILE = "src/main/resources/war_and_peace.txt";
    private static final int NUMBER_OF_THREADS = 8;

    public static void main(String[] args) throws IOException {
        String text = new String(Files.readAllBytes(Paths.get(INPUT_FILE)));
        startServer(text);
    }

    /*
     * Jmeter will simulate 200 users (threads) requests each time, for a total of 2600 request (words in the csv) file
     * getting throughpout is a kind of meter to measure speed  task/time
     * 6478 tasks(request) per second
     */

    public static void startServer(String text) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/search", new WordCountHandler(text));

        // Executor creates a number of given threads and instead of closing them and recreacting them, reuses them
        // until the tasks are done. This is known as a thread pool
        Executor executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        server.setExecutor(executor);
        server.start();
    }

    private static class WordCountHandler implements HttpHandler {
        private String text;

        public WordCountHandler(String text) {
            this.text = text;
        }

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String query = httpExchange.getRequestURI().getQuery();
            String[] keyValue = query.split("=");
            String action = keyValue[0];
            String word = keyValue[1];
            if (!action.equals("word")) {
                httpExchange.sendResponseHeaders(400, 0);
                OutputStream outputStream = httpExchange.getResponseBody();
                outputStream.close();
            } else {
                long count = countWord(word);

                byte[] response = Long.toString(count).getBytes();
                httpExchange.sendResponseHeaders(200, response.length);
                OutputStream outputStream = httpExchange.getResponseBody();
                outputStream.write(response);
                outputStream.close();
            }
        }

        private long countWord(String word) {
            long count = 0;
            int index = 0;
            while (index >= 0) {
                // This returns the first occurrence of the given word in text, starting from the given index
                index = text.indexOf(word, index);

                if (index >= 0) {
                    count++;

                    // we increase the number to keep looking for the next position after the first occurrence,
                    // otherwise it'd be stuck forever at the first occurrence
                    index++;
                }
            }
            return count;
        }
    }
}
