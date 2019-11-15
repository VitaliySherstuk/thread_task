package com.epam.vsharstuk;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class BlockingObjectPoolTest {

    private Logger LOG = Logger.getLogger(BlockingObjectPoolTest.class);
    private BlockingObjectPool blockingObjectPool = new BlockingObjectPool(5);

    @Test
    public void test() throws InterruptedException {

        AtomicInteger value = new AtomicInteger(100);

        Thread t1 = new Thread(() -> {

            while(value.get() > 0) {
                try {
                    LOG.info("Queue size before Take: " + blockingObjectPool.getActualSize());
                    blockingObjectPool.take();
                    LOG.info("Queue size after TAKE: " + blockingObjectPool.getActualSize());
                    value.addAndGet(-1);
                } catch (InterruptedException e) {
                    LOG.warn(e.getMessage());
                }
            }

        }, "----Consumer");


        Thread t2 = new Thread(() -> {

            while(value.get() > 0) {
                try {
                    LOG.info("Queue size before PUT: " + blockingObjectPool.getActualSize());
                    blockingObjectPool.put(new Object());
                    LOG.info("Queue size after PUT: " + blockingObjectPool.getActualSize());
                } catch (InterruptedException e) {
                    LOG.warn(e.getMessage());
                }
            }

        }, "Producer-----");

        t1.start();
        t2.start();
        t1.join();
        t2.join();

    }
}
