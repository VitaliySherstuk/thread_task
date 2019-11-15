package com.epam.vsharstuk.safemap;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DeadlockTaskTest {

    private Logger LOG = Logger.getLogger(DeadlockTaskTest.class);
    private List<Integer> numbers = new ArrayList<>();

    @Test
    public void testDeadlock() throws InterruptedException {

        AtomicInteger counter = new AtomicInteger(200);

        Thread t1 = new Thread(() -> {
            while(counter.get() > 0) {
                counter.addAndGet(-1);
                synchronized (numbers) {
                    numbers.add(getRandomNumber());
                    LOG.info(numbers.size() + ":" + numbers);
                    try {
                        Thread.sleep(600);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, "Producer-----");

        Thread t2 = new Thread(() -> {
            while(counter.get() > 0) {
                counter.addAndGet(-1);
                synchronized (numbers) {
                    long summ = numbers.stream().collect(Collectors.summarizingInt(Integer::intValue)).getSum();
                    LOG.info(summ);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "-----Summomer");

        Thread t3 = new Thread(() -> {
            while (counter.get() > 0) {
                counter.addAndGet(-1);
                synchronized (numbers) {
                    double sqrtSumm = numbers.stream().map(num -> Math.sqrt(num)).collect(Collectors.summarizingInt(Double::byteValue)).getSum();
                    LOG.info(Math.sqrt(sqrtSumm));
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

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
}