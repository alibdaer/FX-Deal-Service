package com.fx.deal.servuce.test.controller;

import com.fx.deal.servuce.test.dto.DealRequest;
import com.fx.deal.servuce.test.service.DealService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DealController {

    @Autowired
    private DealService dealService; // Inject service

    @PostMapping("/deals")
    public ResponseEntity<String> createDeal(@Valid @RequestBody DealRequest dealRequest) {
        dealService.saveDeal(dealRequest);
        return ResponseEntity.ok("Deal saved successfully");
    }
}