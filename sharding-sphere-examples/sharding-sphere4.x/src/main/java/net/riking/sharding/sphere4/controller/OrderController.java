package net.riking.sharding.sphere4.controller;


import net.riking.sharding.sphere4.service.IOrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author kongLiuYi
 * @since 2021-11-23
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private IOrderService orderService;

    @GetMapping(value = "/save")
    public String save() {
        orderService.saveOrder();
        return "成功";
    }

}
