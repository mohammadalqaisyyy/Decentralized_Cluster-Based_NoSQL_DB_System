package com.decentralizedDB.node.databaseManager;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface DatabaseHandler {
    String ROOT_DIRECTORY = "src/main/resources/DB/";
    String DOCUMENT_CACHE = "documentCache";
    ObjectMapper objectMapper = new ObjectMapper();
}
