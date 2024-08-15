package com.debmallick.hosting.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.ecs.EcsClient;

@Configuration
public class AwsConfig {

    @Value("${aws.config.access.key}")
    private String accessKey;

    @Value("${aws.config.secret.key}")
    private String secretKey;

    @Value("${aws.config.region}")
    private String region;

    @Bean
    public AwsBasicCredentials awsBasicCredentials() {
        return AwsBasicCredentials.create(accessKey, secretKey);
    }

    @Bean
    public StaticCredentialsProvider staticCredentialsProvider(AwsBasicCredentials awsBasicCredentials) {
        return StaticCredentialsProvider.create(awsBasicCredentials);
    }

    @Bean
    public EcsClient ecsClient(StaticCredentialsProvider staticCredentialsProvider) {
        return EcsClient.builder()
                .region(Region.of(region))
                .credentialsProvider(staticCredentialsProvider)
                .build();
    }

    @Bean
    public DynamoDbClient dynamoDbClient(StaticCredentialsProvider staticCredentialsProvider) {
        return DynamoDbClient.builder()
                .region(Region.of(region))
                .credentialsProvider(staticCredentialsProvider)
                .build();
    }
}
