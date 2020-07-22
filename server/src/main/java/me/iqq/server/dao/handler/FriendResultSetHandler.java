package me.iqq.server.dao.handler;

import me.iqq.server.entity.Friend;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FriendResultSetHandler implements ResultSetHandler<Friend> {
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
    public Friend handle(ResultSet rs) throws SQLException {
        if (!rs.next())
            return null;

        return (new Friend()
            .setId(rs.getInt("id"))
            .setFirstAccount(rs.getString("first_account"))
            .setSecondAccount(rs.getString("second_account"))
            .setGroupNameForFirst(rs.getString("group_name_for_first"))
            .setGroupNameForSecond(rs.getString("group_name_for_second"))
            .setLinkTime(rs.getTimestamp("link_time"))
            .setType(rs.getInt("type"))
        );
    }
}
