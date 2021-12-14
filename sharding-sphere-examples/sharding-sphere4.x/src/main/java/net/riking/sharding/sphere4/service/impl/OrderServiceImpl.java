package net.riking.sharding.sphere4.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.riking.sharding.sphere4.entity.Order;
import net.riking.sharding.sphere4.entity.OrderItem;
import net.riking.sharding.sphere4.mapper.OrderMapper;
import net.riking.sharding.sphere4.service.IOrderItemService;
import net.riking.sharding.sphere4.service.IOrderService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author kongLiuYi
 * @since 2021-11-23
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Resource
    private IOrderItemService orderItemService;

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 测试绑定表
     */
    // @ShardingTransactionType(TransactionType.XA)
    @Transactional(rollbackFor = Exception.class)
    public void saveOrder() {
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        Long id = snowflake.nextId();
        Order order = new Order();
        order.setOrderId(11L);
        order.setOrderNo("A000A");
        order.setCreateName("订单18");
        order.setPrice(new BigDecimal("30"));
        order.setCustomerId(1462741857441579010L);
        this.save(order);
        OrderItem orderItem = new OrderItem();
        orderItem.setItemId(671038120275214338L);
        orderItem.setOrderId(1464158023718146050L);
        orderItem.setOrderNo("30000");
        orderItemService.save(orderItem);
        //   throw new RuntimeException();
    }


}
