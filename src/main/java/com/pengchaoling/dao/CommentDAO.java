package com.pengchaoling.dao;

import com.pengchaoling.model.Comment;
import com.pengchaoling.model.User;
import com.pengchaoling.model.Weibo;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Author: Lying
 * Data: 2017-02-19
 * description: WeiboDAO
 */
@Mapper
public interface CommentDAO {
    // 注意空格
    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " content,time,uid,wid ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{content},#{time},#{uid},#{wid})"})
    int addComment(Comment comment);


    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, "where wid = #{wid} order by id desc"})
    List<Comment> selectCommentsByWid(int wid);


}
