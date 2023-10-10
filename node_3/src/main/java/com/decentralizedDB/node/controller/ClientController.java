package com.decentralizedDB.node.controller;

import com.decentralizedDB.node.commands.ExecuteHandler;
import com.decentralizedDB.node.node.NodeInfo;
import com.decentralizedDB.node.redirection.BroadcastQuery;
import com.decentralizedDB.node.redirection.NextAffinity;
import com.decentralizedDB.node.redirection.ToAffinity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/client")
public class ClientController {


    @PostMapping
    public static Object getQuery(@RequestBody String query) throws IOException {
        if (ExecuteHandler.isReadQuery(query))
            return ExecuteHandler.selectQuery(query);
        else if (NodeInfo.isAffinity()){
            BroadcastQuery.broadcastQuery(query);
            NextAffinity.nextAffinity();
        }
        else
            ToAffinity.toAffinity(query);
        return "Done";
    }

    @PostMapping("test")
    public static Object test() {
        return "Done";
    }



}
