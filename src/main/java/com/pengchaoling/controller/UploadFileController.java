package com.pengchaoling.controller;

import com.pengchaoling.util.SnsUtil;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Author: Lying
 * Data: 2017-02-18
 * description:  图片上传，图片处理，文件处理专用控制器
 */
@Controller
public class UploadFileController {
    private static final Logger logger = LoggerFactory.getLogger(UploadFileController.class);

    @Autowired
    ResourceLoader resourceLoader;

    private String fileRootPath = "E://upload/";        //文件根目录

    private String FacePath = fileRootPath + "face//";  //头像保存地址

    /**
     * 前台自动载入上传的文件
     * @param filename
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/img/{dir}/{filename:.+}")
    @ResponseBody
    public ResponseEntity<?> getFile(@PathVariable String dir, @PathVariable String filename) {

        try {
            return ResponseEntity.ok(resourceLoader.getResource("file:" + Paths.get(fileRootPath+dir+"/", filename).toString()));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     *  ajax头像上传，生成三种尺寸的头像
     */
    @RequestMapping(value="/UploadFile/UploodFace",method={RequestMethod.POST})
    @ResponseBody
    public String faceUpload(@RequestParam("Filedata")MultipartFile Filedata){

        Map<String,String> map = new HashMap<String,String>();
        if(!Filedata.isEmpty()){

            try {

                String tempName = UUID.randomUUID().toString().replaceAll("-", "");
                String filename = Filedata.getOriginalFilename();
                createPreviewImage(Filedata.getInputStream(),tempName+"_50.jpg",50);
                createPreviewImage(Filedata.getInputStream(),tempName+"_80.jpg",80);
                createPreviewImage(Filedata.getInputStream(),tempName+"_180.jpg",180);

                map.put("mini","face/"+tempName+"_50.jpg");
                map.put("medium","face/"+tempName+"_80.jpg");
                map.put("max","face/"+tempName+"_180.jpg");

            } catch (FileNotFoundException e) {
                logger.error(e.getMessage());
                SnsUtil.getJSONString(0, "找不到文件");
            } catch (IOException e) {
                logger.error(e.getMessage());
                return SnsUtil.getJSONString(0,"IO错误");
            }

            return SnsUtil.getJSONString(1,map);

        }else{
            logger.error("文件为空");
            return SnsUtil.getJSONString(0,"文件为空");
        }

    }

    /**
     * 头像上传 生成不同尺寸的缩略图
     */
    public void createPreviewImage(InputStream input, String filename, int size) throws IOException {
        File fo = new File(FacePath+filename); // 目标图片
        BufferedImage bis = ImageIO.read(input);  //原始图片
        int nw =size;                        //图片缩放后的大小
        int nh =size;
        /*
        //缩略图的标准 为  120*120（等比例缩放）
        if ( (double)w / h < 1 ){
            nh = 120;
            nw = nh*w/h;
        }else {
            nw = 120;
            nh = nw*h/w;
        }*/

        BufferedImage _image = new BufferedImage(nw, nh,BufferedImage.TYPE_INT_RGB);
        _image.getGraphics().drawImage(bis, 0, 0, nw, nh, null); //绘制缩小后的图
        FileOutputStream newimageout = new FileOutputStream(fo); //输出到文件流
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(newimageout);
        encoder.encode(_image); //近JPEG编码
        newimageout.close();
    }
}
