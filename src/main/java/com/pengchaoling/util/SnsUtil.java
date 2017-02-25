package com.pengchaoling.util;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * 分页函数
     * @param page    传进来的PageInfo实例
     * @param url     传进来的url
     * @param pathParam 分页传参的字符串 比如 ?p 或 &p
     * @return
     */
    public static String showPage(PageInfo page,String url,String pathParam){
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
            first = "&nbsp;<a href='"+url+pathParam+"="+firstPage+"'>首页</a>&nbsp;";
        }else{
            first = "";
        }

        if(nowPage!=lastPage&&totalPage>1){
            last = "&nbsp;<a href='"+url+pathParam+"="+firstPage+"'>尾页</a>";
        }else {
            last = "";
        }

        //上一页，下一页样式
        String upPage,downPage;

        if(hasPreviousPage){
            upPage = "<a href='"+url+pathParam+"="+prePage+"'>上一页</a>";
        }else{
            upPage = "";
        }

        if(hasNextPage){
            downPage = "<a href='"+url+pathParam+"="+nextPage+"'>下一页</a>";
        }else{
            downPage="";
        }

        //1,2,3,4,5
        String linkPage = "";
        for(int i=1;i<=5;i++){

            int  pageNum = (nowPage >= 3) ? (nowPage - 3 + i) : (nowPage - 1 + i);
            if(pageNum!=nowPage){
                if(pageNum<=totalPage&&pageNum>=firstPage){
                    linkPage += "&nbsp;<a href='"+url+pathParam+"="+pageNum+"'>&nbsp;"+pageNum+"&nbsp;</a>";
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

    /**
     * 表情包替换
     */
    public static String emojiReplace(String content){

        Map<String,String> hashmap = new HashMap<String,String>();
        hashmap.put("hehe","呵呵");
        hashmap.put("xixi","嘻嘻");
        hashmap.put("haha","哈哈");
        hashmap.put("keai","可爱");
        hashmap.put("kelian","可怜");
        hashmap.put("wabisi","挖鼻屎");
        hashmap.put("chijing","吃惊");
        hashmap.put("haixiu","害羞");
        hashmap.put("jiyan","挤眼");
        hashmap.put("bizui","闭嘴");
        hashmap.put("bishi","鄙视");
        hashmap.put("aini","爱你");
        hashmap.put("lei","泪");
        hashmap.put("touxiao","偷笑");
        hashmap.put("qinqin","亲亲");
        hashmap.put("shengbin","生病");
        hashmap.put("taikaixin","太开心");
        hashmap.put("ldln","懒得理你");
        hashmap.put("youhenhen","右哼哼");
        hashmap.put("zuohenhen","左哼哼");
        hashmap.put("xiu","嘘");
        hashmap.put("shuai","衰");
        hashmap.put("weiqu","委屈");
        hashmap.put("tu","吐");
        hashmap.put("dahaqian","打哈欠");
        hashmap.put("baobao","抱抱");
        hashmap.put("nu","怒");
        hashmap.put("yiwen","疑问");
        hashmap.put("canzui","馋嘴");
        hashmap.put("baibai","拜拜");
        hashmap.put("sikao","思考");
        hashmap.put("han","汗");
        hashmap.put("kun","困");
        hashmap.put("shuijiao","睡觉");
        hashmap.put("qian","钱");
        hashmap.put("shiwang","失望");
        hashmap.put("ku","酷");
        hashmap.put("huaxin","花心");
        hashmap.put("heng","哼");
        hashmap.put("guzhang","鼓掌");
        hashmap.put("yun","晕");
        hashmap.put("beishuang","悲伤");
        hashmap.put("zuakuang","抓狂");
        hashmap.put("heixian","黑线");
        hashmap.put("yinxian","阴险");
        hashmap.put("numa","怒骂");
        hashmap.put("xin","心");
        hashmap.put("shuangxin","伤心");

        //正则匹配替换表情包
        Pattern pt = Pattern.compile("\\[.*?\\]");
        Matcher mt = pt.matcher(content);
        while (mt.find()) {
            for(String getKey: hashmap.keySet()){
                String name = mt.group().replace("[","").replace("]","");
                if(hashmap.get(getKey).equals(name)){
                    String str = "<img src='../Images/phiz/"+getKey+".gif' title='"+name+"' />";
                    content = content.replace(mt.group(),str);
                }

            }
        }

        return content;
    }
}
