package com.decentralizedDB.node.redirection;

import com.decentralizedDB.node.node.NodeInfo;
import com.decentralizedDB.node.node.NodesData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NextAffinity {
    private static RestTemplate restTemplate;

    @Autowired
    public NextAffinity(RestTemplate restTemplate) {
        NextAffinity.restTemplate = restTemplate;
    }

    public static void nextAffinity() {
        String url = NodesData.nextNode() + "/node/setAffinity";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        restTemplate.postForLocation(url, null, headers);
        NodeInfo.setAffinity(false);
        System.out.println("The new affinity: " + NodesData.nextNode());
    }
}
