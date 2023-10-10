package com.decentralizedDB.node.databaseManager.indexing;

import com.google.gson.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;

public class IndexingRemover {

    private IndexingRemover() {
    }

    public static void removeIndex(String directoryPath, String value) throws IOException {
        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".json"));

            if (files != null) {
                for (File file : files) {
                    removeValueFromFile(file, value);
                    EmptyKeysRemover.removeEmptyKeys(file);
                }
            }
        }
    }

    public static void removeValueFromFile(File file, String value) {
        try {
            String content = new String(Files.readAllBytes(file.toPath()));
            JsonObject jsonObject = new Gson().fromJson(content, JsonObject.class);
            for (String key : jsonObject.keySet()) {
                JsonArray jsonArray = jsonObject.getAsJsonArray(key);
                if (jsonArray != null) {
                    jsonArray.remove(new JsonPrimitive(value));
                }
            }
            try (Writer writer = new FileWriter(file)) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(jsonObject, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
