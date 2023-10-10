package com.decentralizedDB.node.redirection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BroadcastQuery {
    private static KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public BroadcastQuery(KafkaTemplate<String, String> kafkaTemplate) {
        BroadcastQuery.kafkaTemplate = kafkaTemplate;
    }

    public static void broadcastQuery(String query) {
        kafkaTemplate.send("queries", query);
    }
}
