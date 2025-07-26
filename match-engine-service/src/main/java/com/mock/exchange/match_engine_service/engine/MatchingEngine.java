package com.mock.exchange.match_engine_service.engine;

import com.mock.exchange.match_engine_service.model.OrderDTO;
import com.mock.exchange.match_engine_service.model.TradeDTO;
import com.mock.exchange.match_engine_service.producer.TradeProducer;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

/**
 * MatchingEngine receives orders and attempts to match BUY vs SELL.
 * It runs in a thread pool and handles concurrency with a BlockingQueue and orderBook map.
 */
@Slf4j
@Component
public class MatchingEngine {
    // 🧵 Thread pool for concurrent order matching
    private final ExecutorService executor  = Executors.newFixedThreadPool(4);
    // ⏳ Blocking queue holds incoming orders from Kafka consumer
    private final BlockingQueue<OrderDTO> orderQueue = new LinkedBlockingQueue<>();

    // 📊 In-memory orderbook: pending BUY and SELL orders, grouped by symbol
    private final Map<String, List<OrderDTO>> orderBook = new ConcurrentHashMap<>();
/**
     * Starts 4 worker threads to pull from queue and match orders
     */
    @PostConstruct
    public void initMatchingThreads() {
        for (int i = 0; i < 4; i++) {
            executor.submit(this::matchOrdersLoop);
        }
    }

     /**
     * Adds an incoming order to the internal processing queue.
     *
     * @param order order to match
     */
    public void enqueue(OrderDTO order) {
        orderQueue.offer(order);
    }

     /**
     * Runs an infinite loop to consume orders from queue and call matcher
     */
    private void matchOrdersLoop(){
        while(true) {
            try {
                OrderDTO order = orderQueue.take();// blocks if queue is empty
                // Call the matcher with the order
                match(order);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Order matching thread interrupted", e);
            }
        }
    }

    private final TradeProducer tradeProducer = null;
    private void match(OrderDTO newOrder) {
        List<OrderDTO> book = orderBook.computeIfAbsent(newOrder.getSymbol(), k -> new CopyOnWriteArrayList<>());

        boolean matched = false;

        for (OrderDTO existing : book) {
            // Match BUY ↔ SELL and price match
            if (!existing.getSide().equalsIgnoreCase(newOrder.getSide())
                    && existing.getPrice() == newOrder.getPrice()) {

                TradeDTO trade = TradeDTO.builder()
                        .symbol(newOrder.getSymbol())
                        .price(newOrder.getPrice())
                        .quantity(Math.min(newOrder.getQuantity(), existing.getQuantity()))
                        .buyOrderId(newOrder.getSide().equalsIgnoreCase("BUY") ? newOrder.getId().toString() : existing.getId().toString())
                        .sellOrderId(newOrder.getSide().equalsIgnoreCase("SELL") ? newOrder.getId().toString() : existing.getId().toString())
                        .timestamp(LocalDateTime.now())
                        .build();

                tradeProducer.sendTrade(trade);

                // 🎯 Match found
                log.info("✅ MATCHED: {} <--> {}", newOrder, existing);
                book.remove(existing);
                matched = true;
                break;
            }
        }

        if (!matched) {
            // No match found, add to orderbook
            book.add(newOrder);
            log.info("🕒 Queued order: {}", newOrder);
        }
    }

}
