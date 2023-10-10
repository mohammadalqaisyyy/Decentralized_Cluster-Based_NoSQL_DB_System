package com.decentralizedDB.node.databaseManager;

import com.decentralizedDB.node.databaseManager.indexing.DocumentIndexing;
import com.decentralizedDB.node.databaseManager.indexing.FilesFinder;
import com.decentralizedDB.node.databaseManager.indexing.IndexingRemover;
import com.decentralizedDB.node.databaseManager.indexing.IndexingUpdater;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class DocumentDAO implements DatabaseHandler {
    private static String IndexPath;

    public DocumentDAO() {
    }

    public String insertDocument(String database, String collection, String documentKey, Map<String, String> values) {
        Path jsonFilePath = Paths.get(ROOT_DIRECTORY + database + "/" + collection + "/" + documentKey + ".json");
        try {
            ObjectNode newObject = objectMapper.createObjectNode();
            for (Map.Entry<String, String> entry : values.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                newObject.put(key, value);
                DocumentIndexing.addIndex(IndexPath + key + ".json", value, documentKey);
            }
            Files.write(jsonFilePath, objectMapper.writeValueAsBytes(newObject));
            return "Document insertion successfully.";
        } catch (IOException e) {
            return "Document insertion failed.";
        }
    }

    @CacheEvict(value = DOCUMENT_CACHE, key = "#database + '-' + #collection + '-' + #documentKey")
    public void deleteDocument(String database, String collection, String documentKey) throws IOException {
        Path jsonFilePath = Paths.get(ROOT_DIRECTORY + database + "/" + collection + "/" + documentKey + ".json");
        Files.deleteIfExists(jsonFilePath);
        IndexingRemover.removeIndex(IndexPath, documentKey);
    }

    @CacheEvict(value = DOCUMENT_CACHE, key = "#database + '-' + #collection + '-' + #documentKey")
    public boolean updateDocument(String database, String collection, String documentKey, Map<String, String> newValues) throws IOException {
        Path jsonFilePath = Path.of(ROOT_DIRECTORY + database + "/" + collection + "/" + documentKey + ".json");
        try {
            String jsonContent = Files.readString(jsonFilePath);
            JsonNode rootNode = objectMapper.readTree(jsonContent);
            if (rootNode.isObject()) {
                ObjectNode objectNode = (ObjectNode) rootNode;

                for (Map.Entry<String, String> entry : newValues.entrySet())
                    objectNode.put(entry.getKey(), entry.getValue());

                Files.write(jsonFilePath, objectMapper.writeValueAsBytes(objectNode));
            }
        } catch (IOException e) {
            return false;
        }
        IndexingUpdater.updateIndex(IndexPath, documentKey, newValues);
        return true;
    }

    @Cacheable(value = DOCUMENT_CACHE, key = "#database + '-' + #collection + '-' + #documentKey")
    public Map<String, String> selectDocument(String database, String collection, String documentKey) throws IOException {
        Map<String, String> fieldsValues = new LinkedHashMap<>();
        String directoryPath = ROOT_DIRECTORY + database + "/" + collection + "/";
        String documentName = documentKey + ".json";
        File file = new File(directoryPath, documentName);
        if (file.exists()) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(file);
            Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                String key = entry.getKey();
                JsonNode valueNode = entry.getValue();
                String value = valueNode.isTextual() ? valueNode.textValue() : valueNode.toString();
                fieldsValues.put(key, value);
            }
        }
        return fieldsValues;
    }

    List<String> findDocument(String database, String collection, Map<String, String> values) throws IOException {
        updateIndexDir(database, collection);
        List<String> files;
        String dir = ROOT_DIRECTORY + database + "/" + collection + "/index/";
        FilesFinder filesFind = new FilesFinder();
        files = filesFind.findFiles(dir, values);
        return files;
    }

    void updateIndexDir(String database, String collection) {
        if (database == null || collection == null)
            throw new IllegalArgumentException("Error selecting.");
        IndexPath = ROOT_DIRECTORY + database + "/" + collection + "/index/";
    }

    @Override
    public String toString() {
        return "DocumentDAO class.";
    }
}
