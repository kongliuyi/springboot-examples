package net.riking.sharding.sphere4.algorithm;

import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 自定义实现复合分片算法
 *
 * @author kongLiuYi
 * @since 2021-11-25
 */
public class OrderComplexKeysShardingAlgorithm implements ComplexKeysShardingAlgorithm<BigDecimal> {

    /**
     * 订单id列名
     */
    private static final String COLUMN_ORDER_ID = "order_id";
    /**
     * 客户id列名
     */
    private static final String COLUMN_CUSTOMER_ID = "customer_id";

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, ComplexKeysShardingValue<BigDecimal> shardingValue) {
        if (!shardingValue.getColumnNameAndRangeValuesMap().isEmpty()) {
            throw new RuntimeException("不支持除了=和in的操作");
        }
        // 获取订单id
        Collection<BigDecimal> orderIds = shardingValue.getColumnNameAndShardingValuesMap().getOrDefault(COLUMN_ORDER_ID, new ArrayList<>(1));
        // 获取客户id
        Collection<BigDecimal> customerIds = shardingValue.getColumnNameAndShardingValuesMap().getOrDefault(COLUMN_CUSTOMER_ID, new ArrayList<>(1));

        // 整合订单id和客户id
        List<BigDecimal> ids = new ArrayList<>(16);
        ids.addAll(orderIds);
        ids.addAll(customerIds);

        return Collections.singleton(ids.stream()
                // 排序取第一个
                .sorted().findFirst()
                // 转换成int
                .map(BigDecimal::intValue)
                // 对可用的表名求余数，获取到真实的表的后缀
                .map(idSuffix -> idSuffix % availableTargetNames.size())
                // 转换成string
                .map(String::valueOf)
                // 获取到真实的表
                .map(tableSuffix -> availableTargetNames.stream().filter(targetName -> targetName.endsWith(tableSuffix)).findFirst().orElseThrow(() -> new RuntimeException("未找到表")))
                .get());
    }

}

