package net.riking.sharding.sphere4.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.riking.sharding.sphere4.entity.Order;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author kongLiuYi
 * @since 2021-11-23
 */
public interface OrderMapper extends BaseMapper<Order> {


    @Select("select t1.* from t_order t1 join t_order_item t2 on t1.order_id = t2.order_id where t1.order_id =  #{orderId}")
    Order joinSelect(@Param("orderId") Long orderId);

}
