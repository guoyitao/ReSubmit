# 基于本地缓存或者Redis的重复提交解决方案 Duplicate submission solution based on local cache or Redis

## 技术项 Technical item
springboot，redis ，guava

## 开始 Start

#### 简单的配置一下redission Simple configuration of redission
```@Configuration
 public class RedissonConfig {
 
     @Value("${spring.redis.host}")
     private String host;
 
     @Value("${spring.redis.port}")
     private String port;
 
 //    @Value("${spring.redis.password}")
 //    private String password;
 
     @Bean
     public RedissonClient getRedisson(){
 
         Config config = new Config();
         config.useSingleServer().setAddress("redis://" + host + ":" + port);
         //添加主从配置
 //        config.useMasterSlaveServers().setMasterAddress("").setPassword("").addSlaveAddress(new String[]{"",""});
 
         return Redisson.create(config);
     }
 
 }
```
#### 配置重复提交的相关组件 Configure related components for repeated submissions

You can define the CacheKeyGenerator key generator yourself.
你可以自己定义CacheKeyGenerator key生成器
你只要new LocalLock();就可以快速从redis的缓存换成本地的缓存
```
  @Configuration
  public class ReSubmitConfig {
 
     @Bean
     public CacheKeyGenerator cacheKeyGenerator(){
         return new LockKeyGenerator();
     }
 
     @Bean
     public FuckLock fuckLock(){
         return new RedisLockHelper();
          //return new LocalLock();
     }
  }
```


#### 你可以这样开始 You can start like this
```
@ReSubmitLock(prefix = "/lock",waitTime = 100,expire = 100)
     @GetMapping("/lock")
     public String query(@RequestParam String token) {
         return "success - " + token;
}
 ```