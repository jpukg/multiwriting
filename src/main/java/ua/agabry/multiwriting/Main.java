package ua.agabry.multiwriting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        String sourceFile = args[0];
        int numberOfCopies = Integer.parseInt(args[1]);

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfCopies + 1);

        // start consumer(s)
        List<DataQueue<String>> buffers = new ArrayList<>();
        Map<File, Future<String>> futures = new HashMap<>();
        for (int i = 1; i <= numberOfCopies; i++) {
            DataQueue<String> dataQueue = new DataQueue<>(100);
            buffers.add(dataQueue);
            File targetFile = new File("(" + i + ")" + sourceFile);
            Future<String> future = executorService.submit(new StringWriter(dataQueue, targetFile));
            futures.put(targetFile, future);
        }
        // start producer
        Future producerStatus = executorService.submit(new StringReader(buffers, new File(sourceFile)));

        // wait for producer to finish its execution
        try {
            producerStatus.get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("Can't process the source file.", e);
        }
        // wait for file writers
        for (Map.Entry<File, Future<String>> entry : futures.entrySet()) {
            try {
                entry.getValue().get(5, TimeUnit.SECONDS);
                System.out.println("File created: " + entry.getKey().toString());
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                LOGGER.error("Can't write the file: {}", entry.getKey().toString(), e);
            }
        }
        executorService.shutdown();
    }
}
