package com.demo.sgs.demo;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.demo.sgs.demo.config.Database;
import com.demo.sgs.demo.config.GetConnection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
        CompletableFuture<Void> asyncTask = CompletableFuture.runAsync(() -> {
            try {
                new Database();
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        });
        asyncTask.join();

        SpringApplication.run(DemoApplication.class, args);
        startup();
    }

    private static void startup() throws IOException, InterruptedException {
//		Database.getQuery("use education");
//
//		Database.getQuery("education.createCollection(\"manager\")");
//		Database.getQuery("education.createCollection(\"teacher\")");
//		Database.getQuery("education.createCollection(\"student\")");
//		Database.getQuery("education.createCollection(\"course\")");
//		Database.getQuery("education.createCollection(\"enrollment\")");
//
//		TimeUnit.SECONDS.sleep(1);
//
//		String hashedPassword = BCrypt.withDefaults().hashToString(12, "admin".toCharArray());
//		Database.getQuery("education.manager.insert(\"name:admin" +
//				",managerID:1,password:" + hashedPassword + ",status:1\")");
//
        System.out.println("Admin:  " + Database.getQuery("education.manager.selectAll()"));
    }

}
