package com.example.demo.mq.producer;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.example.demo.mq.config.AliyunMqProperty;
import org.apache.log4j.Logger;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
@EnableConfigurationProperties(AliyunMqProperty.class)
public class MQProducer {
	private static final Logger logger = Logger.getLogger(MQProducer.class);

   @Resource(name="producerBean")
   private  ProducerBean producerBean;
    
    /**
	 * 消息发送
	 */
	 public SendResult sendMQ(String topic,String tags,String body) {
		 if(producerBean != null) {
			 Message msg=new Message(topic, tags,body.getBytes());
				try {
					SendResult send = producerBean.send(msg);
					logger.info("发送MQ消息,topic["+topic+"],tags["+tags+"],body["+body+"]");
		            return send;
		        } catch (Exception e) {
					logger.error("发送MQ消息异常,topic["+topic+"],tags["+tags+"],body["+body+"]",e);
		        }
		 }
		return null;
	}
}
