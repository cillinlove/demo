package com.example.demo.mq.config;

import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;
import com.example.demo.mq.consumer.ConsumerListener;
import com.example.demo.mq.consumer.OrderConsumerListener;
import com.example.demo.mq.producer.MQProducer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
@Configuration
public class AliyunMqConfig {
    private static final Logger logger = Logger.getLogger(MQProducer.class);
    private static final String CID_CONSUMER_ID = "CID_ICIW_STATUS_ORDER";

    @Autowired
    private AliyunMqProperty aliyunMqProperty;

    @Autowired
    private ConsumerListener consumerListener;
    @Autowired
    private OrderConsumerListener orderConsumerListener;


    /**
     * 生产者
     * @return
     */
    @Bean(name="producerBean" ,initMethod = "start", destroyMethod = "shutdown")
    public ProducerBean producerBean() {
        logger.info("--------------初始化生产者MQ--------------");
        ProducerBean producerBean = new ProducerBean();
        Properties properties = new Properties();
        properties.putAll(new HashMap<String, Object>(){
            {
                put(PropertyKeyConst.ProducerId, aliyunMqProperty.getProducerId());
                put(PropertyKeyConst.AccessKey, aliyunMqProperty.getAccessKey());
                put(PropertyKeyConst.SecretKey, aliyunMqProperty.getSecretKey());
                put(PropertyKeyConst.ONSAddr, aliyunMqProperty.getOnsAddr());
            }
        });
        producerBean.setProperties(properties);
        return producerBean;
    }

    /**
     * 消费者
     */
    @Bean(name="statusOrderConsumer" ,initMethod = "start", destroyMethod = "shutdown")
    public ConsumerBean statusOrderConsumer() {
        logger.info("------------------------初始化互联互通监听MQ------------------------");
        ConsumerBean bean = new ConsumerBean();
        Properties properties = new Properties();
        properties.putAll(new HashMap<String, Object>(){
            {
                put(PropertyKeyConst.ConsumerId, CID_CONSUMER_ID);
                put(PropertyKeyConst.ProducerId, aliyunMqProperty.getProducerId());
                put(PropertyKeyConst.AccessKey, aliyunMqProperty.getAccessKey());
                put(PropertyKeyConst.SecretKey, aliyunMqProperty.getSecretKey());
                put(PropertyKeyConst.ONSAddr, aliyunMqProperty.getOnsAddr());
            }
        });
        bean.setProperties(properties);

        Map<Subscription, MessageListener> statusOrderMap = new HashMap<>();

        Subscription sub = new Subscription();
        sub.setTopic(MQTopicConstant.TOPIC_MONITOR_HLHT);
        //暂未针对tag过滤,可以根据实际情况选择过滤
        sub.setExpression("*");

        Subscription sub1 = new Subscription();
        sub1.setTopic(MQTopicConstant.TOPIC_ORDER_HLHT);
        sub1.setExpression("*");

        statusOrderMap.put(sub, consumerListener);
        statusOrderMap.put(sub1,orderConsumerListener);

        bean.setSubscriptionTable(statusOrderMap);
        return bean;
    }


}
