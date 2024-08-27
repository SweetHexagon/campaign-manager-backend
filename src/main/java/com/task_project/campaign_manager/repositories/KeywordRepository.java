package com.task_project.campaign_manager.repositories;

import com.task_project.campaign_manager.data.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {
     Keyword findByName(String name);
     Keyword findById(long id);
}
