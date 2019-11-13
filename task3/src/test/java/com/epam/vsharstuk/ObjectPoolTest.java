package com.epam.vsharstuk;

import org.testng.annotations.Test;


public class ObjectPoolTest {

    @Test
    public void test() throws InterruptedException {
        ObjectPool objectPool = new ObjectPool(10);

        Thread consumerThread1 = new Consumer(objectPool, "-----Consumer1");
        Thread producerThread1 = new Producer(objectPool, "-----Producer1");
        //Thread consumerThread2 = new Consumer(objectPool, "=======Consumer2");
        //Thread producerThread2 = new Producer(objectPool, "=======Producer2");

        consumerThread1.start();
        producerThread1.start();
        //consumerThread2.start();
        //producerThread2.start();
        producerThread1.join();
        consumerThread1.join();

    }

}