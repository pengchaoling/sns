package com.pengchaoling.service;

import com.pengchaoling.dao.AtmeDAO;
import com.pengchaoling.model.Atme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: Lying
 * Data: 2017-02-24
 * description: Atme 艾特用户
 */
@Service
public class AtmeService {
    @Autowired
    AtmeDAO atmeDAO;

    public List<Atme> getAtmeByUid(int uid){
        return atmeDAO.getAtmeByUid(uid);
    }

    public int addAtme(Atme atme) {
        return atmeDAO.addAtme(atme);
    }

}
