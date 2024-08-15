package com.debmallick.hosting.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectRequest {
    private String projectName;
    private String gitUrl;
}
