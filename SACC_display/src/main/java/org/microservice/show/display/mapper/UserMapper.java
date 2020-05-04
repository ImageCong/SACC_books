package org.microservice.show.display.mapper;

import org.microservice.pub.commons.entity.User;

/**
 * 这个UserMapper和sacc_login模块的UserMapper查的是一张表
 * 但是查的内容不同
 */
public interface UserMapper {
    // 根据ID查找对应的User记录
    // 这里的查找是为了做以后的扩展，如果前台只保存User的id，那么就需要通过此处的查找
    // 拿到对应的userCode，再去查找推荐记录
    User selectUserById(Integer id);
}
