package me.iqq.server.dao.impl;

import me.iqq.server.core.DatabaseFactory;
import me.iqq.server.dao.UserDAO;
import me.iqq.server.dao.handler.UserResultSetHandler;
import me.iqq.server.entity.User;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    @Override
    public int save(User accounts) {
        Long result = -1L;
        try {
            result = new QueryRunner(DatabaseFactory.getDataSource())
                .insert(/* sql */"insert into accounts (id, nickname, email, password, salt) values(?,?,?,?,?)",
                    /* result set handler */new ScalarHandler<>(),
                    /* params */accounts.getId(), accounts.getNickname(), accounts.getPassword());
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return result.intValue();
    }

    @Override
    public int remove(String qq) throws SQLException {
        return 0;
    }

    @Override
    public int update(User accounts) throws SQLException {
        return 0;
    }

    @Override
    public User find(String qq) {
        User accounts = null;
        try {
            accounts = new QueryRunner(DatabaseFactory.getDataSource())
                .query(/* sql */"SELECT id, nickname, avatar, signature, phone, email, password, salt, login_ip, birthday, register_time, status FROM accounts  where id = ?",
                    /* result set handle */new UserResultSetHandler(),
                    /* params */qq
                );
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return accounts;
    }

    @Override
    public List<User> findUserByIds(String... ids) {

        List<User> users = new ArrayList<>();

        QueryRunner qs = new QueryRunner();
        Connection connection = null;
        try {
            connection = DatabaseFactory.getDataSource().getConnection();
            connection.setAutoCommit(false);
            for (String s : ids) {
                User user = qs.query(connection,
                    /* sql */"SELECT id, nickname, avatar, signature, phone, email, password, salt, login_ip, birthday, register_time, status from accounts where id=?",
                    /* result set handle */new UserResultSetHandler(),
                    /* params */s
                );
                if (user != null)
                    users.add(user);
            }
            DbUtils.commitAndCloseQuietly(connection);
        } catch (SQLException sqlException) {
            DbUtils.rollbackAndCloseQuietly(connection);
            sqlException.printStackTrace();
        }

        return users;
    }

}
