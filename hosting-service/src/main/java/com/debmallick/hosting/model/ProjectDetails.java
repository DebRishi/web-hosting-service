package com.debmallick.hosting.model;

import com.debmallick.hosting.constant.Status;
import lombok.Data;

@Data
public class ProjectDetails {
    private String projectId;
    private String projectName;
    private String gitUrl;
    private String projectUrl;
    private String deploymentId;
    private Status status;

    public ProjectDetails(String projectId, String projectName, String gitUrl, String projectUrl, String deploymentId, Status status) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.gitUrl = gitUrl;
        this.projectUrl = projectUrl;
        this.deploymentId = deploymentId;
        this.status = status;
    }
}
