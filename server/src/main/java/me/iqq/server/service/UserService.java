package me.iqq.server.service;

import me.iqq.server.entity.User;

public interface UserService {

    int register(String nickname, String password, String email);

    /**
     * 登录
     *
     * @param id       用户id
     * @param password 用户密码
     * @return statusCode [0 - okay ；1 - 密码错误； 2 - 没有此用户]
     */
    int login(String id, String password);

    User getUserByID(String qq);

    User getUserByName(String name);

}
