package com.task_project.campaign_manager.data;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Campaign {

    public Campaign(String campaignName, String description, List<Keyword> keywords,
                    BigDecimal bidAmount, BigDecimal fund, Boolean status,
                    Town town, int radius, User user) {
        this.campaignName = campaignName;
        this.description = description;
        this.keywords = keywords;
        this.bidAmount = bidAmount;
        this.fund = fund;
        this.status = status;
        this.town = town;
        this.radius = radius;
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String campaignName;
    private String description;

    @ManyToMany
    @JoinTable(
            name = "campaign_keywords",
            joinColumns = @JoinColumn(name = "campaign_id"),
            inverseJoinColumns = @JoinColumn(name = "keyword_id")
    )
    private List<Keyword> keywords = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private BigDecimal bidAmount;
    private BigDecimal fund;
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "town_id")
    private Town town;

    private int radius;

    public List<Long> getKeywordsId() {
        List<Long> ids = new ArrayList<>();
        for (Keyword keyword : keywords) {
            ids.add(keyword.getId());
        }
        return ids;
    }

    public void addKeyword(Keyword keyword) {
        if (!keywords.contains(keyword)) {
            keywords.add(keyword);
            keyword.getCampaigns().add(this);
        }
    }

    public void removeKeyword(Keyword keyword) {
        if (keywords.contains(keyword)) {
            keywords.remove(keyword);
            keyword.getCampaigns().remove(this);
        }
    }

}
