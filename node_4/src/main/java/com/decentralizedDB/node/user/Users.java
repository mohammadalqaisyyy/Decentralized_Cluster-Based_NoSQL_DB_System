package com.decentralizedDB.node.user;

import com.decentralizedDB.node.databaseManager.DatabaseHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Users {
    private static final String jsonFilePath = DatabaseHandler.ROOT_DIRECTORY + "users.json";
    private static int usersSize;

    public static Map<String, UserDetails> userDetailsService() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = Users.class.getClassLoader().getResourceAsStream("DB/users.json");

        if (inputStream == null) {
            throw new FileNotFoundException("DB/users.json not found on the classpath.");
        }
        TypeReference<Map<String, String>> typeReference = new TypeReference<>() {};
        Map<String, String> userData = objectMapper.readValue(inputStream, typeReference);

        Map<String, UserDetails> userDetailsMap = new HashMap<>();

        for (Map.Entry<String, String> entry : userData.entrySet()) {
            String username = entry.getKey();
            String password = entry.getValue();

            UserDetails userDetails = User.withUsername(username)
                    .password("{noop}" + password)
                    .authorities("ROLE_USER")
                    .build();

            userDetailsMap.put(username, userDetails);
        }
        usersSize = userDetailsMap.size();
        return userDetailsMap;
    }

    public static int getUsersSize() {
        return usersSize;
    }
}
