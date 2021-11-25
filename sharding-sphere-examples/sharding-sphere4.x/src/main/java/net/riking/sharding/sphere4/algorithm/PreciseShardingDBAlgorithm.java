package net.riking.sharding.sphere4.algorithm;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;


/**
 * 自定义实现 库精准分片算法（PreciseShardingAlgorithm）接口
 *
 * @author kongLiuYi
 * @since 2021-11-25
 */
@Slf4j
public class PreciseShardingDBAlgorithm implements PreciseShardingAlgorithm<Long> {

    /**
     * @param databaseNames db0 , db1
     * @param shardingValue SQL 分片键对应的实际值
     * @return /
     */
    @Override
    public String doSharding(Collection<String> databaseNames, PreciseShardingValue<Long> shardingValue) {
        /*
         * 作用：散列到具体的哪个库里面去
         * shardingValue ： SQL -> SELECT *  FROM customer WHERE order _id IN(1,3,6)
         * shardingValue = [1,3,6]
         * */
        for (String each : databaseNames) {
            /*
             * 此方法如果参数所表示的字符序列是由该对象表示的字符序列的后缀返回true, 否则为false;
             *  请注意，如果参数是空字符串或等于此 String对象由equals（Object）方法确定结果为 true。
             *  db0.endsWith("0") -> true ;
             */
            if (each.endsWith(String.valueOf(shardingValue.getValue() % databaseNames.size()))) {
                //返回相应的数据库
                log.warn("value:{},db:{}", shardingValue.getValue(), each);
                return each;
            }
        }
        log.error("value:{}", shardingValue.getValue());
        throw new UnsupportedOperationException();
    }
}