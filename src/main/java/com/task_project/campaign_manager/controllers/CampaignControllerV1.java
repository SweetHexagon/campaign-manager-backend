package com.task_project.campaign_manager.controllers;

import com.task_project.campaign_manager.data.Campaign;
import com.task_project.campaign_manager.dtos.CampaignDto;
import com.task_project.campaign_manager.exceptions.CampaignAlreadyExistsException;
import com.task_project.campaign_manager.exceptions.CampaignNotFoundException;
import com.task_project.campaign_manager.services.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/campaigns")
public class CampaignControllerV1 {

    private final CampaignService campaignService;

    @Autowired
    public CampaignControllerV1(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @PostMapping // validation is inside CampaignDto class and service
    public ResponseEntity<CampaignDto> createCampaign(@Valid @RequestBody CampaignDto campaignDto) {
        Campaign campaign = campaignService.createCampaignFromDto(campaignDto);

        CampaignDto campaignDto_ = campaignService.convertCampaignToDto(campaign);
        return new ResponseEntity<>(campaignDto_, HttpStatus.CREATED);
    }

    @GetMapping("/{name}")
    public ResponseEntity<CampaignDto> getCampaignByName (@PathVariable String name) {
        Campaign campaign = campaignService.getCampaignByName(name);
        CampaignDto campaignDto = campaignService.convertCampaignToDto(campaign);
        return new ResponseEntity<>(campaignDto, HttpStatus.OK);
    }

    @GetMapping("/my")
    public ResponseEntity<List<CampaignDto>> getUserCampaigns() {
        List<Campaign> campaigns = campaignService.getUserCampaigns();
        List<CampaignDto> campaignDtos = new ArrayList<>();
        for (Campaign campaign : campaigns) {
            campaignDtos.add(campaignService.convertCampaignToDto(campaign));
        }
        return ResponseEntity.ok(campaignDtos);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteCampaignByName(@PathVariable String name) {
        campaignService.deleteCampaignByName(name);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping()
    public ResponseEntity<CampaignDto> updateCampaign(@Valid @RequestBody CampaignDto campaignDto) {
        Campaign updatedCampaign = campaignService.updateCampaign(campaignDto);
        CampaignDto updatedDto = campaignService.convertCampaignToDto(updatedCampaign);
        return new ResponseEntity<>(updatedDto, HttpStatus.OK);
    }

    @ExceptionHandler(CampaignNotFoundException.class)
    public ResponseEntity<String> handleCampaignNotFoundException(CampaignNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(CampaignAlreadyExistsException.class)
    public ResponseEntity<String> handleCampaignAlreadyExistsException(CampaignAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
