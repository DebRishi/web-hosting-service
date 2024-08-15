package com.debmallick.hosting.service;

import com.debmallick.hosting.constant.Status;
import com.debmallick.hosting.model.DeploymentResponse;
import com.debmallick.hosting.model.Message;
import com.debmallick.hosting.repository.DeploymentRepository;
import com.debmallick.hosting.repository.ProjectRepository;
import com.debmallick.hosting.repository.entity.Deployment;
import com.debmallick.hosting.repository.entity.Project;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class DeploymentService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private DeploymentRepository deploymentRepository;

    @Autowired
    private EcsService ecsService;

    public DeploymentResponse createDeployment(String projectId) {

        Project project = projectRepository.findById(projectId).orElse(null);

        if (ObjectUtils.isEmpty(project)) {
            log.error("No project found with id: {}", projectId);
            return null;
        } else {

            Deployment deployment = Deployment.builder()
                    .deploymentId(UUID.randomUUID().toString())
                    .status(Status.PENDING)
                    .createdAt(new Date())
                    .projectId(projectId)
                    .build();

            deploymentRepository.save(deployment);

            project.setLatestDeploymentId(deployment.getDeploymentId());
            project.setUpdatedAt(new Date());
            projectRepository.save(project);

            ecsService.runTask(project.getGitUrl(), project.getProjectId(), deployment.getDeploymentId());

            return DeploymentResponse.builder()
                    .deploymentId(deployment.getDeploymentId())
                    .projectId(projectId)
                    .projectUrl(project.getProjectUrl())
                    .status(Status.PENDING)
                    .build();
        }
    }

    public void updateDeploymentStatus(Message message) {
        String deploymentStatus = message.getStatus();
        if (deploymentStatus.equals("SUCCESS") || deploymentStatus.equals("FAILURE")) {
            String deploymentId = message.getDeploymentId();
            Deployment deployment = deploymentRepository.findById(deploymentId).orElse(null);
            if (ObjectUtils.isEmpty(deployment)) {
                log.error("Deployment is not present in database: {}", deploymentId);
            } else {
                if (deploymentStatus.equals("SUCCESS"))
                    deployment.setStatus(Status.SUCCESS);
                else
                    deployment.setStatus(Status.FAILURE);
                deploymentRepository.save(deployment);
            }
        }
    }
}
