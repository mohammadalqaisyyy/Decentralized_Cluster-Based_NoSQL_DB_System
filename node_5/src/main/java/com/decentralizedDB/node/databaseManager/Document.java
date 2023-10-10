package com.decentralizedDB.node.databaseManager;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class Document {
    private final DocumentDAO documentDAO;

    public Document(DocumentDAO documentDAO) {
        this.documentDAO = documentDAO;
    }

    public String insert(String database, String collection, Map<String, String> values) {
        String DocumentKey = UUID.randomUUID().toString();
        documentDAO.updateIndexDir(database, collection);
        return documentDAO.insertDocument(database, collection, DocumentKey, values);
    }

    public String delete(String database, String collection, Map<String, String> findValues) throws IOException {
        documentDAO.updateIndexDir(database, collection);
        List<String> documentKeys = documentDAO.findDocument(database, collection, findValues);
        assert documentKeys != null;
        for (String documentKey : documentKeys)
            documentDAO.deleteDocument(database, collection, documentKey);
        return "No files have this values";
    }

    public String update(String database, String collection, Map<String, String> findValues, Map<String, String> newValues) throws IOException {
        documentDAO.updateIndexDir(database, collection);
        List<String> documentKeys = documentDAO.findDocument(database, collection, findValues);
        assert documentKeys != null;
        for (String documentKey : documentKeys) {
            if(!documentDAO.updateDocument(database,collection,documentKey,newValues))
                return "Documents updated failed.";
        }
        return "Documents updated successfully.";
    }

    public ArrayList<Map<String, String>> select(String database, String collection, Map<String, String> findValues) throws IOException {
        documentDAO.updateIndexDir(database, collection);
        List<String> documentKeys = documentDAO.findDocument(database, collection, findValues);
        assert documentKeys != null;
        ArrayList<Map<String, String>> data = new ArrayList<>();
        for (String documentKey : documentKeys) {
            data.add(documentDAO.selectDocument(database, collection, documentKey));
        }
        return data;
    }

    @Override
    public String toString() {
        return "Documents Management class.";
    }
}
