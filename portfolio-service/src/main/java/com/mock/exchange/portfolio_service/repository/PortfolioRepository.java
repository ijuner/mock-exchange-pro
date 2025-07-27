package com.mock.exchange.portfolio_service.repository;

import com.mock.exchange.portfolio_service.entity.PortfolioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface PortfolioRepository extends JpaRepository<PortfolioEntity, Long> {
    Optional<PortfolioEntity> findByUserIdAndSymbol(String userId, String symbol);

}
