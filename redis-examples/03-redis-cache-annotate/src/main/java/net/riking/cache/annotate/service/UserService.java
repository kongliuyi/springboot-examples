package net.riking.cache.annotate.service;


import net.riking.cache.annotate.entity.User;

public interface   UserService {

      /**
       * 获取用户
       * @param id
       * @return
       */
      User get(long id);

      /**
       * 根据用户名获取用户信息
       * @param username
       * @return
       */
      User getByUsername(String username);

      /**
       * 新增用户
       *
       * @param user
       * @return
       */
      long add(User user);

      /**
       * 查询用户
       *
       * @return
       */
    //  List<User> query(UserQueryParam userQueryParam);

      /**
       * 更新用户信息
       *
       * @param user
       */
      void update(User user);

      /**
       * 根据id删除用户
       *
       * @param id
       */
      void delete(long id);
}
