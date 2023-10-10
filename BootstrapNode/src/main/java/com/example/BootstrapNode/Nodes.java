package com.example.BootstrapNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Nodes {
    private static final ArrayList<String> nodesHttp = new ArrayList<>();
    private static final Map<String, String> clientConnection = new HashMap<>();

    private Nodes() {
    }

    public static ArrayList<String> getNodesHttp() {
        return nodesHttp;
    }

    public static String getConnection(String clientLink) {
        if (clientConnection.containsKey(clientLink))
            return clientConnection.get(clientLink);
        String nodeConnection = nodesHttp.get(minimumNodeLoad());
        clientConnection.put(clientLink, nodeConnection);
        return nodeConnection;
    }
    public static <K, V> int countOccurrences(Map<K, V> map, V valueToFind) {
        return (int) map.values().stream().filter(value -> value.equals(valueToFind)).count();
    }

    private static int minimumNodeLoad() {
        int minCount = Integer.MAX_VALUE, indexOfMin = -1;
        for (int i = 0; i < nodesHttp.size(); i++) {
            int temp = countOccurrences(clientConnection, nodesHttp.get(i));
            if (temp < minCount) {
                minCount = temp;
                indexOfMin = i;
            }
        }
        return indexOfMin;
    }

    public static void addNode(String nodeLink) {
        if (nodesHttp.contains(nodeLink))
            return;
        nodesHttp.add(nodeLink);
        BootstrapController.sendNodes();
    }

    public static void sendNode() {

    }

    @Override
    public String toString() {
        return "NodesInfo{" + nodesHttp + "}";
    }
}
