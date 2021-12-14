package net.riking.sharding.sphere4;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.riking.sharding.sphere4.entity.Config;
import net.riking.sharding.sphere4.entity.Customer;
import net.riking.sharding.sphere4.entity.Order;
import net.riking.sharding.sphere4.entity.OrderItem;
import net.riking.sharding.sphere4.mapper.OrderMapper;
import net.riking.sharding.sphere4.service.CustomerService;
import net.riking.sharding.sphere4.service.IConfigService;
import net.riking.sharding.sphere4.service.IOrderItemService;
import net.riking.sharding.sphere4.service.IOrderService;
import org.apache.shardingsphere.api.hint.HintManager;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ShardingSphere4Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShardingSphereTests {
    @Resource
    private IOrderService orderService;

    @Resource
    private IOrderItemService orderItemService;

    @Resource
    private IConfigService configService;

    @Resource
    private CustomerService customerService;


    @Resource
    private OrderMapper orderMapper;

    /**
     * 测试绑定表
     */
    @Test
    @Transactional
    @Rollback(false)
    public void saveOrder() {
        List<Order> orderList = new ArrayList<>();
        List<OrderItem> orderItemList = new ArrayList<>();
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        for (int i = 0; i < 4; i++) {
            Long id = snowflake.nextId();
            Order order = new Order();
            order.setOrderId(id);
            order.setOrderNo("A000" + i);
            order.setCreateName("订单 " + i);
            order.setPrice(new BigDecimal("" + i));
            order.setCustomerId(1462741857441579010L);
            orderList.add(order);
            //orderService.save(order);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getOrderId());
            orderItem.setOrderNo("A000" + i);
            orderItem.setItemName("服务项目" + i);
            orderItem.setPrice(new BigDecimal("" + i));
            orderItemList.add(orderItem);
            //orderItemService.save(orderItem);
        }
        orderService.saveBatch(orderList);
        orderItemService.saveBatch(orderItemList);
    }


    @Test
    public void selectOrderPage() {
        // SELECT  id,name,age  FROM t_order  ORDER BY id ASC LIMIT ?,? ::: [0, 4000] -> 查询 内存会不会爆满？
        Page<Order> page = new Page<>(4, 4);
        QueryWrapper<Order> qw = new QueryWrapper<>();
        qw.orderByAsc("order_id");
        Page<Order> orderPage = orderService.page(page, qw);
        System.out.println(orderPage.getRecords().toString());
    }

    @Test
    public void selectOrderJoin() {
        Order order = orderMapper.joinSelect(1463084806563827712L);
        System.out.println(order);

    }


    @Test
    public void selectBetweenOrder() {
        // 策略不允许 抛出异常：Inline strategy cannot support this type sharding:RangeRouteValue
        QueryWrapper<Order> qw = new QueryWrapper<>();
        qw.between("order_id", 669958008452677632L, 669958008452677642L);
        List<Order> orderList = orderService.list(qw);
        System.out.println(orderList.toString());
    }

    /**
     * 测试标准分片策略
     */
    @Test
    public void saveCustomer() {
        List<Customer> customerList = new ArrayList<>();
        Snowflake snowflake = IdUtil.getSnowflake(1, 1);
        for (int i = 0; i < 100; i++) {
            String idStr = snowflake.nextIdStr();
            customerList.add(Customer.builder().address(idStr)
                    .phone(idStr.substring(idStr.length() - 11)).build());
        }
        customerService.saveBatch(customerList);
    }


    @Test
    public void selectInCustomer() {
        QueryWrapper<Customer> qw = new QueryWrapper<>();
        qw.in("id", 1463341470996299783L, 1463341470929190913L, 1463341470996299781L, 1463341466751664130L
                , 1463341470996299782L, 1463341470996299778L);
        List<Customer> customerList = customerService.list(qw);
        System.out.println(customerList.toString());
    }


    @Test
    public void selectBetweenCustomer() {
        QueryWrapper<Customer> qw = new QueryWrapper<>();
        qw.between("id", 1463341470996299783L, 1463341470996299983L);
        List<Customer> customerList = customerService.list(qw);
        System.out.println(customerList.toString());
    }


    /**
     * 测试广播表
     */
    @Test
    public void saveConfig() {
        configService.save(Config.builder().createTime(LocalDateTime.now()).remark("测试")
                .lastModifyTime(LocalDateTime.now()).build());
    }



    /**
     *
     * 强制走主库
     *
     */
    @Test
    public void masterRouteOnly() {
        // 强制走主库
        HintManager.getInstance().setMasterRouteOnly();
        this.selectInCustomer();

        this.selectInCustomer();
        // 清除强制配置
        HintManager.clear();
        System.out.println("----------    分割线   --------");
        this.selectInCustomer();
    }


    /**
     *
     * 强制走主库
     *
     */
    @Test
   // @Transactional
    public void masterRouteOnly2() {
        // 强制走主库
        HintManager.getInstance().setMasterRouteOnly();
        QueryWrapper<Customer> qw = new QueryWrapper<>();
        qw.in("id", 1463341470996299783L, 1463341470929190913L);
        List<Customer> customerList = customerService.list(qw);
        List<Customer> customerList2 = customerService.list(qw);
        System.out.println(customerList.toString());
    }


    /**
     *
     * 强制走主库
     *
     */
    @Test
    @Transactional(rollbackFor = Exception.class)
    // // 支持TransactionType.LOCAL, TransactionType.XA, TransactionType.BASE
    @ShardingTransactionType(TransactionType.XA)
    @Rollback(false)
    public void shardingTransaction() {

       this.saveOrder();

       throw  new RuntimeException();
    }


}
