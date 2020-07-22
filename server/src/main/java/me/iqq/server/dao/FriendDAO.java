package me.iqq.server.dao;

import me.iqq.server.entity.Friend;

import java.util.List;
import java.util.Map;

public interface FriendDAO {

    int linkFriend(String id1, String id2);

    int unlinkFriend(String id1, String id2);

    Friend getFriendById(String id);

    List<Friend> getFriendsById(String userId);

    Map<String, List<String>> getFriendsMapById(String userId);
}
