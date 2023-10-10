package com.decentralizedDB.node.redirection;

import com.decentralizedDB.node.node.NodesData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ToAffinity {
    private static RestTemplate restTemplate;

    @Autowired
    public ToAffinity(RestTemplate restTemplate) {
        ToAffinity.restTemplate = restTemplate;
    }

    public static void toAffinity(String query) {
        String url = NodesData.nextNode() + "/node/toAffinity";
        restTemplate.postForEntity(url, query, String.class);
        System.out.println("Sent to affinity: " + NodesData.nextNode());
    }
}
