package com.task_project.campaign_manager;


import com.task_project.campaign_manager.controllers.CampaignControllerV1;
import com.task_project.campaign_manager.data.Campaign;
import com.task_project.campaign_manager.dtos.CampaignDto;
import com.task_project.campaign_manager.exceptions.CampaignAlreadyExistsException;
import com.task_project.campaign_manager.exceptions.CampaignNotFoundException;
import com.task_project.campaign_manager.services.CampaignService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CampaignControllerV1Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CampaignService campaignService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateCampaign_Success() throws Exception {
        CampaignDto campaignDto = new CampaignDto(
                Long.valueOf(1),
                "New Campaign",
                "Description",
                new ArrayList<>(),
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(1000),
                true,
                "Town",
                10
        );

        when(campaignService.createCampaignFromDto(any(CampaignDto.class))).thenReturn(new Campaign());
        when(campaignService.convertCampaignToDto(any(Campaign.class))).thenReturn(campaignDto);

        mockMvc.perform(post("/api/v1/campaigns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"campaignName\":\"New Campaign\", \"description\":\"Description\", \"keywords\":[], \"bidAmount\":100, \"fund\":1000, \"status\":true, \"town\":\"Town\", \"radius\":10}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))  // Ensure the Content-Type is application/json
                .andExpect(jsonPath("$.campaignName").value("New Campaign"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.keywords").isEmpty())
                .andExpect(jsonPath("$.bidAmount").value(100))
                .andExpect(jsonPath("$.fund").value(1000))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.town").value("Town"))
                .andExpect(jsonPath("$.radius").value(10));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateCampaign_CampaignAlreadyExists() throws Exception {
        when(campaignService.createCampaignFromDto(any(CampaignDto.class))).thenThrow(new CampaignAlreadyExistsException("Campaign already exists"));

        mockMvc.perform(post("/api/v1/campaigns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"campaignName\":\"Existing Campaign\", \"description\":\"Description\", \"keywords\":[], \"bidAmount\":100, \"fund\":1000, \"status\":true, \"town\":\"Town\", \"radius\":10}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Campaign already exists"));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetCampaignByName_NotFound() throws Exception {
        when(campaignService.getCampaignByName("Nonexistent Campaign")).thenThrow(new CampaignNotFoundException("Campaign not found"));

        mockMvc.perform(get("/api/v1/campaigns/Nonexistent Campaign")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Campaign not found"));
    }
}
