package ua.agabry.multiwriting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class StringWriter extends AbstractConsumer<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StringWriter.class);

    private File file;

    public StringWriter(DataQueue<String> queue, File file) {
        super(queue);
        if (file == null) throw new IllegalArgumentException("Parameters can't be null.");
        this.file = file;
    }

    @Override
    public void run() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            String data = queue.get();
            while (queue.isContinueProducing() || data != null) {
                if (data != null) {
                    writer.write(data);
                    writer.newLine();
                }
                data = queue.get();
            }
        } catch (InterruptedException e) {
            LOGGER.error("Can't get the data from the queue.", e);
        } catch (IOException e) {
            LOGGER.error("Can't write into file.", e);
        }
    }
}
