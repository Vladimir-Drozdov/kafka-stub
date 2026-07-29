package org.example.kafkastub.controller;

import org.example.kafkastub.exception.KafkaPublishException;
import org.example.kafkastub.service.DelayService;
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
import org.springframework.web.context.request.async.DeferredResult;

@RestController
public class PostMessageController {

    private static final Logger log = LoggerFactory.getLogger(PostMessageController.class);

    private static final long TIMEOUT_SAFETY_MARGIN_MS = 5_000;

    private final MessagePublishService messagePublishService;
    private final DelayService delayService;

    public PostMessageController(MessagePublishService messagePublishService, DelayService delayService) {
        this.messagePublishService = messagePublishService;
        this.delayService = delayService;
    }

    @PostMapping("/post-message")
    public DeferredResult<ResponseEntity<String>> postMessage(@Valid @RequestBody PostMessageRequest request,
                                                                HttpServletRequest httpRequest) {
        long timeoutMs = delayService.getDelayMillis() + TIMEOUT_SAFETY_MARGIN_MS;
        DeferredResult<ResponseEntity<String>> deferredResult = new DeferredResult<>(timeoutMs);

        deferredResult.onTimeout(() -> deferredResult.setErrorResult(
                ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("Превышено время ожидания ответа")));

        String method = httpRequest.getMethod();
        String uri = httpRequest.getRequestURI();

        delayService.scheduleAfterDelay(() -> {
            try {
                messagePublishService.publish(request.getMsgId(), method, uri);
                deferredResult.setResult(ResponseEntity.status(HttpStatus.OK).body("OK"));
            } catch (KafkaPublishException e) {
                log.error("Ошибка при записи сообщения в Kafka", e);
                deferredResult.setResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Не удалось записать сообщение в Kafka"));
            } catch (Exception e) {
                log.error("Непредвиденная ошибка при обработке сообщения", e);
                deferredResult.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Внутренняя ошибка сервера"));
            }
        });

        return deferredResult;
    }
}