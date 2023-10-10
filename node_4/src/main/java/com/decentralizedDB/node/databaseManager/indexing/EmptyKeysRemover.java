package com.decentralizedDB.node.databaseManager.indexing;

import com.google.gson.*;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class EmptyKeysRemover {

    private EmptyKeysRemover() {
    }

    public static void removeEmptyKeys(File file) throws IOException {
        JsonObject jsonObject = readJsonFile(file);
        removeEmptyKeys(jsonObject);
        writeJsonFile(file, jsonObject);
    }

    private static JsonObject readJsonFile(File file) throws IOException {
        try (Reader reader = new FileReader(file)) {
            JsonParser parser = new JsonParser();
            return parser.parse(reader).getAsJsonObject();
        }
    }

    private static void writeJsonFile(File file, JsonObject jsonObject) throws IOException {
        try (Writer writer = new FileWriter(file)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(jsonObject, writer);
        }
    }

    private static void removeEmptyKeys(JsonObject jsonObject) {
        Set<String> keysToRemove = new HashSet<>();

        for (String key : jsonObject.keySet()) {
            JsonArray jsonArray = jsonObject.getAsJsonArray(key);

            if (jsonArray != null && jsonArray.size() == 0) {
                keysToRemove.add(key);
            }
        }

        for (String key : keysToRemove) {
            jsonObject.remove(key);
        }
    }
}
