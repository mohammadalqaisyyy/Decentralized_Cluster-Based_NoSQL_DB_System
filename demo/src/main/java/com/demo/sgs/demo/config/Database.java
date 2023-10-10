package com.demo.sgs.demo.config;

import com.demo.sgs.demo.model.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Database {
    private static String vmConnection;
    private static String token = "";

    public Database() throws IOException, InterruptedException {
        vmConnection = GetConnection.getConnection();
        startConnection();
    }

    public void startConnection() throws IOException, InterruptedException {
        String serverUrl = "http://localhost:" + vmConnection.split(":")[2] + "/login";
        System.out.println(serverUrl);
        HttpClient httpClient = HttpClient.newHttpClient();
        LoginRequest loginRequest = new LoginRequest("admin", "admin");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(loginRequestToJson(loginRequest)))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        int statusCode = response.statusCode();
        if (statusCode == 200) {
            token = response.body();
            System.out.println("status code: " + statusCode + " - token: " + token);
        } else
            System.err.println("Token retrieval failed. HTTP Status Code: " + statusCode);
    }

    public static ArrayList<Map<String, String>> getQuery(String query) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        String apiUrl = "http://localhost:" + vmConnection.split(":")[2] + "/client";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(query))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        TimeUnit.MILLISECONDS.sleep(100);

        if (response.statusCode() != 200)
            System.out.println("Error, HTTP Status Code: " + response.statusCode());

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ArrayList myResponse = objectMapper.readValue(response.body(), ArrayList.class);
            return (ArrayList<Map<String, String>>) myResponse.clone();
        } catch (Exception e) {
            return null;
        }
    }

    private String loginRequestToJson(LoginRequest loginRequest) {
        return "{\"username\":\"" + loginRequest.username() + "\",\"password\":\"" + loginRequest.password() + "\"}";
    }
}
