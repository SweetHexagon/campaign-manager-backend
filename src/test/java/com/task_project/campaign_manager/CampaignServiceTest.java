package com.task_project.campaign_manager;

import com.task_project.campaign_manager.data.Campaign;
import com.task_project.campaign_manager.data.Keyword;
import com.task_project.campaign_manager.dtos.CampaignDto;
import com.task_project.campaign_manager.dtos.KeywordDto;
import com.task_project.campaign_manager.exceptions.CampaignAlreadyExistsException;
import com.task_project.campaign_manager.exceptions.CampaignNotFoundException;
import com.task_project.campaign_manager.repositories.CampaignRepository;
import com.task_project.campaign_manager.repositories.KeywordRepository;
import com.task_project.campaign_manager.services.CampaignService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class CampaignServiceTest {
    @Mock
    private CampaignRepository campaignRepository;

    @InjectMocks
    private CampaignService campaignService;


    @Test
    void testConvertCampaignToDto() {
        Campaign campaign = new Campaign(
                "Campaign Name",
                "Description",
                new ArrayList<>(),
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(1000),
                true,
                "Town",
                10,
                null
        );

        CampaignDto campaignDto = campaignService.convertCampaignToDto(campaign);

        assertNotNull(campaignDto);
        assertEquals("Campaign Name", campaignDto.getCampaignName());
        assertEquals("Description", campaignDto.getDescription());
        assertEquals(BigDecimal.valueOf(100), campaignDto.getBidAmount());
        assertEquals(BigDecimal.valueOf(1000), campaignDto.getFund());
        assertTrue(campaignDto.getStatus());
        assertEquals("Town", campaignDto.getTown());
        assertEquals(10, campaignDto.getRadius());
    }

    @Test
    void testCreateNewCampaign_Success() {
        Campaign campaign = new Campaign(
                "New Campaign",
                "Description",
                new ArrayList<>(),
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(1000),
                true,
                "Town",
                10,
                null
        );

        when(campaignRepository.findByCampaignName("New Campaign")).thenReturn(null);
        when(campaignRepository.save(any(Campaign.class))).thenReturn(campaign);

        Campaign result = campaignService.createNewCampaign(campaign);

        assertNotNull(result);
        assertEquals("New Campaign", result.getCampaignName());
        verify(campaignRepository, times(1)).save(campaign);
    }

    @Test
    void testCreateNewCampaign_CampaignAlreadyExists() {

        Campaign existingCampaign = new Campaign(
                "Existing Campaign",
                "Description",
                new ArrayList<>(),
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(1000),
                true,
                "Town",
                10,
        null
        );

        when(campaignRepository.findByCampaignName("Existing Campaign")).thenReturn(existingCampaign);

        Campaign newCampaign = new Campaign(
                "Existing Campaign",
                "Description",
                new ArrayList<>(),
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(1000),
                true,
                "Town",
                10,
        null
        );

        assertThrows(CampaignAlreadyExistsException.class, () -> {
            campaignService.createNewCampaign(newCampaign);
        });

        verify(campaignRepository, never()).save(any(Campaign.class));
    }

    @Test
    void testGetCampaignByName_Success() {
        Campaign campaign = new Campaign(
                "Existing Campaign",
                "Description",
                new ArrayList<>(),
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(1000),
                true,
                "Town",
                10,
                null
        );

        when(campaignRepository.findByCampaignName("Existing Campaign")).thenReturn(campaign);

        Campaign result = campaignService.getCampaignByName("Existing Campaign");

        assertNotNull(result);
        assertEquals("Existing Campaign", result.getCampaignName());
    }

    @Test
    void testGetCampaignByName_CampaignNotFound() {
        when(campaignRepository.findByCampaignName("Nonexistent Campaign")).thenReturn(null);

        assertThrows(CampaignNotFoundException.class, () -> {
            campaignService.getCampaignByName("Nonexistent Campaign");
        });
    }
}
