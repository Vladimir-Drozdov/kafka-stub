package org.example.kafkastub.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.kafkastub.dto.KafkaMessage;
import org.example.kafkastub.entity.PostedMessageEntity;
import org.example.kafkastub.repository.PostedMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PostedMessageKafkaListener {

    private static final Logger log = LoggerFactory.getLogger(PostedMessageKafkaListener.class);

    private final ObjectMapper objectMapper;
    private final PostedMessageRepository repository;

    public PostedMessageKafkaListener(ObjectMapper objectMapper, PostedMessageRepository repository) {
        this.objectMapper = objectMapper;
        this.repository = repository;
    }

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void onMessage(ConsumerRecord<String, String> record) {
        String payload = record.value();
        log.info("Получено сообщение из топика '{}', partition={}, offset={}: {}",
                record.topic(), record.partition(), record.offset(), payload);

        KafkaMessage kafkaMessage;
        try {
            kafkaMessage = objectMapper.readValue(payload, KafkaMessage.class);
        } catch (Exception e) {
            log.error("Не удалось разобрать сообщение из Kafka как JSON: {}", payload, e);
            return;
        }

        PostedMessageEntity entity = new PostedMessageEntity(
                kafkaMessage.getMsgId(),
                kafkaMessage.getTimestamp(),
                kafkaMessage.getMethod(),
                kafkaMessage.getUri(),
                record.partition(),
                record.offset()
        );

        repository.save(entity);
        log.info("Сообщение с msg_id={} сохранено в PostgreSQL, id записи={}", entity.getMsgId(), entity.getId());
    }
}