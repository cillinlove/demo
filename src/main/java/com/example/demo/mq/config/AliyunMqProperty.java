package com.example.demo.mq.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@ConfigurationProperties(prefix = "aliyun.mq")
@Component
public class AliyunMqProperty {
    /**
     * PID
     */
    private String producerId;
    /**
     * ak
     */
    private String accessKey;
    /**
     * sk
     */
    private String secretKey;
    /**
     * 服务地址
     */
    private String onsAddr;
}
