package com.debmallick.hosting.repository.entity;

import com.debmallick.hosting.constant.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "DEPLOYMENTS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Deployment {

    @Id
    private String deploymentId;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column
    private String projectId;
}
