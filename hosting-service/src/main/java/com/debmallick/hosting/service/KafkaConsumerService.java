package com.debmallick.hosting.service;

import com.debmallick.hosting.model.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class KafkaConsumerService {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private DynamoDbService dynamoDbService;

    @Autowired
    private DeploymentService deploymentService;

    @KafkaListener(topics = "container-logs", groupId = "kafka-collector-group")
    public void consume(String messageString) {
        try {
            Message message = mapper.readValue(messageString, Message.class);
            message.setTimestamp(new Date());
            log.info("MESSAGE: {}", message);
            dynamoDbService.saveLog(message);
            deploymentService.updateDeploymentStatus(message);
        } catch (Exception x) {
            log.error("Exception occurred while parsing and saving message: {}", messageString, x);
        }
    }
}
