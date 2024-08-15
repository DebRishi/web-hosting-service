package com.debmallick.hosting.repository;

import com.debmallick.hosting.repository.entity.Deployment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeploymentRepository extends JpaRepository<Deployment, String> {
}
