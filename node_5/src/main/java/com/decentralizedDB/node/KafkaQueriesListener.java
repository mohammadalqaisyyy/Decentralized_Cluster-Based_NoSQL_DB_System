package com.decentralizedDB.node;

import com.decentralizedDB.node.commands.ExecuteHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class KafkaQueriesListener {

    @KafkaListener(topics = "queries", groupId = "node5")
    void Listener(String query) {
        System.out.println("Query received: " + query);
        try {
            ExecuteHandler.query(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
