package com.epam.vsharstuk;

import org.apache.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Producer extends Thread {

    private ObjectPool pool;
    private Logger LOG = Logger.getLogger(Producer.class);
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Producer(ObjectPool pool) {
        this.pool = pool;
    }

    public Producer(ObjectPool pool, String threadName) {
        this.pool = pool;
        this.setName(threadName);
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(300);
                LOG.info("Actual pool size before PUT: " + pool.getSize());
                pool.put("MESSAGE - " + LocalDateTime.now().format(formatter));
                LOG.info("Actual pool size after PUT: " + pool.getSize());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
