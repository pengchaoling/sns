package com.pengchaoling.dao;

import com.pengchaoling.model.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
    List<Comment> selectCommentsByWid(@Param("wid") int wid);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, "where id = #{id}"})
    Comment getCommentById(@Param("id") int id);


}
