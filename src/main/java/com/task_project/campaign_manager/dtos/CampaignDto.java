package com.task_project.campaign_manager.dtos;

import com.task_project.campaign_manager.data.Keyword;
import com.task_project.campaign_manager.validation.interfaces.BidAmountLessThanFund;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Getter @Setter
@BidAmountLessThanFund
@NoArgsConstructor
public class CampaignDto {

    @NotNull(message = "Id cannot be null")
    private Long id;

    @NotNull(message = "Campaign name cannot be null")
    @Size(min = 2, max = 100, message = "Campaign name must be between 2 and 100 characters")
    private String campaignName;

    @NotNull(message = "Description cannot be null")
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Keywords cannot be null")
    @Size(min = 1, message = "At least one keyword must be provided")
    private List<KeywordDto> keywords;

    @NotNull(message = "Bid amount cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Bid amount must be greater than 0")
    private BigDecimal bidAmount;

    @NotNull(message = "Fund cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Fund must be greater than 0")
    private BigDecimal fund;

    @NotNull(message = "Status cannot be null")
    private Boolean status;

    @NotNull(message = "Town cannot be null")
    @Size(min = 2, max = 100, message = "Town must be between 2 and 100 characters")
    private String town;

    @Min(value = 1, message = "Radius must be greater than 0")
    private int radius;

    public CampaignDto(Long id,String campaignName, String description, List<KeywordDto> keywords,
                       BigDecimal bidAmount, BigDecimal fund, Boolean status,
                       String town, int radius) {
        this.id = id;
        this.campaignName = campaignName;
        this.description = description;
        this.keywords = keywords;
        this.bidAmount = bidAmount;
        this.fund = fund;
        this.status = status;
        this.town = town;
        this.radius = radius;
    }

}
