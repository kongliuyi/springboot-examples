package net.riking.mybatis.mapper;

import net.riking.mybatis.entity.User;
import org.apache.ibatis.annotations.Select;


public interface UserMapper {
    @Select("SELECT  userName from t_oauth_user where id =#{id}")
    public User getUserById(long id);

}
