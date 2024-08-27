package com.task_project.campaign_manager.exceptions;

public class CampaignAlreadyExistsException extends RuntimeException {
    public CampaignAlreadyExistsException(String message) {
        super(message);
    }
}
