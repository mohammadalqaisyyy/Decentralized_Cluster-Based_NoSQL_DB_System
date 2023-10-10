package com.demo.sgs.demo.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.demo.sgs.demo.config.Database;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class User {

    public static int maxStudentID = 2010000000, maxTeacherID = 10000, maxManagerID = 0;

    public static final String[] userTypes = {"Manager", "Teacher", "Student", "Not User"};

    public static int generateManagerID() {
        maxManagerID += 1;
        return maxManagerID;
    }

    public static int generateTeacherID() {
        maxTeacherID += 1;
        return maxTeacherID;
    }

    public static int generateStudentID() {
        maxStudentID += 1;
        return maxStudentID;
    }

    public int checkUser(int ID, String password) throws IOException, InterruptedException {
        List<Map<String, String>> users;
        int role;
        if (ID <= maxManagerID) {
            users = Database.getQuery("education.manager.select(\"managerID:" + ID + ",status:1\")");
            role = 0;
        } else if (ID <= maxTeacherID) {
            users = Database.getQuery("education.teacher.select(\"teacherID:" + ID + ",status:1\")");
            role = 1;
        } else {
            users = Database.getQuery("education.student.select(\"studentID:" + ID + ",status:1\")");
            role = 2;
        }

        if (users == null || users.isEmpty())
            return 3;
        boolean passwordVerified = BCrypt.verifyer().verify(password.toCharArray(), (users.get(0).get("password"))).verified;
        if (passwordVerified)
            return role;
        return 3;
    }
}
