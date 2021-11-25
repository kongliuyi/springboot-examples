package net.riking.sharding.sphere4.algorithm;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;


/**
 * 自定义实现 表精准分片算法（PreciseShardingAlgorithm）接口
 *
 * @author kongLiuYi
 * @since 2021-11-25
 */
@Slf4j
public class PreciseShardingTableAlgorithm implements PreciseShardingAlgorithm<Long> {

    @Override
    public String doSharding(Collection<String> tableNames, PreciseShardingValue<Long> shardingValue) {
        for (String key : tableNames) {
            if (key.endsWith(String.valueOf(shardingValue.getValue() % tableNames.size() + 1))) {
                log.warn("tableName：{}", key);
                return key;
            }
        }
        log.error("value:{}, tableNames:{}", shardingValue.getValue(), tableNames);
        throw new UnsupportedOperationException();
    }

}