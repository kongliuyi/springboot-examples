package net.riking.redis.config;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;


/**
 * @Description 原生Redis配置(可按照此方式配置多个Redis)
 * @Author kongLiuYi
 * @Date 2019/9/16 0016 20:02
 */
@Configuration
@EnableCaching //启用缓存
//@ConfigurationProperties(prefix = "spring.redis") //指明配置节点*/
public class RedisConfig extends CachingConfigurerSupport {

    // Redis服务器地址
    @Value("${spring.redis.host:127.0.0.1}")
    // Redis服务器连接端口
    private String host;
    @Value("${spring.redis.port:6379}")
    private Integer port;
    // Redis服务器连接密码（默认为空）
    @Value("${spring.redis.password:}")
    private String password;
    // Redis数据库索引（默认为0）
    @Value("${spring.redis.database:0}")
    private Integer database;
    // 连接超时时间（毫秒）
    @Value("${spring.redis.timeout:10000}")
    private Integer timeout;

    // 连接池最大连接数（使用负值表示没有限制）
    @Value("${spring.redis.lettuce.pool.max-active:8}")
    private Integer maxActive;
    // 连接池中的最大空闲连接
    @Value("${spring.redis.lettuce.pool.max-idle:8}")
    private Integer maxIdle;
    // 连接池最大阻塞等待时间（使用负值表示没有限制）
    @Value("${spring.redis.lettuce.pool.max-wait:-1}")
    private Long maxWait;
    // 连接池中的最小空闲连接
    @Value("${spring.redis.lettuce.pool.min-idle:0}")
    private Integer minIdle;
    // 关闭超时时间
    @Value("${spring.redis.lettuce.shutdown-timeout:0}")
    private Integer shutdown;


    /**
     * 自定义缓存key的生成策略。默认的生成策略是看不懂的(乱码内容) 通过Spring 的依赖注入特性进行自定义的配置注入并且此类是一个配置类可以更多程度的自定义配置
     *
     * @return
     */
/*
    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                return sb.toString();
            }

        };
    }*/


    /**
     * 获取缓存连接池
     *
     * @return
     */
    @Bean
    public LettuceClientConfiguration lettuceClientConfiguration() {
        GenericObjectPoolConfig  poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(maxActive);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxWaitMillis(maxWait);
        return LettucePoolingClientConfiguration
                .builder().poolConfig(poolConfig)
                .commandTimeout(Duration.ofMillis(timeout))
                .shutdownTimeout(Duration.ofMillis(shutdown)).build();
    }

    @Bean
    public RedisStandaloneConfiguration redisStandaloneConfiguration() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setDatabase(database);
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
        return redisStandaloneConfiguration;
    }


    @Bean
    LettuceConnectionFactory lettuceConnectionFactory( RedisStandaloneConfiguration redisStandaloneConfiguration,LettuceClientConfiguration lettuceClientConfiguration) {
        return new LettuceConnectionFactory(redisStandaloneConfiguration,lettuceClientConfiguration);
    }

    /**
     * 缓存配置管理器
     */
    @Bean
    public CacheManager cacheManager(LettuceConnectionFactory lettuceConnectionFactory) {
        //设置缓存过期时间
        RedisCacheConfiguration redisCacheCfg= RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(1));
        return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(lettuceConnectionFactory))
                .cacheDefaults(redisCacheCfg)
                .build();
    }

    /**
     * 获取缓存操作助手对象
     *
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        //使用GenericJackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        template.setValueSerializer(jackson2JsonRedisSerializer());
        //template.setEnableTransactionSupport(true);//是否启用事务
        template.setConnectionFactory(lettuceConnectionFactory); // 配置连接工厂
        template.afterPropertiesSet();
        return template;
    }

    /**
     *   此种序列化方式结果清晰、容易阅读、存储字节少、速度快，所以推荐更换  {详细链接 https://www.cnblogs.com/JKayFeng/p/5911549.html }
     * @return
     */
    private Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper());
        return jackson2JsonRedisSerializer;
    }

    private ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        return objectMapper;
    }


    /**
     * 对hash类型的数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    /**
     * 对redis字符串类型数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ValueOperations<String, Object> valueOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    /**
     * 对链表类型的数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForList();
    }

    /**
     * 对无序集合类型的数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    /**
     * 对有序集合类型的数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForZSet();
    }
}
