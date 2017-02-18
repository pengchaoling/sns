package com.pengchaoling.dao;

import com.pengchaoling.model.FocusGroup;
import com.pengchaoling.model.LoginTicket;
import com.pengchaoling.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Author: Lying
 * Data: 2017-02-17
 * description: 关注分组
 */
@Mapper
public interface FocusGroupDAO {
    String TABLE_NAME = " focus_group ";
    String INSERT_FIELDS = " name,uid ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{name},#{uid})"})
    int addGroup(FocusGroup focusGroup);


    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, "where uid = #{uid}"})
    List<FocusGroup> selectFocusGroupsByUid(@Param("uid") int uid);

}

