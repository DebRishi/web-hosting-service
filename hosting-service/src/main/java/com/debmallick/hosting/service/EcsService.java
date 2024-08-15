package com.debmallick.hosting.service;

import com.debmallick.hosting.constant.Status;
import com.debmallick.hosting.repository.DeploymentRepository;
import com.debmallick.hosting.repository.entity.Deployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import software.amazon.awssdk.services.ecs.EcsClient;
import software.amazon.awssdk.services.ecs.model.*;

import java.util.List;

@Service
public class EcsService {

    private static final Logger log = LoggerFactory.getLogger(EcsService.class);
    @Autowired
    EcsClient client;

    @Value("${aws.config.cluster.arn}")
    private String clusterArn;

    @Value("${aws.config.task.definition.arn}")
    private String taskDefinitionArn;

    @Value("${aws.config.task.container.name}")
    private String taskContainerName;

    @Value("${aws.config.subnets}")
    private List<String> subnets;

    @Value("${aws.config.security.groups}")
    private List<String> securityGroups;

    @Value("${project.url.template}")
    private String projectUrlTemplate;

    @Autowired
    private DeploymentRepository deploymentRepository;

    public void runTask(String gitUrl, String projectId, String deploymentId) {

        Deployment deployment = deploymentRepository.findById(deploymentId).orElse(null);

        if (ObjectUtils.isEmpty(deployment)) {
            log.error("Invalid deployment id was provided: {} : projectId[{}]", deploymentId, projectId);
            return;
        }

        RunTaskRequest request = RunTaskRequest.builder()
                .cluster(clusterArn)
                .taskDefinition(taskDefinitionArn)
                .launchType(LaunchType.FARGATE)
                .count(1)
                .networkConfiguration(NetworkConfiguration.builder()
                        .awsvpcConfiguration(AwsVpcConfiguration.builder()
                                .assignPublicIp(AssignPublicIp.ENABLED)
                                .subnets(subnets)
                                .securityGroups(securityGroups)
                                .build())
                        .build())
                .overrides(TaskOverride.builder()
                        .containerOverrides(ContainerOverride.builder()
                                .name(taskContainerName)
                                .environment(
                                        KeyValuePair.builder().name("GIT_REPO_URL").value(gitUrl).build(),
                                        KeyValuePair.builder().name("PROJECT_ID").value(projectId).build(),
                                        KeyValuePair.builder().name("DEPLOYMENT_ID").value(deploymentId).build()
                                )
                                .build())
                        .build())
                .build();

        client.runTask(request);

        deployment.setStatus(Status.RUNNING);
        deploymentRepository.save(deployment);
    }

    public String getProjectUrlFromId(String projectId) {
        return String.format(projectUrlTemplate, projectId);
    }
}
