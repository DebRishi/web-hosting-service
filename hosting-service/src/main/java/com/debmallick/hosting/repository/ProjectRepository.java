package com.debmallick.hosting.repository;

import com.debmallick.hosting.model.ProjectDetails;
import com.debmallick.hosting.repository.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, String> {

    @Query("SELECT new com.debmallick.hosting.model.ProjectDetails(p.projectId, p.projectName, p.gitUrl, p.projectUrl, " +
            "d.deploymentId, d.status) FROM Project p JOIN Deployment d ON p.latestDeploymentId = d.deploymentId " +
            "WHERE p.isDeleted = false")
    List<ProjectDetails> findProjectsWithDeployments();
}
