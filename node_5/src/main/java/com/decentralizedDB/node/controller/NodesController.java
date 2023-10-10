package com.decentralizedDB.node.controller;

import com.decentralizedDB.node.node.NodeInfo;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/node")
public class NodesController {

    @PostMapping("toAffinity")
    public void toAffinity(@RequestBody String query) throws IOException {
        ClientController.getQuery(query);
    }

    @GetMapping("/getAffinity")
    public boolean getAffinity() {
        return NodeInfo.isAffinity();
    }

    @PostMapping("/setAffinity")
    public void setAffinity() {
        NodeInfo.setAffinity(true);
        System.out.println("I am Affinity: " + NodeInfo.isAffinity());
    }
}
