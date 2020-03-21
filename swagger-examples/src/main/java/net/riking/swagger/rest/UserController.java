package net.riking.swagger.rest;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.DynamicParameter;
import io.swagger.annotations.DynamicParameters;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import net.riking.swagger.entity.User;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags="用户管理",position = 1)
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {


    @PostMapping
    @ApiOperation(value="添加用户",notes = "新增单个用户")
    public Long add(@RequestBody User user) {
        log.debug("name:{}", user);
        return  11L;
    }


    @DeleteMapping(value = "/{id}")
    @ApiOperation(value="删除用户",notes = "通过ID删除单个用户")
    public String delete(@PathVariable long id) {

        return "success";
    }


    @PutMapping()
    @ApiOperation(value="更新用户",notes = "更新单个用户")
    public String update(@RequestBody User user) {

        return "success";
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value="查询用户",notes = "通过ID查询单个用户")
    public User get(@PathVariable long id) {
        log.debug("get with id:{}", id);
        return  new User();

    }


    @GetMapping
    @ApiOperation(value="查询用户",notes = "通过用户名查询单个用户")
    public User query(@RequestParam String username) {
        log.debug("query with username:{}", username);
         return  new User();
    }


    @PostMapping("/createOrder422")
    @ApiOperation(value = "jdk-Map-动态创建显示参数")
    @DynamicParameters(name = "CreateOrderMapModel",properties = {
            @DynamicParameter(name = "id",value = "注解id",example = "X000111",required = true,dataTypeClass = Integer.class),
            @DynamicParameter(name = "name",value = "订单编号"),
            @DynamicParameter(name = "name1",value = "订单编号1"),
    })
    public String createOrder12232(@RequestBody Map map){
        return "success";
    }

}
