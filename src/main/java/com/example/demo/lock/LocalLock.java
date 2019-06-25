package com.example.demo.lock;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

public class LocalLock implements FuckLock {

    private static final Cache<String, Object> CACHES = CacheBuilder.newBuilder()
            // 最大缓存 100 个
            .maximumSize(1000)
            // 设置写缓存后 5 秒钟过期
            .expireAfterWrite(5, TimeUnit.SECONDS)
            .build();

    @Override
    public boolean lock(String lockKey) {
        if (!StringUtils.isEmpty(lockKey)) {
            //如果不存在
            if (CACHES.getIfPresent(lockKey) != null) {
                throw new RuntimeException("请勿重复请求");
            }
            // 如果是第一次请求,就将 key 当前对象压入缓存中
            CACHES.put(lockKey, lockKey);
            return true;
        }else {
            throw new RuntimeException("lockKey not be null or '' in " + this.getClass().getName());
        }
    }

    @Override
    public boolean lock(String lockKey, long leaseTime, TimeUnit timeUnit) {
        return lock(lockKey);
    }

    @Override
    public boolean lock(String lockKey, long waitTime, long leaseTime, TimeUnit timeUnit) {
        return lock(lockKey);
    }

    @Override
    public void unlock(String lockKey) {
        if (StringUtils.isEmpty(lockKey)) {
            throw new RuntimeException("lockKey not be null or '' in " + this.getClass().getName());
        }
        CACHES.invalidate(lockKey);
    }
}
