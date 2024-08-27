package com.task_project.campaign_manager.services;

import com.task_project.campaign_manager.data.Campaign;
import com.task_project.campaign_manager.data.Keyword;
import com.task_project.campaign_manager.data.Town;
import com.task_project.campaign_manager.data.User;
import com.task_project.campaign_manager.dtos.CampaignDto;
import com.task_project.campaign_manager.dtos.KeywordDto;
import com.task_project.campaign_manager.exceptions.CampaignAlreadyExistsException;
import com.task_project.campaign_manager.exceptions.CampaignNotFoundException;
import com.task_project.campaign_manager.repositories.CampaignRepository;
import com.task_project.campaign_manager.repositories.KeywordRepository;
import com.task_project.campaign_manager.repositories.TownRepository;
import com.task_project.campaign_manager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampaignService {


    private final CampaignRepository campaignRepository;
    private final KeywordRepository keywordRepository;
    private final UserRepository userRepository;
    private final TownRepository townRepository;

    @Autowired
    public CampaignService(CampaignRepository campaignRepository, KeywordRepository keywordRepository, UserRepository userRepository, TownRepository townRepository) {
        this.campaignRepository = campaignRepository;
        this.keywordRepository = keywordRepository;
        this.userRepository = userRepository;
        this.townRepository = townRepository;
    }

    @Transactional
    public CampaignDto convertCampaignToDto(Campaign campaign) {


        List<KeywordDto> keywordDtoList = new ArrayList<>();

        for (Keyword keyword : campaign.getKeywords()) {
            keywordDtoList.add(new KeywordDto(keyword.getId(), keyword.getName()));
        }

        CampaignDto campaignDto = new CampaignDto(
                campaign.getId(),
                campaign.getCampaignName(),
                campaign.getDescription(),
                keywordDtoList,
                campaign.getBidAmount(),
                campaign.getFund(),
                campaign.getStatus(),
                campaign.getTown().getName(),
                campaign.getRadius()
        );
        return campaignDto;
    }
    @Transactional
    public Campaign createNewCampaign(Campaign campaign) {

        if (campaignRepository.findByCampaignName(campaign.getCampaignName()) != null){
            throw new CampaignAlreadyExistsException("Campaign with name " + campaign.getCampaignName() + " already exists");
        }

        List<Keyword> existingKeywords = campaign.getKeywords().stream()
                .map(keyword -> {
                    Keyword existingKeyword = keywordRepository.findByName(keyword.getName());
                    if (existingKeyword == null) {
                        throw new IllegalArgumentException("Keyword not found: " + keyword.getName() + ", ask administrator to add new keywords");
                    }
                    return existingKeyword;
                })
                .collect(Collectors.toList());

        campaign.setKeywords(existingKeywords);

        return campaignRepository.save(campaign);
    }

    @Transactional
    public Campaign createCampaignFromDto(CampaignDto campaignDto) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username);

        if (user == null){
            throw new IllegalArgumentException("User not found: " + username);
        }

        if (campaignDto.getFund().compareTo(user.getBalance()) > 0) {
            throw new IllegalArgumentException("Insufficient balance: Campaign fund (" + campaignDto.getFund() + ") exceeds your available balance (" + user.getBalance() + ").");
        }

        Town town = townRepository.findByName(campaignDto.getTown());
        if (town == null){
            throw new IllegalArgumentException("Town not found: " + campaignDto.getTown());
        }

        Campaign campaign = new Campaign();
        campaign.setUser(user);
        campaign.setCampaignName(campaignDto.getCampaignName());
        campaign.setDescription(campaignDto.getDescription());
        campaign.setBidAmount(campaignDto.getBidAmount());
        campaign.setFund(campaignDto.getFund());
        campaign.setStatus(campaignDto.getStatus());

        campaign.setTown(town);
        campaign.setRadius(campaignDto.getRadius());

        List<Keyword> keywords = campaignDto.getKeywords().stream()
                .map(keyword -> {
                    Keyword existingKeyword = keywordRepository.findByName(keyword.getName());
                    if (existingKeyword == null) {
                        throw new IllegalArgumentException("Keyword not found: " + keyword.getName() + ", ask administrator to add new keywords");
                    }
                    return existingKeyword;
                })
                .collect(Collectors.toList());

        campaign.setKeywords(keywords);

        user.setBalance(user.getBalance().subtract(campaign.getFund()));
        return campaignRepository.save(campaign);
    }

    @Transactional
    public Campaign updateCampaign(CampaignDto campaignDto) {
        Campaign existingCampaign = campaignRepository.findByCampaignName(campaignDto.getCampaignName());

        if (existingCampaign == null) {
            throw new IllegalArgumentException("Campaign not found with name: " + campaignDto.getCampaignName());
        }

        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }

        BigDecimal oldFund = existingCampaign.getFund();
        BigDecimal newFund = campaignDto.getFund();
        BigDecimal fundDifference = newFund.subtract(oldFund);

        if (fundDifference.compareTo(BigDecimal.ZERO) > 0 && fundDifference.compareTo(user.getBalance()) > 0) {
            throw new IllegalArgumentException("Insufficient balance: Additional funds required (" + fundDifference + ") exceed your available balance (" + user.getBalance() + ").");
        }

        Town town = townRepository.findByName(campaignDto.getTown());
        if (town == null) {
            throw new IllegalArgumentException("Town not found: " + campaignDto.getTown());
        }

        existingCampaign.setDescription(campaignDto.getDescription());
        existingCampaign.setBidAmount(campaignDto.getBidAmount());
        existingCampaign.setFund(newFund);
        existingCampaign.setStatus(campaignDto.getStatus());
        existingCampaign.setTown(town);
        existingCampaign.setRadius(campaignDto.getRadius());

        List<Keyword> newKeywords = new ArrayList<>();
        for (KeywordDto keywordDto : campaignDto.getKeywords()) {
            Keyword keyword = keywordRepository.findByName(keywordDto.getName());
            if (keyword == null) {
                throw new IllegalArgumentException("Keyword not found with name: " + keywordDto.getName());
            }
            newKeywords.add(keyword);
        }
        existingCampaign.setKeywords(newKeywords);

        if (fundDifference.compareTo(BigDecimal.ZERO) > 0) {
            user.setBalance(user.getBalance().subtract(fundDifference));
        } else {
            user.setBalance(user.getBalance().add(fundDifference.abs()));
        }

        return campaignRepository.save(existingCampaign);
    }


    public List<Campaign> getUserCampaigns() {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException("User not found with username: " + username);
        }

        // Fetch and return the campaigns for this user
        return campaignRepository.findByUser(user);
    }

    public Campaign getCampaignByName(String name){
        Campaign campaign = campaignRepository.findByCampaignName(name);
        if(campaign == null){
            throw new CampaignNotFoundException("Campaign with name '" + name + "' not found.");
        }
        return campaign;
    }

    @Transactional
    public void deleteCampaignByName(String name) {
        Campaign campaign = campaignRepository.findByCampaignName(name);

        if(campaign == null){
            throw new IllegalArgumentException("Campaign not found with name: " + name);
        }

        campaignRepository.delete(campaign);
    }
}
