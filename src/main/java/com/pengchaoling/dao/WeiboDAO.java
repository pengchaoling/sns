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
    Weibo selectWeiBoById(int id);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, "order by id desc limit #{offset},#{limit}"})
    List<Weibo> selectWeibos(@Param("offset") int offset,
                           @Param("limit") int limit);


}
