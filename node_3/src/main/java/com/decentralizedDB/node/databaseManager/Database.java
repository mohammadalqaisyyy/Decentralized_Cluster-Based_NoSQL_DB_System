package com.decentralizedDB.node.databaseManager;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class Database implements DatabaseHandler {



    public Database(){
    }

    public String createDatabase(String databaseName) {
        Path dbDirectoryPath = Paths.get(ROOT_DIRECTORY + databaseName);
        if (Files.exists(dbDirectoryPath)) {
            return "Database " + databaseName + " already exists.";
        }
        try {
            Files.createDirectories(dbDirectoryPath);
            return "Database " + databaseName + " created successfully.";
        } catch (IOException e) {
            return "Error creating " + databaseName + " Database: " + e.getMessage();
        }
    }

    @CacheEvict(value = DOCUMENT_CACHE, key = "#databaseName + '-*'")
    public String dropDatabase(String databaseName) {
        Path databasePath = Path.of(ROOT_DIRECTORY + databaseName);
        try {
            if (Files.exists(databasePath)) {
                Files.walk(databasePath)
                        .sorted((path1, path2) -> -path1.compareTo(path2))
                        .forEach(path -> {
                            try {
                                Files.deleteIfExists(path);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });

                return "Database " + databaseName + " deleted successfully.";
            } else {
                return "Database " + databaseName + " does not exist.";
            }
        } catch (IOException e) {
            return "Error deleting " + databaseName + " Database: " + e.getMessage();
        }
    }

    @Override
    public String toString() {
        return "Databases Management class.";
    }
}
