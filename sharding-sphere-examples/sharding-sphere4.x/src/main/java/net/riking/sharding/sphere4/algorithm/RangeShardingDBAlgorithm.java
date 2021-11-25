package net.riking.sharding.sphere4.algorithm;

import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;


/**
 * 自定义实现 数据库DB范围分片算法（RangeShardingAlgorithm）接口
 *
 * BETWEEN AND进行分片的场景
 *
 * @author kongLiuYi
 * @since 2021-11-25
 */
public class RangeShardingDBAlgorithm implements RangeShardingAlgorithm<Long> {


    @Override
    public Collection<String> doSharding(final Collection<String> databaseNames, final RangeShardingValue<Long> shardingValue) {

        /*
         * 自定义SQL -> SELECT *  FROM t_order WHERE order _id Between 2000 and 4000
         * ds0.t_order: 1000 ~ 3000
         * ds1.t_order: 3001 ~ 5000
         * ds2.t_order: 5001 ~ 7000
         *
         * 执行路由后的SQL 应为：
         * SELECT *  FROM ds0.t_order WHERE order _id Between 2000 and 3000
         * SELECT *  FROM ds1.t_order WHERE order _id Between 3001 and 4000
         */
        Set<String> result = new LinkedHashSet<>();
        // 从sql 中获取 Between 2000 and 4000   的值，将2000 赋值给 lower,  4000 赋值给 upper
        Long lower = shardingValue.getValueRange().lowerEndpoint();
        Long upper = shardingValue.getValueRange().upperEndpoint();
        // 范围间距超过 2000，不支持
        if (upper - lower > 2000) {
            throw new UnsupportedOperationException();
        }
        for (Long i = lower; i <= upper; i++) {
            // db0,db1
            for (String each : databaseNames) {
                if (each.endsWith(i % databaseNames.size() + "")) {
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