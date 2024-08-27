package com.task_project.campaign_manager.dtos;

import com.task_project.campaign_manager.data.Town;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class TownDto {

    private Long id;
    private String name;

    public TownDto() {}

    public TownDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
