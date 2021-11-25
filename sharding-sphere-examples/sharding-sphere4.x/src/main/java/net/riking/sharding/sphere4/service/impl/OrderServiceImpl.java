package net.riking.sharding.sphere4.service.impl;

import net.riking.sharding.sphere4.entity.Order;
import net.riking.sharding.sphere4.mapper.OrderMapper;
import net.riking.sharding.sphere4.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kongLiuYi
 * @since 2021-11-23
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

}
