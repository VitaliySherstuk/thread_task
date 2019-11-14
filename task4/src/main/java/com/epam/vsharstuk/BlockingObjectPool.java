package com.epam.vsharstuk;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Pool that block when it has not any items or it full.
 */
public class BlockingObjectPool {

    private BlockingQueue<Object> objects;

    /**
     * Creates filled pool of passed size
     *
     * @param size of pool
     */
    public BlockingObjectPool(int size) {
        objects = new ArrayBlockingQueue<>(size, true);
    }

    /**
     * Gets object from pool or blocks if pool is empty
     *
     * @return object from pool
     */
    public Object take() throws InterruptedException {
        return objects.take();
    }

    /**
     * Puts object to pool or blocks if pool is full
     *
     * @param object to be taken back to pool
     */
    public void put(Object object) throws InterruptedException {
        objects.put(object);
    }


    /**
     * @return actual size of the queue
     */
    public int getActualSize() {
        return objects.size();
    }

}
