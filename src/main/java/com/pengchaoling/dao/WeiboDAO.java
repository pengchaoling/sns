package com.pengchaoling.dao;

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
public interface WeiboDAO {
    // 注意空格
    String TABLE_NAME = " weibo ";
    String INSERT_FIELDS = " content, isturn, time, turn, keep, comment,uid ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{content},#{isturn},#{time},#{turn},#{keep},#{comment},#{uid})"})
    int addWeibo(Weibo weibo);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    Weibo selectWeiBoById(@Param("id")int id);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, "order by id desc "})
    List<Weibo> selectWeibos();

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, "where uid =#{uid} order by id desc "})
    List<Weibo> selectWeibosByUid(@Param("uid") int uid);

    @Update({"update ", TABLE_NAME, " set turn=turn+1 where id=#{wid}"})
    void IncTurn(@Param("wid") int wid);

    @Update({"update ", TABLE_NAME, " set keep=keep+1 where id=#{wid}"})
    void IncKeep(@Param("wid") int wid);

    @Update({"update ", TABLE_NAME, " set comment=comment+1 where id=#{wid}"})
    void IncComment(@Param("wid") int wid);

    @Delete({"delete from ", TABLE_NAME, " where id=#{id}"})
    void deleteWeiboById(@Param("id") int id);





}
