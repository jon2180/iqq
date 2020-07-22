package me.iqq.server.dao;

import me.iqq.server.dao.impl.FriendDAOImpl;
import me.iqq.server.entity.Friend;
import me.iqq.server.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class FriendDAOTest {

    FriendDAO friendDAO = new FriendDAOImpl();

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void linkFriend() {
    }

    @Test
    void unlinkFriend() {
    }

    @Test
    void getFriendById() {
    }

    @Test
    void getAllFriends() {
        String qq = "2345678";
        List<Friend> list = friendDAO.getFriendsById(qq);

        assertNotNull(list);

        for (var f : list) {
            assertNotNull(f.getFirstAccount());
            System.out.println(f);
        }

        ConcurrentHashMap<String, List<User>> hashMap = new ConcurrentHashMap<>();
        for (var f : list) {
            if (f.getFirstAccount().equals(qq)) {
                User user = new User().setId(f.getSecondAccount());
                if (hashMap.containsKey(f.getGroupNameForFirst())) {
                    hashMap.get(f.getGroupNameForFirst()).add(user);
                } else {
                    List<User> l = new ArrayList<>();
                    l.add(user);
                    hashMap.put(f.getGroupNameForFirst(), l);
                }
            } else {
                User user = new User().setId(f.getFirstAccount());
                if (hashMap.containsKey(f.getGroupNameForFirst())) {
                    hashMap.get(f.getGroupNameForFirst()).add(user);
                } else {
                    List<User> l = new ArrayList<>();
                    l.add(user);
                    hashMap.put(f.getGroupNameForFirst(), l);
                }
            }
        }
        Set<Map.Entry<String, List<User>>> u = hashMap.entrySet();
        for (var e : u) {
            System.out.println(e.getKey());
            for (var v : e.getValue()) {
                System.out.println(v.getId());
            }
        }
    }

    public void testGetFriendsMapById() {
        String id = "123456";
        Map<String, List<String>> listMap = friendDAO.getFriendsMapById(id);

        var entrySet = listMap.entrySet();
        for (var e : entrySet) {
            System.out.println(e.getKey());
            System.out.println(e.getValue());
        }
    }
}
