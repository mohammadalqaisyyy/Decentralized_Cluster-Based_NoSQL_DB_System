package com.decentralizedDB.node.databaseManager.indexing;

import com.google.gson.*;

import java.io.*;

public class DocumentIndexing {
    private static File jsonFile;
    private static JsonObject jsonObject;

    private DocumentIndexing(){
    }

    public static void addIndex(String filePath, String key, String newValue) throws IOException {
        try {
            JsonFileUpdater(filePath);
            addValueToList(key, newValue);
        } catch (Exception e) {
            throw new IOException("Error indexing - " + e.getMessage());
        }
    }

    private static void JsonFileUpdater(String filePath) {
        jsonFile = new File(filePath);
        jsonObject = initializeJsonObject();
    }

    public static void addValueToList(String key, String newValue) {
        JsonArray list = jsonObject.getAsJsonArray(key);
        if (list == null) {
            list = new JsonArray();
        }
        list.add(newValue);
        jsonObject.add(key, list);
        saveChanges();
    }

    private static JsonObject initializeJsonObject() {
        if (jsonFile.exists())
            return readJsonFile(jsonFile);
        return new JsonObject();
    }

    private static JsonObject readJsonFile(File file) {
        try (Reader reader = new FileReader(file)) {
            JsonParser parser = new JsonParser();
            return parser.parse(reader).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void saveChanges() {
        try (Writer writer = new FileWriter(jsonFile)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(jsonObject, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
