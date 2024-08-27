package com.task_project.campaign_manager.validation.impl;

import com.task_project.campaign_manager.dtos.CampaignDto;
import com.task_project.campaign_manager.validation.interfaces.BidAmountLessThanFund;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BidAmountLessThanFundValidator implements ConstraintValidator<BidAmountLessThanFund, CampaignDto> {

    @Override
    public boolean isValid(CampaignDto campaignDto, ConstraintValidatorContext context) {

        return campaignDto.getBidAmount().compareTo(campaignDto.getFund()) < 0;

    }
}
