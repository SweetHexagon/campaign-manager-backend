package com.task_project.campaign_manager.config;
import com.task_project.campaign_manager.data.Campaign;
import com.task_project.campaign_manager.data.Keyword;
import com.task_project.campaign_manager.data.Town;
import com.task_project.campaign_manager.data.User;
import com.task_project.campaign_manager.repositories.CampaignRepository;
import com.task_project.campaign_manager.repositories.KeywordRepository;
import com.task_project.campaign_manager.repositories.TownRepository;
import com.task_project.campaign_manager.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Arrays;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CampaignRepository campaignRepository;
    private final KeywordRepository keywordRepository;
    private final TownRepository townRepository;

    public LoadDatabase(PasswordEncoder passwordEncoder, UserRepository userRepository, CampaignRepository campaignRepository, KeywordRepository keywordRepository, TownRepository townRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.campaignRepository = campaignRepository;
        this.keywordRepository = keywordRepository;
        this.townRepository = townRepository;
    }


    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            User admin = new User("admin", passwordEncoder.encode("admin"));
            User user = new User("user", passwordEncoder.encode("password"));
            admin.setBalance(new BigDecimal("1000000.00"));
            user.setBalance(new BigDecimal("1000000.00"));

            log.info("Preloading " + userRepository.save(admin));
            log.info("Preloading " + userRepository.save(user));

            Keyword keyword1 = new Keyword("Marketing");
            Keyword keyword2 = new Keyword("Sales");
            Keyword keyword3 = new Keyword("Technology");
            log.info("Preloading " + keywordRepository.save(keyword1));
            log.info("Preloading " + keywordRepository.save(keyword2));
            log.info("Preloading " + keywordRepository.save(keyword3));

            Town town1 = new Town("New York");
            Town town2 = new Town("San Francisco");
            log.info("Preloading " + townRepository.save(town1));
            log.info("Preloading " + townRepository.save(town2));

            Campaign campaign1 = new Campaign(
                    "Campaign 1",
                    "This is the first campaign",
                    Arrays.asList(keyword1, keyword2),
                    BigDecimal.valueOf(500),
                    BigDecimal.valueOf(1000),
                    true,
                    town1,
                    10,
                    admin
            );
            Campaign campaign2 = new Campaign(
                    "Campaign 2",
                    "This is the second campaign",
                    Arrays.asList(keyword2, keyword3),
                    BigDecimal.valueOf(300),
                    BigDecimal.valueOf(700),
                    true,
                    town1,
                    20,
                    admin
            );
            log.info("Preloading " + campaignRepository.save(campaign1));
            log.info("Preloading " + campaignRepository.save(campaign2));


        };
    }
}
