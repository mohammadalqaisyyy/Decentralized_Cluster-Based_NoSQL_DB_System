package com.decentralizedDB.node.commands;

import com.decentralizedDB.node.databaseManager.Collection;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
class CollectionExecutor extends ExecuteHandler {

    private static Collection collection;

    private CollectionExecutor(Collection collection) {
        CollectionExecutor.collection = collection;
    }

    public static String executeQuery(String collectionQuery) {
        List<String> queryParts = extractValues(collectionQuery);
        if (isWrongName(queryParts.get(2)) && queryParts.size() != 3)
            throw new IllegalArgumentException("Error wrong collection name.");
        if (queryParts.get(1).equals("createCollection"))
            return collection.createCollection(queryParts.get(0), queryParts.get(2));
        if (queryParts.get(1).equals("dropCollection"))
            return collection.dropCollection(queryParts.get(0), queryParts.get(2));
        throw new IllegalArgumentException("Error wrong collection query.");
    }

    public static List<String> extractValues(String input) {
        List<String> extractedValues = new ArrayList<>();
        Pattern pattern = Pattern.compile("[A-Za-z0-9_]+|\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            String match = matcher.group(1);
            if (match != null) {
                extractedValues.add(match);
            } else
                extractedValues.add(matcher.group());
        }
        return extractedValues;
    }
}
