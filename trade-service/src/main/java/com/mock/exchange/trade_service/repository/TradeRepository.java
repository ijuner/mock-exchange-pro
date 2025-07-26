package com.mock.exchange.trade_service.repository;

import com.mock.exchange.trade_service.entity.TradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * JPA repository for accessing trade records.
 */
public interface TradeRepository extends JpaRepository<TradeEntity, Long> {
    List<TradeEntity> findBySymbol(String symbol);
}