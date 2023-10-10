package com.decentralizedDB.node.node;

import java.util.ArrayList;

public class NodesData {

    static ArrayList<String> nodesHttp = new ArrayList<>();
    static int numberOfNodes;

    private NodesData() {
    }

    public static String nextNode() {
        int next =  nodesHttp.indexOf(NodeInfo.getHttpConnection()) + 1;
        if (next == numberOfNodes)
            return nodesHttp.get(0);
        return nodesHttp.get(next);
    }

    public static ArrayList<String> getNodesLink() {
        return nodesHttp;
    }

    public static void addNode(String nodeLink){
        if(nodesHttp.contains(nodeLink))
            return;
        nodesHttp.add(nodeLink);
        numberOfNodes = nodesHttp.size();
    }

    public static void setNodesLink(ArrayList<String> nodesLink) {
        NodesData.nodesHttp = nodesLink;
        numberOfNodes = nodesLink.size();
    }

    public static int getNumberOfNodes() {
        return numberOfNodes;
    }

    @Override
    public String toString() {
        return "NodesData{" +
                "numberOfNodes= " + numberOfNodes +
                "nodesLink=" + nodesHttp +
                '}';
    }
}
