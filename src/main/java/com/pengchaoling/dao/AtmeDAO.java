package com.pengchaoling.dao;

import com.pengchaoling.model.Atme;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Author: Lying
 * Data: 2017-02-23
 * description: 艾特用户
 */
@Mapper
public interface AtmeDAO {
    String TABLE_NAME = " atme ";
    String INSERT_FIELDS = " entity_type,entity_id,uid";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{entityType},#{entityId},#{uid})"})
    int addAtme(Atme atme);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where uid=#{uid} order by id desc"})
    List<Atme> getAtmeByUid(int uid);


}
