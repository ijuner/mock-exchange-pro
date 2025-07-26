package com.mock.exchange.trade_service.controller;
import com.mock.exchange.trade_service.entity.TradeEntity;
import com.mock.exchange.trade_service.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller to query trade history.
 */
@RestController
@RequestMapping("/api/trades")
@RequiredArgsConstructor
public class TradeController {
 private final TradeRepository tradeRepository;

    @GetMapping
    public List<TradeEntity> getAll() {
        return tradeRepository.findAll();
    }

    @GetMapping("/{symbol}")
    public List<TradeEntity> getBySymbol(@PathVariable String symbol) {
        return tradeRepository.findBySymbol(symbol.toUpperCase());
    }
}
