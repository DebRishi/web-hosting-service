package com.debmallick.hosting.service;

import com.debmallick.hosting.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DynamoDbService {

    @Autowired
    DynamoDbClient client;

    public void saveLog(Message message) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("projectId", AttributeValue.builder().s(message.getProjectId()).build());
        item.put("deploymentId", AttributeValue.builder().s(message.getDeploymentId()).build());
        item.put("timestamp", AttributeValue.builder().n(Long.toString(message.getTimestamp().getTime())).build());
        item.put("log", AttributeValue.builder().s(message.getLog()).build());
        PutItemRequest request = PutItemRequest.builder()
                .tableName("container-logs")
                .item(item)
                .build();
        client.putItem(request);
    }

    public List<Message> getLogs(String deploymentId) {

        Map<String, AttributeValue> expressionValues = new HashMap<>();
        expressionValues.put(":deploymentId", AttributeValue.builder().s(deploymentId).build());

        QueryRequest request = QueryRequest.builder()
                .tableName("container-logs")
                .keyConditionExpression("deploymentId = :deploymentId")
                .expressionAttributeValues(expressionValues)
                .scanIndexForward(false)
                .build();

        return triggerQuery(request);
    }

    private List<Message> triggerQuery(QueryRequest request) {
        QueryResponse response = client.query(request);

        return response.items().stream()
                .map(this::convertItemToMessage)
                .collect(Collectors.toList());
    }

    private Message convertItemToMessage(Map<String, AttributeValue> item) {
        return Message.builder()
                .projectId(item.get("projectId").s())
                .deploymentId(item.get("deploymentId").s())
                .log(item.get("log").s())
                .timestamp(new Date(Long.parseLong(item.get("timestamp").n())))
                .build();
    }
}
