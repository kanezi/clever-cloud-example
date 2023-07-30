package com.kanezi.clevercloudexample;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.datasource")
@Data
@Log4j2
public class DbProperties {
    String username;
    String password;
    String url;

    @PostConstruct
    void displayDbProperties() {
        log.info("db username: {}", username);
        log.info("db url: {}", url);
    }
}
