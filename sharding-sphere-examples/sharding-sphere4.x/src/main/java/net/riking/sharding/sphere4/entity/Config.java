package net.riking.sharding.sphere4.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author kongLiuYi
 * @since 2021-11-23
 */
@TableName("t_config")
@Data
@Builder
@ToString
public class Config implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime lastModifyTime;

}
