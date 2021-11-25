package net.riking.sharding.sphere4.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author kongLiuYi
 * @since 2021-11-23
 */
@TableName("t_order_item")
@Data
@Builder
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long itemId;

    private Long orderId;

    private String orderNo;

    private String itemName;

    private BigDecimal price;


}
