package com.task_project.campaign_manager.dtos;

import com.task_project.campaign_manager.data.Campaign;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter @Setter
public class KeywordDto {
    private Long id;

    private String name;

    public KeywordDto(Long id, String name){
        this.id = id;
        this.name = name;
    }
}
