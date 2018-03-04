package ua.agabry.multiwriting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.Callable;

public abstract class AbstractConsumer<T> implements Callable<String> {

    protected DataQueue<T> queue;

    public AbstractConsumer(DataQueue<T> queue) {
        if (queue == null) throw new IllegalArgumentException("Parameters can't be null.");
        this.queue = queue;
    }
}
