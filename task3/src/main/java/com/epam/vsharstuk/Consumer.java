package com.epam.vsharstuk;

import org.apache.log4j.Logger;

public class Consumer extends Thread{

    private ObjectPool pool;
    private Logger LOG = Logger.getLogger(Producer.class);

    public Consumer(ObjectPool pool) {
        this.pool = pool;
    }

    public Consumer(ObjectPool pool, String threadName) {
        this.pool = pool;
        this.setName(threadName);
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(300);
                LOG.info("Actual pool size before GET: " + pool.getSize());
                pool.get();
                LOG.info("Actual pool size after GET: " + pool.getSize());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        }
    }
}
