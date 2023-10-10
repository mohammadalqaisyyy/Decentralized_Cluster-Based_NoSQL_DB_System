package com.decentralizedDB.node.node;

public class NodeInfo {
    static String httpConnection = "http://node1:8081";
    static int id;
    static boolean affinity = true;

    private NodeInfo() {
    }

    public static String getHttpConnection() {
        return httpConnection;
    }

    public static void setHttpConnection(String httpConnection) {
        NodeInfo.httpConnection = httpConnection;
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        NodeInfo.id = id;
    }

    public static boolean isAffinity() {
        return affinity;
    }

    public static void setAffinity(boolean affinity) {
        NodeInfo.affinity = affinity;
    }

    @Override
    public String toString() {
        return "NodeInfo{" +
                "httpConnection='" + httpConnection + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
