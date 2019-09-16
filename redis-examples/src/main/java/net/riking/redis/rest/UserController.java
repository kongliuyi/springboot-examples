package net.riking.redis.rest;


import lombok.extern.slf4j.Slf4j;
import net.riking.redis.entity.User;
import net.riking.redis.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public Long add(@RequestBody User user) {
        log.debug("name:{}", user);
        return userService.add(user);
    }


    @DeleteMapping(value = "/{id}")
    public String delete(@PathVariable long id) {
        userService.delete(id);
        return "success";
    }


    @PutMapping()
    public String update(@RequestBody User user) {
        userService.update(user);
        return "success";
    }

    @GetMapping(value = "/{id}")
    public User get(@PathVariable long id) {
        log.debug("get with id:{}", id);
        return userService.get(id);
    }


    @GetMapping
    public User query(@RequestParam String username) {
        log.debug("query with username:{}", username);
        return userService.getByUsername(username);
    }




}
