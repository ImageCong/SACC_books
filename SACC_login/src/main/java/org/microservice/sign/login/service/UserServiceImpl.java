package org.microservice.sign.login.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.microservice.pub.commons.entity.User;
import org.microservice.pub.commons.service.UserService;
import org.microservice.sign.login.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@Service(version = "1.0.0")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User loginCheck(String username, String password) {

        User user = userMapper.loginCheck(username, password);
        if (user == null) {
            log.info("==========登陆失败===========");
            return null;
        }

        log.info("==========登陆成功===========");
        return user;
    }

    @Override
    public User registry(String username, String password, String nickname) {

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setNickname(nickname);
        user.setCreateTime(new Date());

        int isRegistry = userMapper.registry(user);

        if (isRegistry == 1) {
            log.info("==========注册成功===========");
        }

        log.info("==========注册失败===========");
        return user;
    }

}
