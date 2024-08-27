package com.task_project.campaign_manager.repositories;

import com.task_project.campaign_manager.data.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
