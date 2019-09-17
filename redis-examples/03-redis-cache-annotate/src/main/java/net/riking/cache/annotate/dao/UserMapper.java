package net.riking.cache.annotate.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import net.riking.cache.annotate.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserMapper extends BaseMapper<User> {
}