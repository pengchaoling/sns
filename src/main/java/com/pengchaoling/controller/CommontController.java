package com.pengchaoling.controller;

import com.pengchaoling.model.FocusGroup;
import com.pengchaoling.model.HostHolder;
import com.pengchaoling.service.FocusGroupService;
import com.pengchaoling.util.SnsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * Author: Lying
 * Data: 2017-02-17
 * description:公用的基础控制器，一些杂七杂八的功能写在这里
 *
 */
@Controller
public class CommontController {
    private static final Logger logger = LoggerFactory.getLogger(CommontController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    FocusGroupService groupService;

    @Autowired
    ResourceLoader resourceLoader;


    private String fileRootPath = "E://upload/";        //文件根目录

    private String FacePath = fileRootPath + "face//";  //头像保存地址


    /**
     * Ajax创建新分组
     */
    @RequestMapping(value = "/Commont/addGroup", method = {RequestMethod.POST})
    @ResponseBody

    public String addGroup(@RequestParam("name") String name,
                            HttpServletResponse response) {

        try {
            FocusGroup focusGroup = new FocusGroup();
            focusGroup.setName(name);
            focusGroup.setUid(hostHolder.getUser().getId());
            //如果插入成功
            if (groupService.addGroup(focusGroup)>0) {
                return SnsUtil.getJSONString(1,"增加分组成功");
            }
        } catch (Exception e) {
            logger.error("增加分组失败" + e.getMessage());
        }
        return SnsUtil.getJSONString(0, "增加分组失败");
    }


}
