package org.microservice.pub.commons.service;

import org.microservice.pub.commons.entity.User;

public interface UserService {

    /**
     * 登录验证 返回登录是否成功
     */
    User loginCheck(String username, String password);

    /**
     * 注册 返回注册是否成功
     */
    User registry(String username, String password, String nickname);
}
