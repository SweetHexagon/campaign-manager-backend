package com.task_project.campaign_manager.repositories;

import com.task_project.campaign_manager.data.Campaign;
import com.task_project.campaign_manager.data.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampaignRepository extends JpaRepository<Campaign, Integer> {
    Campaign findByCampaignName(String name);
    List<Campaign> findByUser(User user);

}
