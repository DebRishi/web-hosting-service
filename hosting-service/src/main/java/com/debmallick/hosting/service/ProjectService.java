package com.debmallick.hosting.service;

import com.debmallick.hosting.model.ProjectDetails;
import com.debmallick.hosting.model.ProjectResponse;
import com.debmallick.hosting.repository.ProjectRepository;
import com.debmallick.hosting.repository.entity.Project;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EcsService ecsService;

    public ProjectResponse createProject(String projectName, String gitUrl) {

        String projectId = UUID.randomUUID().toString();

        Project project = Project.builder()
                .projectId(projectId)
                .projectName(projectName)
                .gitUrl(gitUrl)
                .projectUrl(ecsService.getProjectUrlFromId(projectId))
                .createdAt(new Date())
                .updatedAt(new Date())
                .isDeleted(false)
                .build();

        projectRepository.save(project);

        return ProjectResponse.builder()
                .projectId(project.getProjectId())
                .projectName(projectName)
                .gitUrl(gitUrl)
                .build();
    }

    public List<ProjectDetails> getAllProjects() {
        return projectRepository.findProjectsWithDeployments();
    }

    public ProjectResponse deleteProject(String projectId) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (ObjectUtils.isEmpty(project)) {
            return ProjectResponse.builder().build();
        } else {
            project.setIsDeleted(true);
            projectRepository.save(project);
            return ProjectResponse.builder()
                    .projectId(projectId)
                    .projectName(project.getProjectName())
                    .build();
        }
    }
}
