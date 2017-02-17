package com.pengchaoling.dao;

import com.pengchaoling.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Author: Lying
 * Data: 2017-02-15
 * description: UserDao
 */
@Mapper
public interface UserDAO {
    // 注意空格
    String TABLE_NAME = " user ";
    String INSERT_FIELDS = " account,nickname, password, salt, registime ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{account},#{nickname},#{password},#{salt},#{registime})"})
    int addUser(User user);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    User selectById(int id);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where account=#{account}"})
    User selectByAccount(String account);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where nickname=#{nickname}"})
    User selectByNickname(String nickname);

    @Update({"update ", TABLE_NAME, " set password=#{password} where id=#{id}"})
    void updatePassword(User user);

    @Delete({"delete from ", TABLE_NAME, " where id=#{id}"})
    void deleteById(int id);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, "order by id desc limit #{offset},#{limit}"})
    List<User> selectUsers(@Param("offset") int offset,
                                         @Param("limit") int limit);
}
