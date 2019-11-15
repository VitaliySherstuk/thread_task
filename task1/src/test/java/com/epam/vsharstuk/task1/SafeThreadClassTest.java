package com.epam.vsharstuk.task1;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.Assert.assertFalse;

public class SafeThreadClassTest {

    static Logger LOG = Logger.getLogger(SafeThreadClassTest.class);

    @Test
    public void test() throws InterruptedException {
        Map<Integer, Integer> elements = new ConcurrentHashMap<>();
        AtomicInteger buffer = new AtomicInteger();
        int value = 100;
        AtomicBoolean exception = new AtomicBoolean();
        CountDownLatch latch = new CountDownLatch(1);

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < value; i++) {

                    elements.put(i, 1);
                    LOG.info("Iteration: " + i);

                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            latch.countDown();
        }, "----Producer");

        Thread t2 = new Thread(() -> {
            while (!exception.get()) {
                try {

                    elements.entrySet().forEach(item -> {
                        buffer.addAndGet(item.getValue());
                        LOG.info("Iteration summ: " + buffer);
                    });

                } catch (ConcurrentModificationException e) {
                    exception.set(true);
                    latch.countDown();
                    break;
                }

            }
        }, "Summoner---");

        t1.start();
        t2.start();

        latch.await();

        assertFalse(exception.get());
    }

    @Test
    public void testSyncMap() throws InterruptedException {
        Map<Integer, Integer> elements = Collections.synchronizedMap(new HashMap<>());
        AtomicInteger value = new AtomicInteger(100);

        Thread t1 = new Thread(() -> {
           while(value.get() > 0) {
                try {
                    elements.put(100 - value.get(), 1);
                    LOG.info("Iteration: " + (100 - value.get()));
                    value.addAndGet(-1);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LOG.warn(e.getMessage());
                }
            }

        }, "----Producer");


        Thread t2 = new Thread(() -> {
            while (value.get() > 0) {
                try {
                    Integer result = elements.entrySet().stream().map(item -> item.getValue()).mapToInt(Integer::intValue).sum();
                    LOG.info("Iteration summ: " + result);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LOG.warn(e.getMessage());
                }
            }
        }, "Summoner---");

        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}
