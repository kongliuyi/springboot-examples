package net.riking.sharding.sphere4.service.impl;

import net.riking.sharding.sphere4.entity.OrderItem;
import net.riking.sharding.sphere4.mapper.OrderItemMapper;
import net.riking.sharding.sphere4.service.IOrderItemService;
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
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements IOrderItemService {

}
