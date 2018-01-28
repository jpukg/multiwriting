package ua.agabry.multiwriting;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class DataQueue<T> {

    private ArrayBlockingQueue<T> queue;
    private boolean continueProducing = true;

    public DataQueue(int queueCapacity) {
        this.queue = new ArrayBlockingQueue<T>(queueCapacity);
    }

    public boolean isContinueProducing() {
        return continueProducing;
    }

    public void setContinueProducing(boolean continueProducing) {
        this.continueProducing = continueProducing;
    }

    public boolean offer(T data) throws InterruptedException {
        return queue.offer(data, 5, TimeUnit.SECONDS);
    }

    public T get() throws InterruptedException {
        return queue.poll(1, TimeUnit.SECONDS);
    }

    @Override
    public String toString() {
        return "DataQueue{" +
                "queue=" + queue +
                ", continueProducing=" + continueProducing +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataQueue<?> dataQueue = (DataQueue<?>) o;

        if (continueProducing != dataQueue.continueProducing) return false;
        return queue != null ? queue.equals(dataQueue.queue) : dataQueue.queue == null;
    }

    @Override
    public int hashCode() {
        int result = queue != null ? queue.hashCode() : 0;
        result = 31 * result + (continueProducing ? 1 : 0);
        return result;
    }
}
