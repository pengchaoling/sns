package com.pengchaoling.dao;

import com.pengchaoling.model.User;
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
    String INSERT_FIELDS = " nickname, truename, sex, location, constellation, intro, face50,face80, face180, style, follow, fans, weibo, uid  ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{nickname},#{truename},#{sex},#{location},#{constellation},#{intro},#{face50},#{face80},#{face180},#{style},#{follow},#{fans},#{weibo},#{uid})"})
    int addUserInfo(UserInfo userinfo);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where uid=#{uid}"})
    UserInfo selectUserInfoByUid(int uid);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where nickname=#{nickname}"})
    UserInfo selectUserInfoByNickname(String nickname);

    @Update({"update ", TABLE_NAME, " set face50=#{face50},face80=#{face80},face180=#{face180} where uid=#{uid}"})
    void updateFace(UserInfo userinfo);

}
