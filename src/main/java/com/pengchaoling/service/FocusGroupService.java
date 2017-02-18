package com.pengchaoling.service;

import com.pengchaoling.dao.FocusGroupDAO;
import com.pengchaoling.model.FocusGroup;
import com.pengchaoling.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Author: Lying
 * Data: 2017-02-18
 * description:
 */
@Service
public class FocusGroupService {
    @Autowired
    FocusGroupDAO focusGroupDAO;

    public int addGroup(FocusGroup focusGroup){
        //很有必要过滤一下
        focusGroup.setName(HtmlUtils.htmlEscape(focusGroup.getName()));
        return focusGroupDAO.addGroup(focusGroup);
    }

    /**
     * 根据uid获取该用户所有的关注分组
     */
    public List<FocusGroup> selectFocusGroupsByUid(int uid){
        return focusGroupDAO.selectFocusGroupsByUid(uid);
    }

}
