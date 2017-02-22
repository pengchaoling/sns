package com.pengchaoling.util;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.Map;

/**
 * Author: Lying
 * Data: 2017-02-15
 * description: 依赖函数库
 */
public class SnsUtil {
    private static final Logger logger = LoggerFactory.getLogger(SnsUtil.class);
    public static int ANONYMOUS_USERID = 3;
    //系统用户id 用于发系统站内信（私信）
    public static int SYSTEM_USERID = 3;
    //生成分页样式 分页参数用p
    public static String showPage(PageInfo page,String url){
        int nowPage = page.getPageNum();        //现在的页面
        int prePage = page.getPrePage();        //上一页
        int nextPage = page.getNextPage();      //下一页
        int lastPage = page.getLastPage();      //最后一页
        int firstPage = page.getFirstPage();    //第一页
        int totalPage = page.getPages();     //总页数

        boolean hasPreviousPage = page.isHasPreviousPage();
        boolean hasNextPage = page.isHasNextPage();

        String info = "共" + page.getTotal()+"条记录，"+nowPage + "/" + totalPage + "页";

        //第一页，最后一页样式
        String first,last;
        if(nowPage!=firstPage&&totalPage>1){
            first = "&nbsp;<a href='"+url+"?p="+firstPage+"'>首页</a>&nbsp;";
        }else{
            first = "";
        }

        if(nowPage!=lastPage&&totalPage>1){
            last = "&nbsp;<a href='"+url+"?p="+firstPage+"'>尾页</a>";
        }else {
            last = "";
        }

        //上一页，下一页样式
        String upPage,downPage;

        if(hasPreviousPage){
            upPage = "<a href='"+url+"?p="+prePage+"'>上一页</a>";
        }else{
            upPage = "";
        }

        if(hasNextPage){
            downPage = "<a href='"+url+"?p="+nextPage+"'>下一页</a>";
        }else{
            downPage="";
        }

        //1,2,3,4,5
        String linkPage = "";
        for(int i=1;i<=5;i++){

            int  pageNum = (nowPage >= 3) ? (nowPage - 3 + i) : (nowPage - 1 + i);
            if(pageNum!=nowPage){
                if(pageNum<=totalPage&&pageNum>=firstPage){
                    linkPage += "&nbsp;<a href='"+url+"?p="+pageNum+"'>&nbsp;"+pageNum+"&nbsp;</a>";
                }else {
                    //没有那么多页
                    break;
                }
            }else{
                linkPage +=  "&nbsp;<span class='current'>"+pageNum+"</span>";
            }
        }

        String result = info + first + upPage+ linkPage + downPage + last;


        return result;
    }

    public static String getJSONString(int status) {
        JSONObject json = new JSONObject();
        json.put("status", status);
        return json.toJSONString();
    }

    public static String getJSONString(int status, String msg) {
        JSONObject json = new JSONObject();
        json.put("status", status);
        json.put("msg", msg);
        return json.toJSONString();
    }

    public static String getJSONString(int status, Map<String, String> map) {
        JSONObject json = new JSONObject();
        json.put("status", status);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            json.put(entry.getKey(), entry.getValue());
        }
        return json.toJSONString();
    }

    public static String MD5(String key) {
        char hexDigits[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            logger.error("生成MD5失败", e);
            return null;
        }
    }
}
