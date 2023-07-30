package com.kanezi.clevercloudexample;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "s3")
@Data
@Log4j2
public class S3Properties {
    String accessKey;
    String secretKey;
    String host;
    String region;
    String bucket;

    @PostConstruct
    void printProperties() {
        log.info("s3Properties: {}", this);
    }

}
