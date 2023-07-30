package com.kanezi.clevercloudexample;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j2;
import org.joda.time.Period;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import com.amazonaws.HttpMethod;

import static com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;


@Configuration
@Value
@NonFinal
@Log4j2
public class S3Config {

    S3Properties s3Properties;

    private EndpointConfiguration endpointConfiguration() {
        return new EndpointConfiguration(s3Properties.host, s3Properties.region);
    }


    private AWSStaticCredentialsProvider awsStaticCredentialsProvider() {
        return new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(s3Properties.accessKey, s3Properties.secretKey)
        );
    }


    @Bean
    AmazonS3 s3Client() {
        return AmazonS3ClientBuilder.standard()
                                    .withCredentials(awsStaticCredentialsProvider())
//                             .withClientConfiguration(opts)
                                    .withEndpointConfiguration(endpointConfiguration())
                                    .withPathStyleAccessEnabled(Boolean.TRUE)
                                    .build();
    }

}
