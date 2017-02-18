package com.pengchaoling.controller;

import com.pengchaoling.model.FocusGroup;
import com.pengchaoling.model.HostHolder;
import com.pengchaoling.service.FocusGroupService;
import com.pengchaoling.util.SnsUtil;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

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
