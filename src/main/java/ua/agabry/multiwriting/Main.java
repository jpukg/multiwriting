package ua.agabry.multiwriting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            String sourceFile = args[0];
            int numberOfCopies = Integer.parseInt(args[1]);

            ExecutorService executorService = Executors.newFixedThreadPool(numberOfCopies + 1);

            List<DataQueue<String>> buffers = new ArrayList<>();
            for (int i = 1; i <= numberOfCopies; i++) {
                DataQueue<String> dataQueue = new DataQueue<>(100);
                executorService.execute(new StringWriter(dataQueue, new File("(" + i + ")" + sourceFile)));
                buffers.add(dataQueue);
            }

            Future producerStatus = executorService.submit(new StringReader(buffers, new File(sourceFile)));
            producerStatus.get();
            executorService.shutdown();
        } catch (Exception e) {
            LOGGER.error("Something goes wrong...", e);
        }

    }
}
