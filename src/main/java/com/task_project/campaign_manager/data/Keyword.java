package com.task_project.campaign_manager.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@Entity
public class Keyword
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "keywords")
    private List<Campaign> campaigns;

    private String name;

    public Keyword(String keyword){
        this.name = keyword;
    }


    public Keyword(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
