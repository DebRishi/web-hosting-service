package com.debmallick.hosting.model;

import com.debmallick.hosting.constant.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectResponse {
    private String projectId;
    private String projectName;
    private String gitUrl;
    private String deploymentId;
    private String projectUrl;
    private Status status;
}
