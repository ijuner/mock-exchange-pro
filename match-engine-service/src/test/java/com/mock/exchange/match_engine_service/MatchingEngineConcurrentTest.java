package com.mock.exchange.match_engine_service;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.mock.exchange.match_engine_service.engine.MatchingEngine;
import com.mock.exchange.match_engine_service.model.OrderDTO;
import com.mock.exchange.match_engine_service.model.TradeDTO;
import com.mock.exchange.match_engine_service.producer.TradeProducer;

public class MatchingEngineConcurrentTest {
 TradeProducer tradeProducer;
    MatchingEngine engine;
 @BeforeEach
    void setUp() {
        tradeProducer = mock(TradeProducer.class);
        engine = new MatchingEngine(tradeProducer, /*workers*/4, /*queueCap*/1000);
    }
@Test
    @DisplayName("Single symbol: price-time priority & basic matching works under concurrency")
    void match_priceTime_priority_under_load() {
        String sym = "TD";

        // 预先挂两笔卖单（更低价格先成交）
        engine.enqueue(new OrderDTO("S1", sym, SELL, 100_00, 50, now()));
        engine.enqueue(new OrderDTO("S2", sym, SELL, 101_00, 50, now()));
        // 并发塞入多个买单（价格100/101混合），验证成交顺序
        for (int i = 0; i < 20; i++) {
            long price = (i % 2 == 0) ? 101_00 : 100_00;
            engine.enqueue(new OrderDTO("B" + i, sym, BUY, price, 5, now()));
        }
        // 捕获成交
        ArgumentCaptor<TradeDTO> captor = ArgumentCaptor.forClass(TradeDTO.class);

        // 等待直到至少有若干成交（50+50 总量，买单总和 20*5=100，正好吃完两档）
        Awaitility.await().atMost(Duration.ofSeconds(3)).untilAsserted(() -> {
            verify(tradeProducer, atLeast(10)).send(captor.capture());
        });

        List<TradeDTO> trades = captor.getAllValues();

        // 断言：优先吃掉100价位的卖单，再吃101（根据我们 BUY 逻辑，先匹配<=买价的最低卖价）
        long s1Qty = trades.stream().filter(t -> t.sellOrderId().equals("S1")).mapToLong(TradeDTO::quantity).sum();
        long s2Qty = trades.stream().filter(t -> t.sellOrderId().equals("S2")).mapToLong(TradeDTO::quantity).sum();
        assertThat(s1Qty).isEqualTo(50);
        assertThat(s2Qty).isEqualTo(50);

        // 所有成交的 symbol 都是 TD
        assertThat(trades).allSatisfy(t -> assertThat(t.symbol()).isEqualTo(sym));
    }

@Test
    @DisplayName("Multi symbol: per-symbol isolation (no cross-symbol matching)")
    void match_multi_symbol_isolation() {
        engine.enqueue(new OrderDTO("S3", "TD", SELL, 100_00, 10, now()));
        engine.enqueue(new OrderDTO("S4", "RBC", SELL, 50_00,  10, now()));

        engine.enqueue(new OrderDTO("B0", "TD",  BUY, 100_00, 10, now()));
        engine.enqueue(new OrderDTO("B1", "RBC", BUY, 50_00,  10, now()));

        ArgumentCaptor<TradeDTO> captor = ArgumentCaptor.forClass(TradeDTO.class);

        Awaitility.await().atMost(Duration.ofSeconds(2)).untilAsserted(() ->
                verify(tradeProducer, times(2)).send(captor.capture())
        );

        var trades = captor.getAllValues();
        assertThat(trades).extracting(TradeDTO::symbol).containsExactlyInAnyOrder("TD", "RBC");
    }

    private static long now() {
        // 简单生成一个递增时间（ns 单位模拟）
        return TimeUnit.MILLISECONDS.toNanos(System.currentTimeMillis())
                + ThreadLocalRandom.current().nextInt(1000);
    }


    

}
