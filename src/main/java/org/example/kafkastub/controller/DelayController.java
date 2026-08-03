package org.example.kafkastub.controller;

import org.example.kafkastub.service.DelayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
    Управление искусственной задержкой ответа /post-message в рантайме, без перезапуска приложения.
    POST  /delay?millis=10000   - выставить задержку 10 секунд
    POST  /delay?millis=0     - отключить задержку
    GET  /delay           - узнать текущее значение
*/

@RestController
public class DelayController {

    private final DelayService delayService;

    public DelayController(DelayService delayService) {
        this.delayService = delayService;
    }

    @PostMapping("/delay")
    public ResponseEntity<String> setDelay(@RequestParam long millis) {
        delayService.setDelayMillis(millis);
        return ResponseEntity.ok("Задержка установлена: " + millis + " мс");
    }

    @GetMapping("/delay")
    public ResponseEntity<Long> getDelay() {
        return ResponseEntity.ok(delayService.getDelayMillis());
    }
}