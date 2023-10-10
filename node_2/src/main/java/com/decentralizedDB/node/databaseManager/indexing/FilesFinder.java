package com.decentralizedDB.node.databaseManager.indexing;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FilesFinder {

    public List<String> findFiles(String directoryPath, Map<String, String> keyMap) throws IOException {
        if (keyMap == null || keyMap.isEmpty()) {
            return findAllValues(directoryPath);
        }

        List<List<String>> valueLists = new ArrayList<>();

        for (Map.Entry<String, String> entry : keyMap.entrySet()) {
            String fileName = entry.getKey() + ".json";
            File file = new File(directoryPath, fileName);

            if (file.exists()) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(file);
                JsonNode valuesNode = rootNode.get(entry.getValue());

                if (valuesNode != null && valuesNode.isArray()) {
                    List<String> values = new ArrayList<>();
                    for (JsonNode valueNode : valuesNode) {
                        values.add(valueNode.asText());
                    }
                    valueLists.add(values);
                }
            }
        }

        if (valueLists.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> commonValues = valueLists.get(0);
        for (int i = 1; i < valueLists.size(); i++) {
            commonValues.retainAll(valueLists.get(i));
        }

        return commonValues;
    }

    public List<String> findAllValues(String directoryPath) throws IOException {
        Set<String> allValues = new HashSet<>();

        File[] files = new File(directoryPath).listFiles((dir, name) -> name.endsWith(".json"));

        if (files != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            for (File file : files) {
                JsonNode rootNode = objectMapper.readTree(file);
                Iterator<JsonNode> elements = rootNode.elements();

                while (elements.hasNext()) {
                    JsonNode valuesNode = elements.next();
                    if (valuesNode.isArray()) {
                        valuesNode.elements().forEachRemaining(valueNode -> allValues.add(valueNode.asText()));
                    }
                }
            }
        }

        return new ArrayList<>(allValues);
    }
}
