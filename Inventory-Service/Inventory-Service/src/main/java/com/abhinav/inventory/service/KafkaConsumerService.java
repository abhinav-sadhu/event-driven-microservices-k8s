package com.abhinav.inventory.service;

import com.abhinav.inventory.entity.InventoryEvent;
import com.abhinav.inventory.repository.InventoryRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final InventoryRepository repository;

    // कंस्ट्रक्टर के ज़रिए Repository इंजेक्ट कर रहे हैं
    public KafkaConsumerService(InventoryRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "test-topic", groupId = "my-inventory-group")
    public void consumeMessage(String message) {
        System.out.println("\n====== KAFKA CONSUMER ने मैसेज रिसीव किया ======");
        System.out.println("प्राप्त मैसेज: " + message);

        // डेटाबेस (Neon) में सेव करने का लॉजिक
        InventoryEvent event = new InventoryEvent();
        event.setMessage(message);
        repository.save(event);

        System.out.println("मैसेज सफलतापूर्वक Neon Database में सेव हो गया! ======\n");
    }
}