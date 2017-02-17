package com.pengchaoling.dao;

import com.pengchaoling.model.Group;
import com.pengchaoling.model.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * Author: Lying
 * Data: 2017-02-17
 * description: 关注分组
 */
@Mapper
public interface GroupDAO {
    String TABLE_NAME = " group ";
    String INSERT_FIELDS = " name,uid ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{name},#{uid}"})
    int addGroup(LoginTicket ticket);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where uid=#{uid}"})
    Group selectGroupByUid(int uid);

    @Update({"update ", TABLE_NAME, " set name=#{name} where uid=#{uid}"})
    void updateStatus(@Param("name") String name, @Param("uid") int uid);
}

