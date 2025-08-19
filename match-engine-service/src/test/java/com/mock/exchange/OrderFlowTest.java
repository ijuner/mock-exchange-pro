package com.mock.exchange;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.mock.exchange.match_engine_service.consumer.OrderConsumer;
import com.mock.exchange.match_engine_service.engine.MatchingEngine;
import com.mock.exchange.match_engine_service.model.OrderDTO;
import com.mock.exchange.match_engine_service.model.TradeDTO;
import com.mock.exchange.match_engine_service.producer.TradeProducer;

public class OrderFlowTest {
 TradeProducer tradeProducer;
    MatchingEngine engine;
    OrderConsumer consumer;

    @BeforeEach
    void setUp() {
        tradeProducer = mock(TradeProducer.class);
        engine = new MatchingEngine(tradeProducer, 2, 100);
        consumer = new OrderConsumer(engine);
    }

    @AfterEach
    void tearDown() {
        engine.shutdown();
    }

 @Test
    @DisplayName("Kafka flow (mocked): consumer → engine → producer")
    void end_to_end_flow_mocked() {
        // 模拟收到两条 Kafka 订单消息
        consumer.onMessage(new OrderDTO("S5","TD", SELL, 100_00, 5, 1));
        consumer.onMessage(new OrderDTO("B9","TD", BUY, 100_00, 5, 2));

        ArgumentCaptor<TradeDTO> captor = ArgumentCaptor.forClass(TradeDTO.class);

        Awaitility.await().atMost(Duration.ofSeconds(2)).untilAsserted(() ->
                verify(tradeProducer, times(1)).send(captor.capture())
        );

        TradeDTO t = captor.getValue();
        assertThat(t.symbol()).isEqualTo("TD");
        assertThat(t.price()).isEqualTo(100_00);
        assertThat(t.quantity()).isEqualTo(5);
        assertThat(t.sellOrderId()).isEqualTo("S5");
        assertThat(t.buyOrderId()).isEqualTo("B9");
    }




}
