package com.task_project.campaign_manager.controllers;

import com.task_project.campaign_manager.data.Keyword;
import com.task_project.campaign_manager.data.Town;
import com.task_project.campaign_manager.dtos.KeywordDto;
import com.task_project.campaign_manager.dtos.TownDto;
import com.task_project.campaign_manager.repositories.KeywordRepository;
import com.task_project.campaign_manager.repositories.TownRepository;
import com.task_project.campaign_manager.services.KeywordService;
import com.task_project.campaign_manager.services.TownService;
import org.hibernate.engine.jdbc.env.spi.AnsiSqlKeywords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v1/data")
public class DataController {
    private final TownRepository townRepository;
    private final KeywordRepository keywordRepository;
    private final TownService townService;
    private final KeywordService keywordService;

    @Autowired
    public DataController(TownRepository townRepository, KeywordRepository keywordRepository, TownService townService, KeywordService keywordService) {
        this.townRepository = townRepository;
        this.keywordRepository = keywordRepository;
        this.townService = townService;
        this.keywordService = keywordService;
    }

    @GetMapping("/towns")
    public ResponseEntity<List<TownDto>> getTowns() {
        List<TownDto> towns = townService.getAllTowns();
        return ResponseEntity.ok(towns);
    }

    @GetMapping("/keywords")
    public ResponseEntity<List<KeywordDto>> getKeywords() {
        List<KeywordDto> keywords = keywordService.getAllKeywords();
        return ResponseEntity.ok(keywords);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
