package com.neutron.im.service;

import com.neutron.im.core.entity.Friend;
import com.neutron.im.mapper.FriendMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class FriendService {

    private final FriendMapper friendMapper;

    @Autowired
    public FriendService(FriendMapper friendMapper) {
        this.friendMapper = friendMapper;
    }

    /**
     * 高级查询，需要查到所有好友的 id，头像，昵称，签名
     *
     * @param accountId
     */
    public List<Map<String, Object>> findDetailsByAccountId(String accountId) {
        return friendMapper.findDetailByAccountId(accountId);
    }

//    public List<FriendWithInfo> findDetailsByAccountId(String accountId) {
//        return friendMapper.findDetailByAccountId(accountId);
//    }

    /**
     * @param myId
     * @param id
     * @param category
     * @param status   范围必须在 {0, 1}
     * @return
     */
    @Transactional
    public Friend updateFriend(String myId, String id, String category, int status) {
        Friend friend = friendMapper.findByFIDs(myId, id);
        if (friend == null) {
            return null;
        }
//        if (friend.getFid_1().equals(myId) && friend.getFid_2().equals(id)) {
//            friend.setCate_name_1(category);
//            friend.setStatus((friend.getStatus() >> 1 << 1) | status);
//        } else if (friend.getFid_1().equals(id) && friend.getFid_2().equals(myId)) {
//            friend.setCate_name_2(category);
//            friend.setStatus((status << 1) | (friend.getStatus() & 1));
//        }

        boolean isSuccess = friendMapper.updateOne(friend);
        if (isSuccess)
            return friend;
        else
            return null;
    }

    public int deleteOneById(String id) {
        return friendMapper.deleteOneById(id);
    }

    public int insertOneByDefault(String fid1, String fid2) {
        return friendMapper.insertOneByDefault(fid1, fid2);
    }

    public int insertOne(Friend friend) {
        return friendMapper.insertOne(friend);
    }

    public boolean updateOne(Friend friend) {
        return friendMapper.updateOne(friend);
    }

}
