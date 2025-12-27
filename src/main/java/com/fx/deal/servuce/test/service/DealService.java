package com.fx.deal.servuce.test.service;

import com.fx.deal.servuce.test.dto.DealRequest;
import com.fx.deal.servuce.test.entity.DealEntity;
import com.fx.deal.servuce.test.repository.DealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DealService {

    @Autowired
    private DealRepository dealRepository;

    public void saveDeal(DealRequest dealRequest) {
        if (dealRepository.existsByDealId(dealRequest.getDealId())) {
            throw new IllegalArgumentException("Duplicate dealId: " + dealRequest.getDealId());
        }

        DealEntity entity = new DealEntity();
        entity.setDealId(dealRequest.getDealId());
        entity.setFromCurrency(dealRequest.getFromCurrency());
        entity.setToCurrency(dealRequest.getToCurrency());
        entity.setTimestamp(dealRequest.getTimestamp());
        entity.setAmount(dealRequest.getAmount());

        dealRepository.save(entity);
    }
}