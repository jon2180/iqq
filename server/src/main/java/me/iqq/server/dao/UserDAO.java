package me.iqq.server.dao;

import me.iqq.server.entity.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO {

    int save(User accounts) throws SQLException;

    int remove(String qq) throws SQLException;

    int update(User accounts) throws SQLException;

    User find(String qq) throws SQLException;

    /**
     * 通过用户 id 数组来查找 user
     *
     * @param ids id 数组
     * @return
     */
    List<User> findUserByIds(String... ids);
}

