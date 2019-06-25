package com.example.demo.lock;

import com.example.demo.config.RedissonConfig;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class RedisLockHelper implements FuckLock{


//    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newScheduledThreadPool(10);
    private static final long DEFAULT_WAITTIME= 5;
    private static final long DEFAULT_LEASETIME= 5;
    private static final TimeUnit DEFAULT_TIMEUNIT= TimeUnit.SECONDS;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * @author guoyitao
     * @date 2019/6/25 12:56
     * @param lockKey key
     * @return boolean 是否上锁成功
     */
    @Override
    public boolean lock(String lockKey) {
        return lock(lockKey,DEFAULT_WAITTIME,DEFAULT_LEASETIME,DEFAULT_TIMEUNIT);
    }
    /**
     * @author guoyitao
     * @date 2019/6/25 12:56
     * @param lockKey key
     * @param leaseTime 延迟时间
     * @param timeUnit 单位
     * @return boolean 是否上锁成功
     */
    @Override
    public boolean lock(String lockKey, long leaseTime, TimeUnit timeUnit)  {
        return lock(lockKey,DEFAULT_WAITTIME,leaseTime,timeUnit);
    }

    /**
     * @author guoyitao
     * @date 2019/6/25 12:56
     * @param lockKey key
     * @param leaseTime 锁住时间
     * @param timeUnit 单位
     * @return boolean 是否上锁成功
     */
    @Override
    public boolean lock(String lockKey, long waitTime,long leaseTime, TimeUnit timeUnit)  {
        RLock lock = redissonClient.getLock(lockKey);
        if (lock.isLocked()){
            return false;
        }
        try {
            return lock.tryLock(waitTime,leaseTime,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException("Redission Lock InterruptedException 加锁被中断");
        }
    }

    @Override
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.unlock();
    }
}
