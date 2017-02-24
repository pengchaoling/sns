package com.pengchaoling.controller;

import com.pengchaoling.async.EventModel;
import com.pengchaoling.async.EventProducer;
import com.pengchaoling.async.EventType;
import com.pengchaoling.model.*;
import com.pengchaoling.service.CommentService;
import com.pengchaoling.service.UserInfoService;
import com.pengchaoling.service.WeiboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Author: Lying
 * Data: 2017-02-19
 * description: 微博处理控制器，发布，转发，删除 等等操作
 */
@Controller
public class WeiboController {
    private static final Logger logger = LoggerFactory.getLogger(WeiboController.class);
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    WeiboService weiboService;
    @Autowired
    CommentService commentService;
    @Autowired
    EventProducer eventProducer;

    /**
     *  微博发布处理
     */
    @RequestMapping(value = "/weiboSend", method = {RequestMethod.POST})
    public String weiboSend(Model model, @RequestParam("content") String content,
                            @RequestParam("mini") String mini,
                            @RequestParam("medium") String medium,
                            @RequestParam("max") String max,
                           HttpServletResponse response) {

        try {
            Weibo weibo = new Weibo();
            weibo.setUid(hostHolder.getUser().getId());
            weibo.setTime(new Date());
            weibo.setContent(content);
            int add = weiboService.addWeibo(weibo);
            if(add>0){
                //图片插入
                if(!max.isEmpty()){
                    Picture picture = new Picture();
                    picture.setMini(HtmlUtils.htmlEscape(mini));
                    picture.setMedium(HtmlUtils.htmlEscape(medium));
                    picture.setMax(HtmlUtils.htmlEscape(max));
                    picture.setWid(weibo.getId());
                    weiboService.addPicture(picture);
                }

                //提交到异步事件队列去处理
                eventProducer.fireEvent(new EventModel(EventType.ADDWEIBO)
                        .setActorId(hostHolder.getUser().getId()).setEntityId(weibo.getId())
                        .setEntityType(EntityType.ENTITY_WEIBO).setEntityOwnerId(weibo.getUid())
                        .setExt("weibo", String.valueOf(weibo.getContent())));

                //微博数加一
                userInfoService.IncWeibo(hostHolder.getUser().getId());

                model.addAttribute("success_msg", "微博发布成功");
                model.addAttribute("jump_url", "/");
                return "dispatch_jump";

            }else{
                model.addAttribute("error_msg", "微博发布失败");
                model.addAttribute("jump_url", "/");
                return "dispatch_jump";
            }

        } catch (Exception e) {
            logger.error("微博发布异常", e.getMessage());
            model.addAttribute("error_msg", "微博发布失败");
            model.addAttribute("jump_url", "/");
            return "dispatch_jump";
        }

    }

    /**
     * ajax微博评论
     */
    @RequestMapping(value="/addComment",method={RequestMethod.POST})
    @ResponseBody
    public String addComment(@RequestParam("content") String content,
                             @RequestParam("uid") int uid,
                             @RequestParam("wid") int wid,
                             @RequestParam(value="isturn",required = false) int isturn){
        String str="";
        try{

            Comment comment = new Comment();
            comment.setContent(content);
            comment.setUid(uid);
            comment.setTime(new Date());
            comment.setWid(wid);
            int add = commentService.addComment(comment);
            if(add>0){
                //被评论的微博 评论数+1
                weiboService.IncComment(wid);

                //同时转发到我的微博
                if(isturn>0){
                    Weibo weibo = new Weibo();
                    weibo.setUid(hostHolder.getUser().getId());
                    weibo.setTime(new Date());

                    //评论的微博
                    Weibo source = weiboService.selectWeiboById(wid);
                    //评论的微博的转发的原始id
                    int sourceTurnWid = source.getIsturn();

                    //如果评论的微博是转发来的，那么要 @//最开始的用户：最开始的微博内容
                    content =  sourceTurnWid>0 ? content + "//@" + userInfoService.getUserInfoByUid(source.getUid()).getNickname()+ ":" + source.getContent() : content;
                    int turnid = sourceTurnWid >0 ? source.getIsturn() : wid;

                    weibo.setIsturn(turnid);
                    weibo.setContent(content);
                    weiboService.addWeibo(weibo);

                    //被评论的微博转发数+1
                    weiboService.IncTurn(wid);

                    //最原始的微博的转发数 + 1
                    if(sourceTurnWid >0){
                        weiboService.IncTurn(sourceTurnWid);
                    }

                    //用户的微博数+1
                    userInfoService.IncWeibo(hostHolder.getUser().getId());
                }

                //组合样式 ajax返回
                UserInfo userInfo = userInfoService.getUserInfoByUid(uid);

                str += "<dl class='comment_content'>";
                str += "<dt><a href=/profile/"+uid+">";
                str += "<img src='/img/";
                if (!userInfo.getFace50().isEmpty()) {
                    str += userInfo.getFace50()+"'";
                } else {
                    str += "../Images/noface.gif";
                }
                str += " alt='" + userInfo.getNickname() + "' width='30' height='30'/>";
                str += "</a></dt><dd>";
                str += "<a href=/profile/"+uid+ " class='comment_name'>";
                str += userInfo.getNickname() + "</a>：" + HtmlUtils.htmlEscape(content);
                str += "&nbsp;&nbsp(刚刚)";
                str += "<div class='reply'>";
                str += "<a href=''>回复</a>";
                str += "</div></dd></dl>";

                return str;

            }else{
               //评论插入失败
                return "评论异常";
            }

        }catch (Exception e){
            logger.error("评论异常", e.getMessage());
            return "评论异常";

        }
    }

    /**
     * ajax获取微博评论列表
     * 暂时还没做分页
     */
    @RequestMapping(value="/getComment",method={RequestMethod.POST})
    @ResponseBody
    public String getComment(@RequestParam("wid") int wid){
        String str="";

        try{

            List<Comment> comments = commentService.selectCommentsByWid(wid);

            if(!comments.isEmpty()){
                for(Comment comment : comments){
                    //组合样式 ajax返回
                    UserInfo userInfo = userInfoService.getUserInfoByUid(comment.getUid());
                    SimpleDateFormat timeToString=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    str += "<dl class='comment_content'>";
                    str += "<dt><a href=/profile/"+comment.getUid()+">";
                    str += "<img src='/img/";
                    if (!userInfo.getFace50().isEmpty()) {
                        str += userInfo.getFace50()+"'";
                    } else {
                        str += "../Images/noface.gif";
                    }
                    str += " alt='" + userInfo.getNickname() + "' width='30' height='30'/>";
                    str += "</a></dt><dd>";
                    str += "<a href=/profile/"+comment.getUid()+ " class='comment_name'>";
                    str += userInfo.getNickname() + "</a>：" + comment.getContent();
                    str += "&nbsp;&nbsp("+timeToString.format(comment.getTime())+")";
                    str += "<div class='reply'>";
                    str += "<a href=''>回复</a>";
                    str += "</div></dd></dl>";
                }


                return str;

            }else{
                //没有评论
                return "";
            }

        }catch (Exception e){
            logger.error("获取评论列表异常", e.getMessage());
            return "获取评论列表失败";

        }
    }

    /**
     * 微博转发表单处理
     */
    @RequestMapping(value = "/turn", method = {RequestMethod.POST})
    public String weiboTurn(Model model, @RequestParam("content") String content,
                            @RequestParam("id") int id,
                            @RequestParam("tid") int tid,
                            @RequestParam(value="becomment",required = false) String becomment,
                            HttpServletResponse response) {

        try {
            //isturn用于保存原来微博的id
            int isturn = tid>0 ? tid : id;
            Weibo weibo = new Weibo();
            weibo.setUid(hostHolder.getUser().getId());
            weibo.setTime(new Date());
            weibo.setIsturn(isturn);
            weibo.setContent(content);

            int add = weiboService.addWeibo(weibo);
            if(add>0){
                //原微博转发数+1
                weiboService.IncTurn(id);
                if(tid>0){
                    //多重转发的最开始的微博的转发数+1
                    weiboService.IncTurn(tid);
                }
                //用户微博数加一
                userInfoService.IncWeibo(hostHolder.getUser().getId());
                //同时评论
                if(becomment!=null){
                    Comment comment = new Comment();
                    comment.setContent(content);
                    comment.setUid(hostHolder.getUser().getId());
                    comment.setTime(new Date());
                    comment.setWid(id);
                    commentService.addComment(comment);
                    //被评论的微博 评论数+1
                    weiboService.IncComment(id);
                }

                model.addAttribute("success_msg", "微博转发成功");
                model.addAttribute("jump_url", "/");
                return "dispatch_jump";

            }else{
                model.addAttribute("error_msg", "微博转发失败");
                model.addAttribute("jump_url", "/");
                return "dispatch_jump";
            }

        } catch (Exception e) {
            logger.error("微博转发异常", e.getMessage());
            model.addAttribute("error_msg", "微博转发异常");
            model.addAttribute("jump_url", "/");
            return "dispatch_jump";
        }

    }

    /**
     * ajax收藏微博
     */
    @RequestMapping(value="/keep",method={RequestMethod.POST})
    @ResponseBody
    public String keep(@RequestParam("wid") int wid){

        try {
            Keep keep = new Keep();
            keep.setWid(wid);
            keep.setUid(hostHolder.getUser().getId());
            keep.setTime(new Date());

            Keep isKeep = weiboService.selectKeepByUidWid(keep);
            //已经收藏过该微博
            if(isKeep !=null){
                return "-1";
            }

            int add = weiboService.addKeep(keep);
            if(add>0){
                //该微博 收藏数+1
                weiboService.IncKeep(wid);
            }

            return "1";
        }catch (Exception e){
            logger.error("微博收藏异常", e.getMessage());
            return "0";
        }

    }

    /**
     * ajax删除微博
     */
    @RequestMapping(value="/delWeibo",method={RequestMethod.POST})
    @ResponseBody
    public String delWeibo(@RequestParam("wid") int wid){


            Weibo weibo = weiboService.selectWeiboById(wid);
            //当前的微博不是该用户的
            if(weibo.getUid() != hostHolder.getUser().getId()){
                return "0";
            }

            weiboService.delectWeiboByWid(wid);
            //该用户微博数 -1
            userInfoService.DecWeibo(hostHolder.getUser().getId());
            return "1";

    }



}
