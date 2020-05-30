package net.riking.swagger.bootstrap.ui.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 用户对象
 * @Author: kongLiuYi
 * @Date: 2020/5/30 0030 17:44
 */
@Data
@ApiModel(value = "用户对象", description = "一个demo")
public class User implements Serializable {
    private Long id;
    @ApiModelProperty(value = "用户名", required = true)
    private String username;
    @ApiModelProperty(value = "用户密码", required = true)
    private String password;
    @ApiModelProperty(value = "别名")
    private String name;
    @ApiModelProperty(value = "手机号码", required = true)
    private String mobile;

}

