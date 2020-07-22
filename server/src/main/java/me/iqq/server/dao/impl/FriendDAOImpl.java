package me.iqq.server.dao.impl;

import me.iqq.server.core.DatabaseFactory;
import me.iqq.server.dao.FriendDAO;
import me.iqq.server.dao.handler.FriendListResultSetHandler;
import me.iqq.server.dao.handler.FriendResultSetHandler;
import me.iqq.server.entity.Friend;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendDAOImpl implements FriendDAO {
    @Override
    public int linkFriend(String id1, String id2) {
        QueryRunner qs = new QueryRunner(DatabaseFactory.getDataSource());
        Connection connection = null;
        // TODO: 确定类型
        Long rs = -1L;
        try {
            connection = DatabaseFactory.getDataSource().getConnection();
            connection.setAutoCommit(false);

            Friend friend = qs.query(connection, "select id, first_account, second_account, group_name_for_first, group_name_for_second, link_time, type from friend where first_account= ? and second_account=?" +
                    "union select id, first_account, second_account, group_name_for_first, group_name_for_second, link_time, type from friend where first_account=? and second_account=?",
                new FriendResultSetHandler(),
                id1, id2, id2, id1);

            if (friend == null) {
                rs = qs.insert(connection, "insert into friend(first_account, second_account) values(?,?)", new ScalarHandler<>(), id1, id2);
            }

            DbUtils.commitAndCloseQuietly(connection);
        } catch (SQLException sqlException) {
            DbUtils.rollbackAndCloseQuietly(connection);
            sqlException.printStackTrace();
        }
        return rs.intValue();
    }

    @Override
    public int unlinkFriend(String id1, String id2) {
        return 0;
    }

    @Override
    public Friend getFriendById(String id) {
        return null;
    }

    /**
     * 获取好友列表
     *
     * @return the friends
     */
    public List<Friend> getFriendsById(String userId) {
        QueryRunner qs = new QueryRunner(DatabaseFactory.getDataSource());
        List<Friend> result = null;
        try {
//            result = qs.query(/* sql */"select id, target_account, group_name, type from friends where origin_account=?",
//                /* friend bean list handler */new BeanListHandler<>(Friend.class),
//                /* parameters */qq);
            result = qs.query(/* sql */"select id, first_account, second_account, group_name_for_first, group_name_for_second, link_time, type from friend where first_account=?" +
                    " union " +
                    "select id, first_account, second_account, group_name_for_first, group_name_for_second, link_time, type from friend where second_account=?",
                /* friend bean list handler */new FriendListResultSetHandler(),
                /* parameters */userId, userId);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return result;
    }

    @Override
    public Map<String, List<String>> getFriendsMapById(String userId) {
        QueryRunner qs = new QueryRunner(DatabaseFactory.getDataSource());
        List<Friend> result = null;
        try {
            result = qs.query(/* sql */"select id, first_account, second_account, group_name_for_first, group_name_for_second, link_time, type from friend where first_account=?" +
                    " union " +
                    "select id, first_account, second_account, group_name_for_first, group_name_for_second, link_time, type from friend where second_account=?",
                /* friend bean list handler */new FriendListResultSetHandler(),
                /* parameters */userId, userId);

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return convertFriendListToMap(result, userId);
    }

    /**
     * @param friendList List 朋友关系列表
     * @param qq         当前账号
     * @return 一个以分组名字为键，以对应键为分组名的好友列表的map
     */
    private Map<String, List<String>> convertFriendListToMap(List<Friend> friendList, String qq) {
        if (friendList == null)
            return null;

        Map<String, List<String>> hashMap = new HashMap<>();
        for (var f : friendList) {
            if (f.getFirstAccount().equals(qq)) {
                if (hashMap.containsKey(f.getGroupNameForFirst())) {
                    hashMap.get(f.getGroupNameForFirst()).add(f.getSecondAccount());
                } else {
                    List<String> l = new ArrayList<>();
                    l.add(f.getSecondAccount());
                    hashMap.put(f.getGroupNameForFirst(), l);
                }
            } else {
                if (hashMap.containsKey(f.getGroupNameForFirst())) {
                    hashMap.get(f.getGroupNameForFirst()).add(f.getFirstAccount());
                } else {
                    List<String> l = new ArrayList<>();
                    l.add(f.getFirstAccount());
                    hashMap.put(f.getGroupNameForFirst(), l);
                }
            }
        }
        return hashMap;
    }
}
