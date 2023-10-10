package com.decentralizedDB.node.commands;

import com.decentralizedDB.node.databaseManager.Database;
import org.springframework.stereotype.Service;

@Service
class DatabaseExecutor extends ExecuteHandler{

    private static Database database;

    private DatabaseExecutor(Database database) {
        DatabaseExecutor.database = database;
    }

    public static String executeQuery(String databaseQuery) {
        String[] queryParts = databaseQuery.split(" ");
        if(queryParts.length != 2){
            System.out.println("=====================");
            System.out.println("databaseQuery");
            System.out.println(databaseQuery);
            System.out.println("=====================");
            throw new IllegalArgumentException("Error wrong database name.");
        }
        if (queryParts[0].equals("use"))
            return database.createDatabase(queryParts[1]);
        if (queryParts[0].equals("drop"))
            return database.dropDatabase(queryParts[1]);
        throw new IllegalArgumentException("Error wrong query");
    }
}
