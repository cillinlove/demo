package com.example.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "aliyun.oss")
@Component
public class OSSConfig {
    /**
     * bucketName
     */
    private String bucketName;
    /**
     * endpoint
     */
    private String endpoint;
    /**
     * ak
     */
    private String accessKeyId;
    /**
     * sk
     */
    private String accessKeySecret;
}
