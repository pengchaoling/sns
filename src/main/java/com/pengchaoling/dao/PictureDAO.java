package com.pengchaoling.dao;

import com.pengchaoling.model.Picture;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Author: Lying
 * Data: 2017-02-19
 * description:
 */
@Mapper
public interface PictureDAO {
    // 注意空格
    String TABLE_NAME = " picture ";
    String INSERT_FIELDS = " mini, medium, max, wid ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{mini},#{medium},#{max},#{wid})"})
    int addWPicture(Picture picture);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where wid=#{wid}"})
    Picture selectPictureByWid(int wid);


}

