package com.demo.sgs.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

public class GetConnection {
    private static final String BOOTSTRAP_VM = "http://localhost:8080";
    static RestTemplate restTemplate = new RestTemplate();

    public GetConnection(){}

    public static String getConnection() throws InterruptedException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        HttpEntity<String> requestEntity = new HttpEntity<>("http://localhost:8000", headers);
        String appBUrl = BOOTSTRAP_VM + "/getConnection";
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                appBUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        TimeUnit.MILLISECONDS.sleep(100);
        return responseEntity.getBody();
    }

}
