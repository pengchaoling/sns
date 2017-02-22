package com.pengchaoling.dao;

import com.pengchaoling.model.UserInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Author: Lying
 * Data: 2017-02-17
 * description: 用户额外信息
 */
@Mapper
public interface UserInfoDAO {
    // 注意空格
    String TABLE_NAME = " userinfo ";
    String INSERT_FIELDS = " nickname, truename, sex, location, constellation, intro, face50,face80, face180, style, follower, followee, weibo, uid  ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{nickname},#{truename},#{sex},#{location},#{constellation},#{intro},#{face50},#{face80},#{face180},#{style},#{follower},#{followee},#{weibo},#{uid})"})
    int addUserInfo(UserInfo userinfo);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where uid=#{uid}"})
    UserInfo selectUserInfoByUid(@Param("uid") int uid);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where nickname=#{nickname}"})
    UserInfo selectUserInfoByNickname(String nickname);

    @Update({"update ", TABLE_NAME, " set face50=#{face50},face80=#{face80},face180=#{face180} where uid=#{uid}"})
    void updateFace(UserInfo userinfo);

    @Update({"update ", TABLE_NAME, " set nickname=#{nickname},truename=#{truename},sex=#{sex},location=#{location},constellation=#{constellation},intro=#{intro} where uid=#{uid}"})
    void updateBasic(UserInfo userinfo);

    //字段加一
    @Update({"update ", TABLE_NAME, " set weibo=weibo+1 where uid=#{uid}"})
    void IncWeibo(@Param("uid") int uid);

    @Update({"update ", TABLE_NAME, " set weibo=weibo-1 where uid=#{uid}"})
    void DecWeibo(@Param("uid") int uid);






}
