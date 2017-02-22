package com.pengchaoling.async;

import java.util.List;

/**
 * Author: Lying
 * Data: 2017-02-21
 * description: 事件处理接口，提供规范
 */
public interface EventHandler {
    //处理事件
    void doHandle(EventModel model);
    //注册事件类型，这里存的事件都会去调用前面的doHandle方法
    List<EventType> getSupportEventTypes();
}
