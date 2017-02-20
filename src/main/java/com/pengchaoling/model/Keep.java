package com.pengchaoling.model;

import java.util.Date;

/**
 * Author: Lying
 * Data: 2017-02-20
 * description: 微博收藏表
 */
public class Keep {

        private int id;
        private int uid;
        private Date time;
        private int wid;

        public Keep(){}

        public int getId(){
            return this.id;
        }

        public void setId(int id){
            this.id = id;
        }

        public int getUid(){
            return this.uid;
        }

        public void setUid(int uid){
            this.uid = uid;
        }

        public Date getTime(){
            return this.time;
        }

        public void setTime(Date time){
            this.time = time;
        }

        public int getWid(){
            return this.wid;
        }

        public void setWid(int wid){
            this.wid = wid;
        }





}
