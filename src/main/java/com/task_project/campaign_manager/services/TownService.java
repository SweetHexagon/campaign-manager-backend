package com.task_project.campaign_manager.services;


import com.task_project.campaign_manager.data.Town;
import com.task_project.campaign_manager.dtos.TownDto;
import com.task_project.campaign_manager.repositories.TownRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TownService {

    private final TownRepository townRepository;

    @Autowired
    public TownService(TownRepository townRepository) {
        this.townRepository = townRepository;
    }


    public List<TownDto> getAllTowns() {
        List<Town> towns = townRepository.findAll();
        return convertTownsToDtos(towns);
    }


    public TownDto convertTownToDto(Town town) {
        return new TownDto(town.getId(), town.getName());
    }

    public List<TownDto> convertTownsToDtos(List<Town> towns) {
        return towns.stream()
                .map(this::convertTownToDto)
                .collect(Collectors.toList());
    }
}
