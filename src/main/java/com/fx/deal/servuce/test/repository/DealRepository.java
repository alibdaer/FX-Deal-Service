package com.fx.deal.servuce.test.repository;

import com.fx.deal.servuce.test.entity.DealEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DealRepository extends JpaRepository<DealEntity, Long> {

    boolean existsByDealId(String dealId);

}