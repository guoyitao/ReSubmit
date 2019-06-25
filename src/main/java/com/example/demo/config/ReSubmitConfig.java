package com.example.demo.config;


import com.example.demo.core.CacheKeyGenerator;
import com.example.demo.core.LockKeyGenerator;
import com.example.demo.lock.FuckLock;
import com.example.demo.lock.LocalLock;
import com.example.demo.lock.RedisLockHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReSubmitConfig {

    @Bean
    public CacheKeyGenerator cacheKeyGenerator(){
        return new LockKeyGenerator();
    }

    @Bean
    public FuckLock fuckLock(){
        return new RedisLockHelper();

//        return new LocalLock();
    }
}
