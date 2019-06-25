package com.example.demo.lock;

import java.util.concurrent.TimeUnit;

public interface FuckLock {

    boolean lock(String lockKey);

    boolean lock(String lockKey,long leaseTime, TimeUnit timeUnit );

    boolean lock(String lockKey, long waitTime,long leaseTime, TimeUnit timeUnit );

    void unlock(String lockKey);
}
