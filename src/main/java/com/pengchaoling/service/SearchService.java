package com.pengchaoling.service;


import com.pengchaoling.model.UserInfo;
import com.pengchaoling.model.Weibo;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.MapSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Lying
 * Data: 2017-02-23
 * description: 整合solr 搜索服务
 */
@Service
public class SearchService {
    private static final String SOLR_URL = "http://127.0.0.1:8983/solr/sns";
    //服务
    private HttpSolrClient client = new HttpSolrClient.Builder(SOLR_URL).build();
    private static final String WEIBO_CONTENT_FIELD = "weibo";
    private static final String NICKNAME_CONTENT_FIELD = "nickname";

    /**
     *  搜索微博
     */
    public List<Weibo> searchWeibo(String keyword, int offset, int count,
                                      String hlPre, String hlPos) throws Exception {
        List<Weibo> weibosList = new ArrayList<>();
        SolrQuery query = new SolrQuery(keyword);
        Map<String,String> map = new HashMap<String,String>();
        map.put("df",WEIBO_CONTENT_FIELD);
        SolrParams params = new MapSolrParams(map);
        query.add(params);
        query.setRows(count);
        query.setStart(offset);
        query.setHighlight(true);               //高亮
        query.setHighlightSimplePre(hlPre);     //前缀
        query.setHighlightSimplePost(hlPos);    //后缀
        query.set("hl.fl", WEIBO_CONTENT_FIELD);
        //查询结果
        QueryResponse response = client.query(query);
        //解析结果
        for (Map.Entry<String, Map<String, List<String>>> entry : response.getHighlighting().entrySet()) {
            Weibo weibo = new Weibo();
            weibo.setId(Integer.parseInt(entry.getKey()));

            if (entry.getValue().containsKey(WEIBO_CONTENT_FIELD)) {
                List<String> contentList = entry.getValue().get(WEIBO_CONTENT_FIELD);
                if (contentList.size() > 0) {
                    weibo.setContent(contentList.get(0));
                }
            }

            weibosList.add(weibo);
        }
        return weibosList;
    }


    /**
     * 搜索用户
     */
    public List<UserInfo> searchUser(String keyword, int offset, int count,
                                     String hlPre, String hlPos) throws Exception {
        List<UserInfo> userInfosList = new ArrayList<>();
        SolrQuery query = new SolrQuery(keyword);
        Map<String,String> map = new HashMap<String,String>();
        map.put("df",NICKNAME_CONTENT_FIELD);
        SolrParams params = new MapSolrParams(map);
        query.add(params);
        query.setRows(count);
        query.setStart(offset);
        query.setHighlight(true);               //高亮
        query.setHighlightSimplePre(hlPre);     //前缀
        query.setHighlightSimplePost(hlPos);    //后缀
        query.set("hl.fl", NICKNAME_CONTENT_FIELD);
        //查询结果
        QueryResponse response = client.query(query);
        //解析结果
        for (Map.Entry<String, Map<String, List<String>>> entry : response.getHighlighting().entrySet()) {
            UserInfo userInfo = new UserInfo();
            userInfo.setId(Integer.parseInt(entry.getKey()));

            if (entry.getValue().containsKey(NICKNAME_CONTENT_FIELD)) {
                List<String> nicknameList = entry.getValue().get(NICKNAME_CONTENT_FIELD);
                if (nicknameList.size() > 0) {
                    userInfo.setNickname(nicknameList.get(0));
                }
            }

            userInfosList.add(userInfo);
        }
        return userInfosList;
    }

    /**
     *  微博发布的时候索引起来
     */
    public boolean indexWeibo(int wid, String content) throws Exception {
        SolrInputDocument doc =  new SolrInputDocument();
        doc.setField("id", wid);
        doc.setField(WEIBO_CONTENT_FIELD, content);
        UpdateResponse response = client.add(doc, 1000);
        return response != null && response.getStatus() == 0;
    }


    /**
     *  添加用户的时候也把用户索引起来
     */
    public boolean indexUserInfo(int id,int uid, String nickname) throws Exception {
        SolrInputDocument doc =  new SolrInputDocument();
        doc.setField("id", id);
        doc.setField("uid", uid);
        doc.setField(NICKNAME_CONTENT_FIELD, nickname);
        UpdateResponse response = client.add(doc, 1000);
        return response != null && response.getStatus() == 0;
    }
}
