package com.epam.vsharstuk.task1;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.Assert.assertTrue;


public class NotSafeThreadClassTest {

    static Logger LOG = Logger.getLogger(NotSafeThreadClassTest.class);

    @Test
    public void test() throws InterruptedException {
        Map<Integer, Integer> elements = new HashMap<>();
        AtomicInteger buffer = new AtomicInteger();
        int value = 100;
        AtomicBoolean exception = new AtomicBoolean();
        CountDownLatch latch = new CountDownLatch(1);

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < value; i++) {
                elements.put(i, 1);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                LOG.info("Iteration: " + i);
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

        assertTrue(exception.get());
    }

}