package com.pengchaoling.service;

import com.pengchaoling.dao.FeedDAO;
import com.pengchaoling.model.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: Lying
 * Data: 2017-02-23
 * description: feedserver
 */
@Service
public class FeedService {
    @Autowired
    FeedDAO feedDAO;

    public List<Feed> getUserFeeds(int maxId, List<Integer> uids, int count) {
        return feedDAO.selectUserFeeds(maxId, uids, count);
    }

    public boolean addFeed(Feed feed) {
        feedDAO.addFeed(feed);
        return feed.getId() > 0;
    }

    public Feed getById(int id) {
        return feedDAO.getFeedById(id);
    }
}
