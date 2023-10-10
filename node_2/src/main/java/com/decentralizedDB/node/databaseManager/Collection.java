package com.decentralizedDB.node.databaseManager;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class Collection implements DatabaseHandler {

    public Collection() {
    }

    public String createCollection(String database, String collectionName) {
        Path collectionPath = Path.of(ROOT_DIRECTORY + database + "/" + collectionName);
        Path collectionIndexPath = Path.of(ROOT_DIRECTORY + database + "/" + collectionName + "/index");
        if (database == null)
            return "Error select the Database.";
        if (Files.exists(collectionPath))
            return "Collection " + collectionName + " already exists.";
        try {
            Files.createDirectories(collectionPath);
            Files.createDirectories(collectionIndexPath);
            return "Collection " + collectionName + " created successfully.";
        } catch (IOException e) {
            return "Error creating " + collectionName + " collection: " + e.getMessage();
        }
    }

    @CacheEvict(value = DOCUMENT_CACHE, key = "#database + '-' + #collection + '-*'")
    public String dropCollection(String database, String collection) {
        Path collectionPath = Path.of(ROOT_DIRECTORY + database + "/" + collection);
        if (database == null)
            return "Error select the Database.";
        try {
            if (Files.exists(collectionPath)) {
                Files.walk(collectionPath)
                        .sorted((path1, path2) -> -path1.compareTo(path2))
                        .forEach(path -> {
                            try {
                                Files.deleteIfExists(path);
                            } catch (IOException e) {
                                throw new RuntimeException();
                            }
                        });
                return "Collection " + collection + " deleted successfully.";
            } else {
                return "Collection " + collection + " does not exist.";
            }
        } catch (IOException e) {
            return "Error deleting " + collection + " Collection: " + e.getMessage();
        }
    }

    @Override
    public String toString() {
        return "Collections Management class.";
    }
}
