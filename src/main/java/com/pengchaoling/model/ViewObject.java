package com.pengchaoling.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Lying
 * Data: 2017-02-15
 * description: 用hashmap自定义的一种数据模型
 */
public class ViewObject {
    private Map<String, Object> objs = new HashMap<String, Object>();
    public void set(String key, Object value) {
        objs.put(key, value);
    }

    public Object get(String key) {
        return objs.get(key);
    }
}
