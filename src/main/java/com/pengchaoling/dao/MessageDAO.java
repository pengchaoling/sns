package com.pengchaoling.dao;

import com.pengchaoling.model.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Author: Lying
 * Data: 2017-02-20
 * description: 站内信
 */

@Mapper
public interface MessageDAO {
    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " fromId, toId, content,createdDate, hasRead, conversationId ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{fromId},#{toId},#{content},#{createdDate},#{hasRead},#{conversationId} )"})
    int addMessage(Message message);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where conversationId=#{conversationId} order by id desc limit #{offset}, #{limit}"})
    List<Message> getConversationDetail(@Param("conversationId") String conversationId,
                                        @Param("offset") int offset, @Param("limit") int limit);

    @Select({"select count(id) from ", TABLE_NAME, " where hasRead=0 and toId=#{userId} and conversationId=#{conversationId}"})
    int getConvesationUnreadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);

    @Select({"select count(id) from ", TABLE_NAME, " where conversationId=#{conversationId}"})
    int getConvesationTotalCount(@Param("conversationId") String conversationId);


    @Select({"select ", INSERT_FIELDS, " ,count(id) as id from ( select * from ", TABLE_NAME, " where fromId=#{userId} or toId=#{userId} order by id desc) tt group by conversationId  order by createdDate desc limit #{offset}, #{limit}"})
    List<Message> getConversationList(@Param("userId") int userId,
                                      @Param("offset") int offset, @Param("limit") int limit);

    @Update({"update ", TABLE_NAME, " set hasRead=1 where toId=#{toId} and conversationId=#{conversationId}"})
    void updateHasRead(@Param("toId") int toId,@Param("conversationId") String conversationId);
}
