package com.debmallick.hosting.controller;

import com.debmallick.hosting.model.DeploymentRequest;
import com.debmallick.hosting.model.DeploymentResponse;
import com.debmallick.hosting.service.DeploymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deploy")
@Slf4j
public class DeploymentController {

    @Autowired
    private DeploymentService deploymentService;

    @PostMapping
    public ResponseEntity<DeploymentResponse> createDeployment(@RequestBody DeploymentRequest request) {
        try {
            if (ObjectUtils.isEmpty(request.getProjectId())) {
                return ResponseEntity.badRequest().body(null);
            } else {
                return ResponseEntity.ok(deploymentService.createDeployment(request.getProjectId()));
            }
        } catch (Exception x) {
            log.error("Error occurred while creating deployment: {}", request, x);
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
