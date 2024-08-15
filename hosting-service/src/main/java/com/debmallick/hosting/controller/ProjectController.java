package com.debmallick.hosting.controller;

import com.debmallick.hosting.model.ProjectDetails;
import com.debmallick.hosting.model.ProjectRequest;
import com.debmallick.hosting.model.ProjectResponse;
import com.debmallick.hosting.repository.entity.Project;
import com.debmallick.hosting.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
@Slf4j
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@RequestBody ProjectRequest request) {
        try {
            if (ObjectUtils.isEmpty(request.getProjectName()) || ObjectUtils.isEmpty(request.getGitUrl())) {
                return ResponseEntity.badRequest().body(null);
            } else {
                return ResponseEntity.ok(projectService.createProject(request.getProjectName(), request.getGitUrl()));
            }
        } catch (Exception x){
            log.error("Error occurred while creating project: {}", request, x);
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<ProjectDetails>> getAllProjects() {
        try {
            return ResponseEntity.ok(projectService.getAllProjects());
        } catch (Exception x) {
            log.error("Error occurred while fetching project details", x);
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> deleteProject(@PathVariable String projectId) {
        try {
            return ResponseEntity.accepted().body(projectService.deleteProject(projectId));
        } catch (Exception x) {
            log.error("Error occurred while project deletion", x);
            return ResponseEntity.internalServerError().body(null);
        }
    }
}
