package com.example.BootstrapNode;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class ClientController {

    @GetMapping
    public String Home() {
        return "Bootstrap node is working";
    }

    @PostMapping("/getConnection")
    public String getConnection(@RequestBody String clientLink) {
        String nodeToServe = Nodes.getConnection(clientLink);
        System.out.println("Get connection by client: " + clientLink + " _ will serve by: " + nodeToServe);
        return nodeToServe;
    }
}
