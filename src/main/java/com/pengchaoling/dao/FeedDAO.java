package com.pengchaoling.dao;

import com.pengchaoling.model.Feed;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Author: Lying
 * Data: 2017-02-23
 * description: 新鲜事流
 */
@Mapper
public interface FeedDAO {
    String TABLE_NAME = " feed ";
    String INSERT_FIELDS = " uid,created_date,event_type,entity_type,entity_id,data";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{uid},#{createdDate},#{evenType},#{entityType},#{entityId},#{data})"})
    int addFeed(Feed feed);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    Feed getFeedById(int id);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where uid=#{uid} and event_type=#{eventType} and entity_type=#{entityType} and entity_id=#{entityId} limit 1"})
    Feed checkFeed(@Param("uid") int uidd,
                               @Param("eventType") int eventType,
                               @Param("entityType") int entityType,
                               @Param("entityId") int entityId);
}
