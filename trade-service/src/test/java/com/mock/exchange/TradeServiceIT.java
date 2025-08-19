package com.mock.exchange;

import static org.junit.Assert.assertThat;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.mock.exchange.trade_service.repository.TradeRepository;

@SpringBootTest
@Testcontainers
@Import(TradeService.class) // 明确导入被测Service（或由ComponentScan自动扫描）
public class TradeServiceIT {
@Container
    static MongoDBContainer mongo = new MongoDBContainer("mongo:7.0");

    @DynamicPropertySource
    static void mongoProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongo::getReplicaSetUrl);
    }

    private final TradeService tradeService;
    private final TradeRepository tradeRepository;

    TradeServiceIT(TradeService tradeService, TradeRepository tradeRepository) {
        this.tradeService = tradeService;
        this.tradeRepository = tradeRepository;
    }

    @BeforeEach
    void clean() {
        tradeRepository.deleteAll();
    }

    @Test
    void record_and_read_latest() {
        long base = Instant.now().toEpochMilli();

        tradeService.recordTrade("B1","S1","TD",100_00,5, base-1000);
        tradeService.recordTrade("B2","S2","TD",101_00,10, base-500);
        Trade saved = tradeService.recordTrade("B3","S3","TD",102_00,7, base);

        assertThat(saved.getId()).isNotBlank();

        List<Trade> latest = tradeService.latestTrades("TD");
        assertThat(latest).extracting(Trade::getPrice)
                .containsExactly(102_00, 101_00, 100_00);

        assertThat(tradeService.totalCount("TD")).isEqualTo(3);
    }
}
