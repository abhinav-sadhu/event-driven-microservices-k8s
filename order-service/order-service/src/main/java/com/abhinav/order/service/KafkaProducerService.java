package com.abhinav.order.service;

import com.abhinav.order.dto.OrderRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper; // JSON में बदलने के लिए
    private static final String TOPIC = "test-topic";

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendMessage(OrderRequest orderRequest) {
        try {
            // Java Object को JSON String में बदलें
            String jsonMessage = objectMapper.writeValueAsString(orderRequest);
            kafkaTemplate.send(TOPIC, jsonMessage);
            System.out.println("Kafka में JSON मैसेज पब्लिश किया गया: " + jsonMessage);
        } catch (Exception e) {
            System.err.println("JSON कन्वर्ट करने में एरर: " + e.getMessage());
        }
    }
}