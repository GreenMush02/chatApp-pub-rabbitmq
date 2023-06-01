package com.example.springbootpubrabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PublisherMq {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/sendMessage")
    public ResponseEntity<MessageDto> sendMessage(@RequestBody MessageDto messageDto) {
        try {
            // Wysłanie obiektu MessageDto do kolejki RabbitMQ
            rabbitTemplate.convertAndSend(messageDto.getQueueId(), messageDto);

            System.out.println("Wiadomość wysłana do kolejki RabbitMQ.");
            return ResponseEntity.ok(messageDto);
        } catch (Exception e) {
            System.err.println("Błąd podczas wysyłania wiadomości do kolejki RabbitMQ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/createListener/{queueId}")
    public ResponseEntity<String> createListener(@PathVariable String queueId) {
        rabbitTemplate.convertAndSend("listenerCreator", queueId);
        return ResponseEntity.ok(queueId);
    }
}
