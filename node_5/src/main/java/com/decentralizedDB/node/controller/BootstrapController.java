package com.decentralizedDB.node.controller;

import com.decentralizedDB.node.node.NodeInfo;
import com.decentralizedDB.node.node.NodesData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@RestController
@RequestMapping("/Bootstrap")
public class BootstrapController {
    static RestTemplate restTemplate;
    static HttpHeaders headers;

    @Autowired
    public BootstrapController(RestTemplate restTemplate) {
        BootstrapController.restTemplate = restTemplate;
        BootstrapController.headers = new HttpHeaders();
    }

    @GetMapping("/sendMyInfo")
    public static boolean sendMyInfo() {
        String url = "http://bootstrap-node:8080/Bootstrapping/receiveNode";
        String requestContent = NodeInfo.getHttpConnection();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestContent, String.class);
        return responseEntity.getStatusCode().is2xxSuccessful();
    }

    @PostMapping("/getNodes")
    public ResponseEntity<String> getNodes(@RequestBody ArrayList<String> requestList) {
        for (String nodeLink : requestList) {
            System.out.println("Received node link: " + nodeLink);
            NodesData.addNode(nodeLink);
        }
        return ResponseEntity.ok("Received and processed the list");
    }

    public static void startup() {
        System.out.println("My info sent to Bootstrap is: " + sendMyInfo());
    }
}
