package org.example.kafkastub.service;

import org.example.kafkastub.dto.KafkaMessage;
import org.example.kafkastub.exception.KafkaPublishException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class MessagePublishService {

    private static final Logger log = LoggerFactory.getLogger(MessagePublishService.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final String topic;

    public MessagePublishService(KafkaTemplate<String, String> kafkaTemplate,
                                 ObjectMapper objectMapper,
                                 @Value("${app.kafka.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.topic = topic;
    }

    public void publish(String msgId, String method, String uri) {
        long timestampMillis = System.currentTimeMillis();

        KafkaMessage message = new KafkaMessage(
                msgId,
                String.valueOf(timestampMillis),
                method,
                uri
        );

        String payload;
        try {
            payload = objectMapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new KafkaPublishException("Не удалось сериализовать сообщение в JSON", e);
        }

        try {
            kafkaTemplate.send(topic, msgId, payload).get(5, TimeUnit.SECONDS);
            log.info("Сообщение отправлено в топик '{}': {}", topic, payload);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            log.error("Не удалось отправить сообщение в Kafka: {}", payload, e);
            throw new KafkaPublishException("Не удалось отправить сообщение в Kafka", e);
        }
    }
}