package com.debmallick.hosting.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "PROJECTS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    private String projectId;

    @Column
    private String projectName;

    @Column
    private String gitUrl;

    @Column
    private String projectUrl;

    @Column
    private String latestDeploymentId;

    @Column
    private Boolean isDeleted;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
}
