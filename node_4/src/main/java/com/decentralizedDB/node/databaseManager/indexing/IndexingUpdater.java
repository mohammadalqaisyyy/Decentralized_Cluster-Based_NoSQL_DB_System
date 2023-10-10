package com.decentralizedDB.node.databaseManager.indexing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class IndexingUpdater {

    private IndexingUpdater() {
    }

    public static void updateIndex(String directoryPath, String documentKey, Map<String, String> keysValues) throws IOException {
        for (Map.Entry<String, String> entry : keysValues.entrySet()) {
            File file = new File(directoryPath + entry.getKey() + ".json");
            if (Files.exists(file.toPath())) {
                IndexingRemover.removeValueFromFile(file, documentKey);
                EmptyKeysRemover.removeEmptyKeys(file);
            }
            DocumentIndexing.addIndex(directoryPath + entry.getKey() + ".json", entry.getValue(), documentKey);
        }
    }
}
