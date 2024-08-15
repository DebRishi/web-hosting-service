package com.debmallick.hosting.model;

import com.debmallick.hosting.constant.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeploymentResponse {
    private String projectId;
    private String deploymentId;
    private Status status;
    private String projectUrl;
}
