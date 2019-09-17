package net.riking.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author kongLiuYi
 * @Date 2019/9/17 0017 17:53
 */
@Component
public class RedisOperate {


    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    ValueOperations<String, Object> valueOperations;


    public void valueOperations() throws InterruptedException {


        System.out.println("******    设置指定键的值。        set void set(K key, V value);              ******");
        valueOperations.set("name", "kongLiuYi");
        System.out.println("******     获取指定键的值。           get V get(Object key);                ******");
        System.out.println("****************结果：" + valueOperations.get("name"));

        System.out.println("******   /插入值并设置该值过期时间        set void set(K key, V value, long timeout, TimeUnit unit);             ******");
        valueOperations.set("name", "kongLiuYi", 10, TimeUnit.SECONDS);
        System.out.println("******十秒之内查询结果:" + valueOperations.get("name"));//由于设置的是10秒失效，十秒之内查询有结果
        Thread.sleep(10000);
        System.out.println("******十秒之后查询结果" + valueOperations.get("name"));//由于设置的是10秒失效，十秒之后返回为null


        System.out.println("******           set void set(K key, V value, long offset);            ******");
        //该方法是用 value 参数覆写(overwrite)给定 key 所储存的字符串值，从偏移量 offset 开始
        valueOperations.set("set", "hello world");
        valueOperations.set("set", "redis", 6);
        System.out.println("***************结果：" + valueOperations.get("set"));

        System.out.println("******   1.设置键的值，仅当键不存在时   2.存在反回 false ，反正true      setIfAbsent Boolean setIfAbsent(K key, V value);           ******");
        System.out.println("***************结果：" + valueOperations.setIfAbsent("setIfAbsent", "setIfAbsent1"));//之前不存在 true
        System.out.println("***************结果：" + valueOperations.get("setIfAbsent"));//设置键的值
        System.out.println("***************结果：" + valueOperations.setIfAbsent("setIfAbsent", "setIfAbsent2"));// 之前存在 false
        System.out.println("***************结果：" + valueOperations.get("setIfAbsent"));//值没有覆盖




        System.out.println("******  设置键的字符串值并返回其旧值          getAndSet V getAndSet(K key, V value);               ******");
        valueOperations.set("getAndSet", "getAndSe1");
        System.out.println("***************结果：" + valueOperations.getAndSet("getAndSet", "getAndSe2"));


        System.out.println("******   批量设置多个键的值        multiGet List<V> multiGet(Collection<K> keys);              ******");
        Map<String, String> maps = new HashMap<>();
        maps.put("multiGet1", "multiGet1");
        maps.put("multiGet2", "multiGet2");
        maps.put("multiGet3", "multiGet3");
        System.out.println("******  批量取出多个键的值         multiGet List<V> multiSet(Collection<K> keys);              ******");
        valueOperations.multiSet(maps);
        List<String> keys = new ArrayList<>();
        keys.add("multiGet1");
        keys.add("multiGet2");
        keys.add("multiGet3");
        System.out.println("***************结果：" + valueOperations.multiGet(keys));


        System.out.println("******  将键的整数值按给定的数值增加，初始值为0          increment Long increment(K key, long delta);              ******");
        valueOperations.increment("incrementLong", 1);
        System.out.println("***************结果：" + valueOperations.get("incrementLong"));
        valueOperations.increment("incrementLong", 1);
        System.out.println("***************结果：" + valueOperations.get("incrementLong"));


        System.out.println("******  将键的浮点值按给定的数值增加 ，初始值为0         increment Double increment(K key, double delta);            ******");
        valueOperations.increment("incrementDouble", 1.2);
        System.out.println("***************结果：" + valueOperations.get("incrementDouble"));
        valueOperations.increment("incrementDouble", 1.2);
        System.out.println("***************结果：" + valueOperations.get("incrementDouble"));


        System.out.println("******              append Integer append(K key, String value);            ******");
        // 如果key已经存在并且是一个字符串，则该命令将该值追加到字符串的末尾。如果键不存在，则它被创建并设置为空字符串，因此APPEND在这种特殊情况下将类似于SET。
        valueOperations.append("append", "Hello");
        // System.out.println(valueOperations.get("append"));
        valueOperations.set("append", "Hello");
        valueOperations.append("append", "world");
        System.out.println("***************结果：" + valueOperations.get("append"));


        System.out.println("******  截取key所对应的value字符串            get String get(K key, long start, long end);           ******");
        System.out.println("***************结果：" + valueOperations.get("append", 0, 5));
        System.out.println("***************结果：" + valueOperations.get("append", 0, -1));
        System.out.println("***************结果：" + valueOperations.get("append", -3, -1));


        System.out.println("******           //使用：appendTest对应的value为Helloworld\n           size Long size(K key);           ******");
        valueOperations.set("size", "hello world");
        System.out.println("***************结果：" + valueOperations.size("size"));


        System.out.println("******              setBit Boolean setBit(K key, long offset, boolean value);          ******");
        // 对 key 所储存的字符串值，设置或清除指定偏移量上的位(bit)
        //key键对应的值value对应的ascii码,在offset的位置(从左向右数)变为value
        valueOperations.set("setBit", "a");
        // 'a' 的ASCII码是 97。转换为二进制是：01100001
        // 'b' 的ASCII码是 98  转换为二进制是：01100010
        // 'c' 的ASCII码是 99  转换为二进制是：01100011
        //因为二进制只有0和1，在setbit中true为1，false为0，因此我要变为'b'的话第六位设置为1，第七位设置为0
        valueOperations.setBit("setBit", 6, true);
        valueOperations.setBit("setBit", 7, false);
        System.out.println("***************结果：" + valueOperations.get("setBit"));


        System.out.println("******   获取键对应值的ascii码的在offset处位值            getBit Boolean getBit(K key, long offset);         ******");
        System.out.println("***************结果：" + valueOperations.getBit("setBit", 7));
    }

}
