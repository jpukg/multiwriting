package ua.agabry.multiwriting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public abstract class AbstractConsumer<T> implements Runnable {

    protected DataQueue<T> queue;

    public AbstractConsumer(DataQueue<T> queue) {
        if (queue == null) throw new IllegalArgumentException("Parameters can't be null.");
        this.queue = queue;
    }
}
