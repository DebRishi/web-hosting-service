package com.debmallick.hosting.controller;

import com.debmallick.hosting.model.Message;
import com.debmallick.hosting.service.DynamoDbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs")
public class LogQueryController {

    @Autowired
    DynamoDbService service;

    @GetMapping("/{deploymentId}")
    ResponseEntity<List<Message>> getLogs(@PathVariable String deploymentId) {
        try {
            if (ObjectUtils.isEmpty(deploymentId)) {
                return ResponseEntity.badRequest().body(null);
            } else {
                return ResponseEntity.ok(service.getLogs(deploymentId));
            }
        } catch (Exception x) {
            return ResponseEntity.internalServerError().body(null);
        }

    }
}
