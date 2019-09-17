package net.riking.cache.annotate.service.impl;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.riking.cache.annotate.dao.UserMapper;
import net.riking.cache.annotate.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import net.riking.cache.annotate.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public long add(User user) {
        return userMapper.insert(user);
    }

    @Override
    @CacheEvict(value = "user", key = "#root.targetClass.name+'-'+#id")
    public void delete(long id) {
        userMapper.deleteById(id);
    }

    @Override
    @CacheEvict(value = "user", key = "#root.targetClass.name+'-'+#user.id")
    public void update(User user) {
        userMapper.updateById(user);
    }

    @Override
    @Cacheable(value = "user", key = "#root.targetClass.name+'-'+#id")
    public User get(long id) {
        return userMapper.selectById(id);
    }

    @Override
    public User getByUsername(String username) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
    }

}
