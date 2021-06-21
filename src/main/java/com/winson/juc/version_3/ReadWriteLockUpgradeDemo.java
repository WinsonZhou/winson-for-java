package com.winson.juc.version_3;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

/**
 * @author winson
 * @date 2021/6/21
 **/
public class ReadWriteLockUpgradeDemo {

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    private Lock readLock = lock.readLock();

    private Lock writeLock = lock.writeLock();

    private Map<String, String> cache = new HashMap<>();

    /**
     * 这里的锁升级会导致永久等待，需要注意
     * @param key
     * @param supplier
     * @return
     */
    public String get(String key, Supplier<String> supplier) {
        readLock.lock();
        String result = cache.get(key);
        if (result == null) {
            writeLock.lock();
            result = cache.get(key);
            if (result == null) {
                cache.put(key, supplier.get());
                result = cache.get(key);
            }
            writeLock.unlock();
        }
        readLock.unlock();
        return result;

    }

    public static void main(String[] args) {
        final ReadWriteLockUpgradeDemo demo = new ReadWriteLockUpgradeDemo();
        int size = 100;
        Thread[] threads = new Thread[size];
        for (int i = 0; i < size; i++) {
            final int threadFlag = i;
            threads[i] = new Thread(() -> System.out.println(demo.get("key", () -> "haha : " + threadFlag)));
            threads[i].start();
        }

        try {
            for (int i = 0; i < size; i++) {
                threads[i].join();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
