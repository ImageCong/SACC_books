package org.microservice.sign.login.mapper;

import org.apache.ibatis.annotations.Param;
import org.microservice.pub.commons.entity.User;

import java.util.List;

public interface UserMapper {
    // 根据ID查找用户
    User selectById(User user);

    // 登录验证
    User loginCheck(@Param("username") String username, @Param("password") String password);

    // 列出所有的用户
    List<User> listUsers();

    // 注册
    int registry(User user);
}
