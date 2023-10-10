package com.decentralizedDB.node.commands;

import com.decentralizedDB.node.databaseManager.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
class DocumentExecutor extends ExecuteHandler {

    private static List<String> mainParts;
    private static String[] valueParts;
    private static Map<String, String> values;
    private static Document document;

    public DocumentExecutor(Document document) {
        DocumentExecutor.document = document;
    }

    public static String executeQuery(String documentQuery) throws IOException {
        mainParts = extractComponents(documentQuery);
        valueParts = documentQuery.split("\"");
        values = extractKeyValuePairs(valueParts[1]);
        if (Objects.equals(mainParts.get(2), "insert"))
            return document.insert(mainParts.get(0), mainParts.get(1), values);
        if (Objects.equals(mainParts.get(2), "delete"))
            return document.delete(mainParts.get(0), mainParts.get(1), values);
        if (Objects.equals(mainParts.get(2), "update")) {
            if (valueParts.length != 5)
                throw new IllegalArgumentException("Error query syntax.");
            Map<String, String> secondValues = extractKeyValuePairs(valueParts[3]);
            return document.update(mainParts.get(0), mainParts.get(1), values, secondValues);
        }
        return "";
    }

    public static ArrayList<Map<String, String>> selectQuery(String selectQuery) throws IOException {
        mainParts = extractComponents(selectQuery);
        valueParts = selectQuery.split("\"");
        if (Objects.equals(mainParts.get(2), "selectAll"))
            return document.select(mainParts.get(0), mainParts.get(1), null);
        values = extractKeyValuePairs(valueParts[1]);
        if (Objects.equals(mainParts.get(2), "select"))
            return document.select(mainParts.get(0), mainParts.get(1), values);
        return null;
    }

    public static List<String> extractComponents(String query) {
        return List.of(query.split("\\(")[0].split("\\."));
    }

    private static Map<String, String> extractKeyValuePairs(String query) {
        Map<String, String> keyValueMap = new HashMap<>();
        Pattern pattern = Pattern.compile("(\\w+)\\s*:\\s*\"?([^\",]+)\"?");
        Matcher matcher = pattern.matcher(query);
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2).trim();
            keyValueMap.put(key, value);
        }
        return keyValueMap;
    }

    @Override
    public String toString() {
        return "Document Executor class.";
    }
}
