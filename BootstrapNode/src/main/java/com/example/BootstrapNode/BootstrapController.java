package com.example.BootstrapNode;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@RestController
@RequestMapping("/Bootstrapping")
public class BootstrapController {
    static RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/nodes")
    public ArrayList<String> nodes() {
        return Nodes.getNodesHttp();
    }

    @PostMapping("/receiveNode")
    public void receiveNode(@RequestBody String nodeLink) {
        Nodes.addNode(nodeLink);
        System.out.println("Received node: " + nodeLink);
        sendNodes();
    }

    @GetMapping("/sendNodes")
    public static void sendNodes() {
        for (String nodeLink : Nodes.getNodesHttp()) {
            ArrayList<String> requestContent = Nodes.getNodesHttp();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String url = nodeLink + "/Bootstrap/getNodes";
            HttpEntity<ArrayList<String>> requestEntity = new HttpEntity<>(requestContent, headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            System.out.println(responseEntity.getBody());
        }
    }
}
