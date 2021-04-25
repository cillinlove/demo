package com.example.demo.mq.consumer;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.example.demo.mq.producer.MQProducer;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class OrderConsumerListener implements MessageListener {
    private static final Logger logger = Logger.getLogger(MQProducer.class);
    private static ExecutorService executorService = Executors.newFixedThreadPool(20);

    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    String str = new String(message.getBody(), Charset.defaultCharset().name());
                    logger.info("订单消费... topic:{"+message.getTopic()+"} | tag:{"+message.getTag()+"} | json:{"+str+"}");
                    //业务处理

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        return Action.CommitMessage;
    }

}
