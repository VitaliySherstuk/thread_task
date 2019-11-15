package com.epam.vsharstuk.safemap;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class SafeDeadlockTaskTest {

    private Logger LOG = Logger.getLogger(DeadlockTaskTest.class);
    private List<Integer> numbers = new ArrayList<>();
    AtomicBoolean isStoped = new AtomicBoolean();

    @Test
    public void testDeadlock() throws InterruptedException {

        AtomicInteger counter = new AtomicInteger(100);

        Thread t1 = new Thread(() -> {
            while(!isStoped.get() && counter.get() < 100) {
                counter.addAndGet(-1);
                synchronized (numbers) {
                    doStop();
                    numbers.add(getRandomNumber());
                    LOG.info(numbers.size() + ":" + numbers);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                doRun();
            }
        }, "Producer-----");

        Thread t2 = new Thread(() -> {
            while(!isStoped.get() && counter.get() < 100) {
                counter.addAndGet(-1);
                synchronized (numbers) {
                    doStop();
                    long summ = numbers.stream().collect(Collectors.summarizingInt(Integer::intValue)).getSum();
                    LOG.info(summ);
                }
                doRun();
            }
        }, "-----Summomer");

        Thread t3 = new Thread(() -> {
            while(!isStoped.get() && counter.get() < 100) {
                counter.addAndGet(-1);
                synchronized (numbers) {
                    doStop();
                    double sqrtSumm = numbers.stream().map(num -> Math.sqrt(num)).collect(Collectors.summarizingInt(Double::byteValue)).getSum();
                    LOG.info(Math.sqrt(sqrtSumm));
                }
                doRun();
            }
        }, "-----Sqrter-----");

        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();

    }

    private int getRandomNumber() {
        Random random = new Random();
        return  random.nextInt(10);
    }

    private void doStop() {
        isStoped.set(true);
    }

    private void doRun() {
        isStoped.set(false);
    }

    private boolean isStopepd() {
        return isStoped.get();
    }
}
