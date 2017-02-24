package com.pengchaoling.async.handler;

import com.alibaba.fastjson.JSON;
import com.pengchaoling.async.EventHandler;
import com.pengchaoling.async.EventModel;
import com.pengchaoling.async.EventType;
import com.pengchaoling.util.JedisAdapter;
import com.pengchaoling.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Lying
 * Data: 2017-02-21
 * description: 处理队列里面的事件，一手取出并分发事件到各个Handeler
 * ApplicationContext 这个类可以直接获取spring配置文件中，所有有引用到的bean对象。
 */
@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    //事件以及对应的Handler
    private Map<EventType, List<EventHandler>> config = new HashMap<EventType, List<EventHandler>>();
    //用于事件同步，
    private ApplicationContext applicationContext;

    @Autowired
    JedisAdapter jedisAdapter;

    /*
     * Spring 初始化时候会自动调用
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        //引入所有的 实现了 EwentHandler接口的类 相当于从容器里面获取
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (beans != null) {
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {
                //获取到的这些handler分别是什么类型的，以下代码就是把这些handler 组装到 map里面
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();
                //事件注册  弄进这个map里面
                for (EventType type : eventTypes) {
                    if (!config.containsKey(type)) {
                        config.put(type, new ArrayList<EventHandler>());
                    }
                    config.get(type).add(entry.getValue());
                }
            }
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //取队列
                while(true) {
                    String key = RedisKeyUtil.getEventQueueKey();
                    //从redis的队列里面取事件  brpop ：取队列里面的东西，如果没有的话就卡着
                    List<String> events = jedisAdapter.brpop(0, key);

                    //已经取出了事件，然后去找他们的handler去处理
                    for (String message : events) {
                        if (message.equals(key)) {
                            continue;
                        }
                        //反序列化 解析出原来的事件
                        EventModel eventModel = JSON.parseObject(message, EventModel.class);
                        //判断是否包含键名
                        if (!config.containsKey(eventModel.getType())) {
                            logger.error("不能识别的事件");
                            continue;
                        }
                        //调用相关的handler去执行
                        for (EventHandler handler : config.get(eventModel.getType())) {
                            handler.doHandle(eventModel);
                        }
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}