package ua.agabry.multiwriting;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Iterator;
import java.util.List;

public class StringReader extends AbstractProducer<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StringReader.class);

    private File file;

    public StringReader(List<DataQueue<String>> dataQueues, File file) {
        super(dataQueues);
        if (file == null) throw new IllegalArgumentException("Parameters can't be null.");
        this.file = file;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Iterator<DataQueue<String>> iterator = queues.iterator();
                if (!iterator.hasNext()) {
                    LOGGER.debug("No queues available to offer data.");
                    break;
                }
                while (iterator.hasNext()) {
                    DataQueue<String> queue = iterator.next();
                    boolean isAdded = queue.offer(line);
                    if (!isAdded) {
                        LOGGER.debug("Can't offer data into queue - it's full.");
                        queue.setContinueProducing(false);
                        iterator.remove();
                    }
                }
            }
        } catch (InterruptedException e) {
            LOGGER.error("Can't insert data into queue.", e);
        } catch (FileNotFoundException e) {
            LOGGER.error("Can't find a file.", e);
        } catch (IOException e) {
            LOGGER.error("Can't read a file.", e);
        } finally {
            for (DataQueue<String> queue : queues) {
                queue.setContinueProducing(false);
            }
        }
    }


}
