package com.example.demo.interceptor;

import com.example.demo.annotation.ReSubmitLock;
import com.example.demo.core.CacheKeyGenerator;
import com.example.demo.lock.FuckLock;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Aspect
@Configuration
public class ReSubmitMethodInterceptor {


    @Autowired
    public ReSubmitMethodInterceptor(FuckLock lockHelper, CacheKeyGenerator cacheKeyGenerator) {
        this.lockHelper = lockHelper;
        this.cacheKeyGenerator = cacheKeyGenerator;
    }

    private final FuckLock lockHelper;
    private final CacheKeyGenerator cacheKeyGenerator;


    @Around("execution(public * *(..)) && @annotation(com.example.demo.annotation.ReSubmitLock)")
    public Object interceptor(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        ReSubmitLock lock = method.getAnnotation(ReSubmitLock.class);
        if (StringUtils.isEmpty(lock.prefix())) {
            throw new RuntimeException("lock key don't null...");
        }
        final String lockKey = cacheKeyGenerator.getLockKey(pjp);
        try {
            // 假设上锁成功，但是设置过期时间失效，以后拿到的都是 false
            final boolean success = lockHelper.lock(lockKey, lock.waitTime(), lock.expire(), lock.timeUnit());
            if (!success) {
                throw new RuntimeException("重复提交");
            }
            try {
                return pjp.proceed();
            } catch (Throwable throwable) {
                throw new RuntimeException("系统异常");
            }
        } finally {
            // TODO 如果演示的话需要注释该代码;实际应该放开
//            lockHelper.unlock(lockKey);
        }
    }

}
