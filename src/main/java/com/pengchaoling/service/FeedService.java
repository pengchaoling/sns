package com.pengchaoling.service;

import com.pengchaoling.dao.FeedDAO;
import com.pengchaoling.model.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author: Lying
 * Data: 2017-02-23
 * description: feedserver
 */
@Service
public class FeedService {
    @Autowired
    FeedDAO feedDAO;

    public boolean addFeed(Feed feed) {
       //去重  检查是否已经存在了
        Feed check = feedDAO.checkFeed(feed.getUid(),feed.getEvenType(),feed.getEntityType(),feed.getEntityId());
        if(check != null) return false;
        feedDAO.addFeed(feed);
        return feed.getId() > 0;
    }

    public Feed getById(int id) {
        return feedDAO.getFeedById(id);
    }
}
