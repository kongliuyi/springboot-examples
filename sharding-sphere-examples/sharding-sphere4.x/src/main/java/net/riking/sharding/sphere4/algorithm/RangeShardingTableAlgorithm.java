package net.riking.sharding.sphere4.algorithm;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 自定义实现 表范围分片算法（RangeShardingAlgorithm）接口
 *
 * BETWEEN AND进行分片的场景
 *
 * @author kongLiuYi
 * @since 2021-11-25
 */
@Slf4j
public class RangeShardingTableAlgorithm implements RangeShardingAlgorithm<Long> {

    @Override
    public Collection<String> doSharding(final Collection<String> tableNames, final RangeShardingValue<Long> shardingValue) {
        Set<String> result = new LinkedHashSet<>();
        Long lower = shardingValue.getValueRange().lowerEndpoint();
        Long upper = shardingValue.getValueRange().upperEndpoint();
        // 范围间距超过 2000，不支持
        if (upper - lower > 2000) {
            throw new UnsupportedOperationException();
        }
        for (Long i = lower; i <= upper; i++) {
            for (String each : tableNames) {
                if (each.endsWith((i % tableNames.size() + 1) + "")) {
                    log.warn("value:{},tableName:{}", i, each);
                    result.add(each);
                    break;
                }
            }
        }
        if (result.isEmpty()) {
            throw new UnsupportedOperationException();
        }
        return result;
    }

}