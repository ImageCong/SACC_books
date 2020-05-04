package org.microservice.sign.login;

import org.junit.jupiter.api.Test;
import org.microservice.pub.commons.entity.User;
import org.microservice.pub.commons.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LoginApplicationTests {
    @Autowired
    UserService userService;

    @Test
    void contextLoads() {
        User b = userService.loginCheck("admin", "admin");
        System.out.println(b);
    }

}
