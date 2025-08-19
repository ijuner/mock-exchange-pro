package com.mock.exchange.trade_service;

import static org.junit.Assert.assertThat;

import java.time.Instant;
import java.util.List;

import org.junit.Test;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.mock.exchange.trade_service.repository.TradeRepository;


@DataMongoTest
@Testcontainers
public class TradeRepositoryIT {
 // 启动独立的 Mongo 容器（测试级别）
    @Container
    static MongoDBContainer mongo = new MongoDBContainer("mongo:7.0");

    @DynamicPropertySource
    static void mongoProps(DynamicPropertyRegistry registry) {
        // Spring Boot 3.x: 使用容器提供的 replica set URL
        registry.add("spring.data.mongodb.uri", mongo::getReplicaSetUrl);
    }

    private final TradeRepository tradeRepository;

    TradeRepositoryIT(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    @Test
    void should_save_and_query_latest_trades() {
        long now = Instant.now().toEpochMilli();

        tradeRepository.save(new Trade("B1","S1","TD",100_00,10, now-1000));
        tradeRepository.save(new Trade("B2","S2","TD",101_00,10, now-500));
        tradeRepository.save(new Trade("B3","S3","TD",102_00,10, now));

        List<Trade> latest = tradeRepository.findTop10BySymbolOrderByTsDesc("TD");
        assertThat(latest).hasSize(3);
        assertThat(latest.get(0).getPrice()).isEqualTo(102_00);
        assertThat(latest.get(1).getPrice()).isEqualTo(101_00);
        assertThat(latest.get(2).getPrice()).isEqualTo(100_00);

        assertThat(tradeRepository.countBySymbol("TD")).isEqualTo(3);
    }

    
}
