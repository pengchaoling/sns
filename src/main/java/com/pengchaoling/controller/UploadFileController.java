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

    private String fileRootPath = "C://upload/";        //文件根目录

    private String FacePath = fileRootPath + "face//";  //头像保存地址

    private String PicPath = fileRootPath + "weibo//";  //微博图片保存地址

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
    @RequestMapping(value="/UploadFile/UploadFace",method={RequestMethod.POST})
    @ResponseBody
    public String faceUpload(@RequestParam("Filedata")MultipartFile Filedata){

        Map<String,String> map = new HashMap<String,String>();
        if(!Filedata.isEmpty()){

            try {

                String tempName = UUID.randomUUID().toString().replaceAll("-", "");
                String filename = Filedata.getOriginalFilename();
                createPreviewImage(Filedata.getInputStream(),tempName+"_50.jpg",50,false,FacePath);
                createPreviewImage(Filedata.getInputStream(),tempName+"_80.jpg",80,false,FacePath);
                createPreviewImage(Filedata.getInputStream(),tempName+"_180.jpg",180,false,FacePath);

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
     * 微博内容插入的图片上传
     */
    @RequestMapping(value="/UploadFile/UploadPic",method={RequestMethod.POST})
    @ResponseBody
    public String UploadPic(@RequestParam("Filedata")MultipartFile Filedata){

        Map<String,String> map = new HashMap<String,String>();
        if(!Filedata.isEmpty()){

            try {
                String tempName = UUID.randomUUID().toString().replaceAll("-", "");
                String filename = Filedata.getOriginalFilename();
                createPreviewImage(Filedata.getInputStream(),tempName+"_800.jpg",800,true,PicPath);
                createPreviewImage(Filedata.getInputStream(),tempName+"_380.jpg",380,true,PicPath);
                createPreviewImage(Filedata.getInputStream(),tempName+"_120.jpg",120,true,PicPath);

                map.put("mini","weibo/"+tempName+"_120.jpg");
                map.put("medium","weibo/"+tempName+"_380.jpg");
                map.put("max","weibo/"+tempName+"_800.jpg");

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
     *
     * @param input
     * @param filename      保存文件名称
     * @param size          缩略图大小（只需指定一个）
     * @param ratio         是否等比例压缩
     * @throws IOException
     */
    public void createPreviewImage(InputStream input, String filename, int size,boolean ratio,String path) throws IOException {
        File fo = new File(path+filename); // 目标图片
        BufferedImage bis = ImageIO.read(input);  //原始图片
        int width = bis.getWidth();     //图片原始大小
        int height = bis.getHeight();
        int newWidth,newHeight;         //压缩后图片宽高
        if(ratio){
            //等比例压缩，高度随宽度改变
            newWidth = size;
            newHeight = newWidth*height/width;
        }else{
            //强制压缩，用用头像处理，宽高都一样
            newWidth=newHeight=size;
        }


        BufferedImage _image = new BufferedImage(newWidth, newHeight,BufferedImage.TYPE_INT_RGB);
        _image.getGraphics().drawImage(bis, 0, 0, newWidth, newHeight, null); //绘制缩小后的图
        FileOutputStream newimageout = new FileOutputStream(fo); //输出到文件流
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(newimageout);
        encoder.encode(_image); //近JPEG编码
        newimageout.close();
    }
}
