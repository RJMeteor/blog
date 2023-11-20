package com.personal.blog.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

@Configuration
@PropertySource({"classpath:config/oss.config.properties"})
public class OssConfig {

    public volatile static OSS ossClient;

    public volatile static OSSClientBuilder ossClientBuilder;

    public static String endpoint;
    public static String bucket;

    public static String accessKeyId;

    public static String accessKeySecret;

    @Value("${aliyun.bucketName}")
    private  String bucketName;

    @Value("${aliyun.bucket}")
    public void setBucket(String bucket) {
        OssConfig.bucket = bucket;
    }
    @Value("${aliyun.endpoint}")
    public void setEndpoint(String endpoint) {
        OssConfig.endpoint = endpoint;
    }

    @Value("${aliyun.accessKeyId}")
    public void setAccessKeyId(String accessKeyId) {
        OssConfig.accessKeyId = accessKeyId;
    }

    @Value("${aliyun.accessKeySecret}")
    public void setAccessKeySecret(String accessKeySecret) {
        OssConfig.accessKeySecret = accessKeySecret;
    }

    public String getBucketName() {
        return bucketName;
    }

    @Bean
    @Scope("prototype")
    public static OSS initOSSClient() {
        if (ossClient == null) {
            synchronized (OssConfig.class) {
                if (ossClient == null) {
                    ossClient = initOSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
                }
            }
        }
        return ossClient;
    }

    private static OSSClientBuilder initOSSClientBuilder() {
        if (ossClientBuilder == null) {
            synchronized (OssConfig.class) {
                if (ossClientBuilder == null) {
                    ossClientBuilder = new OSSClientBuilder();
                }
            }
        }
        return ossClientBuilder;
    }
}
