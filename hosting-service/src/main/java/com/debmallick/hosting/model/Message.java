package com.debmallick.hosting.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {
    private String projectId;
    private String deploymentId;
    private String log;
    private String status;
    private Date timestamp;
}
