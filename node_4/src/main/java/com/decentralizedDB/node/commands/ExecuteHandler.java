package com.decentralizedDB.node.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ExecuteHandler {
    static ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
    static ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
    static ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    public static final StringBuilder queriesList = new StringBuilder();

    ExecuteHandler() {
    }

    public static void query(String query) throws IOException {
        String mainPartsQuery = query.split("\\(")[0];
        try {
            writeLock.lock();
            switch (mainPartsQuery.split("\\.").length) {
                case 3 -> DocumentExecutor.executeQuery(query.replaceAll("\\s", ""));
                case 2 -> CollectionExecutor.executeQuery(query.replaceAll("\\s", ""));
                case 1 -> DatabaseExecutor.executeQuery(query);
                default -> throw new IllegalStateException("Unexpected value: " + query);
            }
        } finally {
            queriesList.append("_").append(query).append("\n");
            writeLock.unlock();
        }
    }

    public static ArrayList<Map<String, String>> selectQuery(String query) throws IOException {
        query = query.replaceAll("\\s", "");
        if (query.split("\\.").length != 3 || !isReadQuery(query))
            throw new IllegalArgumentException("Unexpected value: " + query);
        try {
            readLock.lock();
            return DocumentExecutor.selectQuery(query);
        } finally {
            readLock.unlock();
        }
    }

    public static boolean isReadQuery(String query) {
        return query.contains("select");
    }

    static boolean isWrongName(String name) {
        return !name.matches("^[a-zA-Z0-9]+$");
    }

    public static StringBuilder getQueriesList() {
        return queriesList;
    }
}
