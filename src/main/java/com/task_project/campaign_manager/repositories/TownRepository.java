package com.task_project.campaign_manager.repositories;

import com.task_project.campaign_manager.data.Town;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TownRepository extends JpaRepository<Town, Long> {
    Town findByName(String name);
}
