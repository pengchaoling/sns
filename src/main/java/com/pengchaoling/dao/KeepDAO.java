package com.pengchaoling.dao;

import com.pengchaoling.model.Keep;
import com.pengchaoling.model.Picture;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Author: Lying
 * Data: 2017-02-19
 * description:
 */
@Mapper
public interface KeepDAO {
    // 注意空格
    String TABLE_NAME = " keep ";
    String INSERT_FIELDS = " uid, time, wid ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{uid},#{time},#{wid})"})
    int addKeep(Keep keep);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where uid=#{uid} order by id desc"})
    List<Keep> selectKeepsByuid(@Param("uid")int uid);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where uid =#{uid} and wid=#{wid}"})
    Keep selectKeepByUidWid(Keep keep);

}

