package org.example.kafkastub.controller;

import org.example.kafkastub.exception.KafkaPublishException;
import org.example.kafkastub.service.MessagePublishService;
import org.example.kafkastub.dto.PostMessageRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostMessageController {

    private static final Logger log = LoggerFactory.getLogger(PostMessageController.class);

    private final MessagePublishService messagePublishService;

    public PostMessageController(MessagePublishService messagePublishService) {
        this.messagePublishService = messagePublishService;
    }
    @PostMapping("/post-message")
    public ResponseEntity<String> postMessage(@Valid @RequestBody PostMessageRequest request,
                                              HttpServletRequest httpRequest) {
        try {
            messagePublishService.publish(
                    request.getMsgId(),
                    httpRequest.getMethod(),
                    httpRequest.getRequestURI()
            );
            return ResponseEntity.status(HttpStatus.OK).body("OK");
        } catch (KafkaPublishException e) {
            log.error("Ошибка при записи сообщения в Kafka", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Не удалось записать сообщение в Kafka");
        }
    }
}
