package org.example.kafkastub.service;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class DelayService {

    private static final Logger log = LoggerFactory.getLogger(DelayService.class);
    private static final int SCHEDULER_POOL_SIZE = 8;

    private final AtomicLong delayMillis = new AtomicLong(0);
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(SCHEDULER_POOL_SIZE);
    private final MeterRegistry meterRegistry;

    public DelayService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }
    @PostConstruct
    public void init() {
        Gauge.builder("kafkastub_response_delay_ms", delayMillis, AtomicLong::get).register(meterRegistry);
    }

    public void setDelayMillis(long millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("Задержка не может быть отрицательной");
        }
        long previous = delayMillis.getAndSet(millis);
        log.info("Задержка ответа изменена: {} мс -> {} мс", previous, millis);
    }

    public long getDelayMillis() {
        return delayMillis.get();
    }

    public ScheduledFuture<?> scheduleAfterDelay(Runnable action) {
        long currentDelay = delayMillis.get();
        return scheduler.schedule(action, currentDelay, TimeUnit.MILLISECONDS);
    }

    @PreDestroy
    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            scheduler.shutdownNow();
        }
    }
}