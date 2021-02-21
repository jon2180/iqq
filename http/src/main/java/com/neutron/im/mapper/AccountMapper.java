package com.neutron.im.mapper;

import com.neutron.im.entity.Account;
import com.neutron.im.entity.Friend;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AccountMapper {
    @Select("select * from accounts")
    List<Account> findAll();

    @Select("select * from accounts where email=#{email} limit 1")
    Account findOneByEmail(String email);


    @Select("select * from accounts where id=#{id} limit 1")
    Account findOneById(int id);

    @Select("'select * from accounts where uid=? limit 1")
    Account findOneByUId(String uid);

    @Insert("insert into accounts(uid,email,nickname,signature,tel,avatar,password,salt,birthday)\n" +
            "       values (#{uid},#{email},#{nickname},#{signature},#{tel},#{avatar},#{password},#{salt},#{birthday})")
    @SelectKey(statement = "call identity()", keyProperty = "id", before = false, resultType = int.class)
    int insert(
            String uid,
            String email,
            String nickname,
            String signature,
            String tel,
            String avatar,
            String password,
            String salt,
            String birthday
    );

//    @Insert("insert into accounts(uid,email,nickname,signature,tel,avatar,password,salt,birthday)\n" +
//        "       values (#{uid},#{email},#{nickname},#{signature},#{tel},#{avatar},#{password},#{salt},#{birthday})")
//    @SelectKey(statement = "call identity()", keyProperty = "id", before = false, resultType = int.class)
//    int insert(Account account);

    @Update("""
            update accounts
            set
            uid=#{uid}, email=#{email}, nickname=#{nickname}, signature=#{signature}, tel=#{tel}, avatar=#{avatar},
            password=#{password}, salt=#{salt}, birthday=#{birthday},status=#{status}
            where id=#{id}
            """)
    int update(
            String uid,
            String email,
            String nickname,
            String signature,
            String tel,
            String avatar,
            String password,
            String salt,
            String birthday,
            int status,
            int id
    );

//    @Delete("delete from accounts where id = #{id}")
//    int delete(int id);
}
