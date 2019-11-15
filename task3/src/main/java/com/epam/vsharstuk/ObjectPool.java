package com.epam.vsharstuk;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ObjectPool {

    private Queue<Object> objects;
    private int size;
    private Lock lock;
    private Condition isFull;
    private Condition isEmpty;


    public ObjectPool(int size) {
        this.size = size;
        objects = new LinkedList<>();
        lock = new ReentrantLock();
        isFull = lock.newCondition();
        isEmpty = lock.newCondition();
    }

    public void put(Object object) throws InterruptedException {
        lock.lockInterruptibly();
        if (objects.size() == size) {
            isFull.await();
        }
        objects.add(object);
        isEmpty.signal();
        lock.unlock();
    }

    public Object get() throws InterruptedException {
        lock.lockInterruptibly();
        if (objects.isEmpty()) {
            isEmpty.await();
        }
        Object object = objects.poll();
        isFull.signal();
        lock.unlock();
        return object;
    }

    public int getSize() {
        return objects.size();
    }
}
