package com.task_project.campaign_manager.services;

import com.task_project.campaign_manager.data.Keyword;
import com.task_project.campaign_manager.dtos.KeywordDto;
import com.task_project.campaign_manager.repositories.KeywordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class KeywordService {
    private final KeywordRepository keywordRepository;

    @Autowired
    public KeywordService(KeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;
    }


    public List<KeywordDto> getAllKeywords() {
        List<Keyword> keywords = keywordRepository.findAll();
        return convertKeywordsToDtos(keywords);
    }


    public KeywordDto convertKeywordToDto(Keyword keyword) {
        return new KeywordDto(keyword.getId(), keyword.getName());
    }


    public List<KeywordDto> convertKeywordsToDtos(List<Keyword> keywords) {
        return keywords.stream()
                .map(this::convertKeywordToDto)
                .collect(Collectors.toList());
    }
}
