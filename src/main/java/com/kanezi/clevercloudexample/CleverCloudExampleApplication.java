package com.kanezi.clevercloudexample;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@RequiredArgsConstructor
@EnableCaching
@Log4j2
public class CleverCloudExampleApplication {

    private final AvatarService avatarService;

    public static void main(String[] args) {
        SpringApplication.run(CleverCloudExampleApplication.class, args);
    }

}
