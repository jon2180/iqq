package me.iqq.server.db;

import me.iqq.common.protocol.Friend;
import me.iqq.common.protocol.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class DBUtils {

    private static DatabaseConnection dbConn = new DatabaseConnection();

    /**
     * 获取好友列表
     *
     * @return the friends
     */
    public static Vector<Friend> getAllFriends(String qq) {
        Vector<Friend> friends = new Vector<>();

//        DatabaseConnection dbConn = new DatabaseConnection();

        String sql = "select id, target_account, group_name, type from friends where origin_account='" + qq + "'";

        try {
            ResultSet res = dbConn.query(sql);
            while (res.next()) {
                int friendId = res.getInt("id");
                String targetAccount = res.getString("target_account");
                String groupName = res.getString("group_name");
                int type = res.getInt("type");

                friends.add(new Friend(friendId, qq, targetAccount, groupName, type));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return friends;
    }

    public static ResultSet find(String qq) {
        String sql = "SELECT id,qnumber,nickname,password FROM accounts where qnumber = '" + qq + "'";
        ResultSet resultSet = null;
        try {
            resultSet = dbConn.query(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static void createUser(User user) {
//        DatabaseConnection dbConn = new DatabaseConnection();
        String sql = "insert into accounts (qnumber, nickname, password) values('" + user.getQQ() + "','"
            + user.getNickname() + "', '" + user.getPassword() + "')";
        try {
            dbConn.update(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
