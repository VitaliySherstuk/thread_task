package com.epam.vsharstuk.task1;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockedMap {

    Lock lock = new ReentrantLock();
    private Logger LOG = Logger.getLogger(LockedMap.class);
    AtomicInteger buffer = new AtomicInteger();

    public void add(Map<Integer, Integer> map) {
       if(lock.tryLock()) {
           map.put(buffer.get(), 1);
           LOG.info("Iteration: " + buffer.get());
           buffer.addAndGet(1);
       }
   }

   public Integer summ(Map<Integer, Integer> map) {
        lock.lock();
        Integer result = map.entrySet().stream().map(item -> item.getValue()).mapToInt(Integer::intValue).sum();
        LOG.info("Iteration summ: " + result);
        lock.unlock();
        return result;
   }
}
