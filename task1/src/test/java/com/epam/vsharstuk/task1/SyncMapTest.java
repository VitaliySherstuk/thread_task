package com.epam.vsharstuk.task1;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SyncMapTest {

    static Logger LOG = Logger.getLogger(SafeThreadClassTest.class);
    Map<Integer, Integer> elements = new HashMap<>();
    int value = 100;
    private Lock lock = new ReentrantLock();
    AtomicInteger buffer = new AtomicInteger();

    @Test
    public void test() throws InterruptedException {

        Map<Integer, Integer> map = new HashMap<>();
        SyncMap lockedMap = new SyncMap();
        AtomicInteger value = new AtomicInteger(100);

        Thread t1 = new Thread(() -> {
            while(value.get() > 0){
                try {
                    lockedMap.add(map);
                    value.addAndGet(-1);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LOG.warn(e.getMessage());
                }
            }

        }, "----Producer");


        Thread t2 = new Thread(() -> {
            while(value.get() > 0) {
                try {
                    lockedMap.summ(map);
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