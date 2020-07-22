package me.iqq.server.dao.handler;

import me.iqq.server.entity.User;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserResultSetHandler implements ResultSetHandler<User> {

    /**
     * Turn the <code>ResultSet</code> into an Object.
     *
     * @param rs The <code>ResultSet</code> to handle.  It has not been touched
     *           before being passed to this method.
     * @return An Object initialized with <code>ResultSet</code> data. It is
     * legal for implementations to return <code>null</code> if the
     * <code>ResultSet</code> contained 0 rows.
     * @throws SQLException if a database access error occurs
     */
    @Override
    public User handle(ResultSet rs) throws SQLException {
        if (!rs.next())
            return null;

        return (new User().setId(rs.getString("id"))
            .setNickname(rs.getString("nickname"))
            .setAvatar(rs.getString("avatar"))
            .setSignature(rs.getString("signature"))
            .setPhone(rs.getString("phone"))
            .setEmail(rs.getString("email"))
            .setPassword(rs.getString("password"))
            .setSalt(rs.getString("salt"))
            .setLogin_ip(rs.getString("login_ip"))
            .setBirthday(rs.getTimestamp("birthday"))
            .setRegisterTime(rs.getTimestamp("register_time"))
            .setStatus(rs.getInt("status")));
    }
}
