package com.abhinav.order.controller;

import com.abhinav.order.dto.OrderRequest;
import com.abhinav.order.service.KafkaProducerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class KafkaTestController {

    private final KafkaProducerService producerService;

    public KafkaTestController(KafkaProducerService producerService) {
        this.producerService = producerService;
    }

    // अब यह GET की जगह POST रिक्वेस्ट लेगा और JSON Body एक्सेप्ट करेगा
    @PostMapping("/publish")
    public String createOrder(@RequestBody OrderRequest request) {
        producerService.sendMessage(request);
        return "ऑर्डर सफलतापूर्वक काफ्का में चला गया: " + request.getProductName();
    }
}