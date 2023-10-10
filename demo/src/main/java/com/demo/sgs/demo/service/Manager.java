package com.demo.sgs.demo.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.demo.sgs.demo.config.Database;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Manager {
    String managerName;
    Map<Integer, String> managers, teachers, students;
    Map<String, String> courses;

    public Manager() throws IOException, InterruptedException {
        readManagers();
        readTeachers();
        readStudents();
        readCourses();
    }

    public void managerInfo(String managerName) {
        this.managerName = managerName;
    }

    private void readCourses() throws IOException, InterruptedException {
        courses = new HashMap<>();
        List<Map<String, String>> courses = Database.getQuery(
                "education.course.select(\"status:1\")"
        );
        if (courses == null)
            return;
        for (Map<String, String> course : courses) {
            String courseId = course.get("courseID");
            String courseName = course.get("courseName");
            this.courses.put(courseId, courseName);
        }
    }

    private void readManagers() throws IOException, InterruptedException {
        managers = new HashMap<>();
        List<Map<String, String>> managers = Database.getQuery("education.manager.selectAll()");

        if (managers == null)
            return;
        for (Map<String, String> manager : managers) {
            int managerID = Integer.parseInt(manager.get("managerID"));
            this.managers.put(managerID, manager.get("name"));
            if (managerID > User.maxManagerID)
                User.maxManagerID = managerID;
        }
    }

    private void readTeachers() throws IOException, InterruptedException {
        teachers = new HashMap<>();
        List<Map<String, String>> teachers = Database.getQuery("education.teacher.selectAll()");

        if (teachers == null)
            return;
        for (Map<String, String> teacher : teachers) {
            int teacherID = Integer.parseInt(teacher.get("teacherID"));
            this.teachers.put(teacherID, teacher.get("name"));
            if (teacherID > User.maxTeacherID)
                User.maxTeacherID = teacherID;
        }
    }

    private void readStudents() throws IOException, InterruptedException {
        this.students = new HashMap<>();
        List<Map<String, String>> studentData = Database.getQuery("education.student.selectAll()");

        if (studentData == null)
            return;
        for (Map<String, String> student : studentData) {
            int studentID = Integer.parseInt(student.get("studentID"));
            this.students.put(studentID, student.get("name"));
            if (studentID > User.maxStudentID)
                User.maxStudentID = studentID;
        }
    }

    public String addUser(String role, String name, String password) throws IOException, InterruptedException {
        String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        int userID;
        if (role.equalsIgnoreCase("student")) {
            userID = User.generateStudentID();
            Database.getQuery("education.student.insert(\"name:" + name +
                    ",studentID:" + userID + ",password:" + hashedPassword + ",status:1\")");
            students.put(userID, name);
        } else if (role.equalsIgnoreCase("teacher")) {
            userID = User.generateTeacherID();
            Database.getQuery("education.teacher.insert(\"name:" + name +
                    ",teacherID:" + userID + ",password:" + hashedPassword + ",status:1\")");
            teachers.put(userID, name);
        } else if (role.equalsIgnoreCase("manager")) {
            userID = User.generateManagerID();
            Database.getQuery("education.manager.insert(\"name:" + name +
                    ",managerID:" + userID + ",password:" + hashedPassword + ",status:1\")");
            managers.put(userID, name);
        } else
            return "The role is not one of choices.";
        return "The userID for new " + role + " is: " + userID;
    }

    public String enrollStudent(int studentID, String courseID) throws IOException, InterruptedException {
        if (!students.containsKey(studentID))
            throw new IllegalArgumentException("No student has this ID");
        if (!courses.containsKey(courseID))
            throw new IllegalArgumentException("No course has this ID");

        List<Map<String, String>> enrollmentData = Database.getQuery(
                "education.enrollment.select(\"courseID:" + courseID + ",studentID:" + studentID + ",status:1\")"
        );

        if (enrollmentData != null)
            if (!enrollmentData.isEmpty())
                return "studentID: " + studentID + " is already enrolled.";

        List<Map<String, String>> courseInfo = Database.getQuery(
                "education.course.select(\"courseID:" + courseID + ",status:1\")"
        );

        if (courseInfo == null || courseInfo.isEmpty())
            return "this course ID is not correct.";

        Database.getQuery("education.enrollment.insert(\"" +
                "studentID:" + studentID +
                ",studentName:" + students.get(studentID) +
                ",courseID:" + courseID +
                ",courseName:" + courseInfo.get(0).get("courseName") +
                ",teacherName:" + courseInfo.get(0).get("teacherName") +
                ",grade:" + "0" +
                ",status:" + "1" + "\")");

        return studentID + " enrolled successfully in course " + courseID;
    }

    public String addCourse(String courseID, String courseName, int teacherID) throws IOException, InterruptedException {
        if (courses.containsKey(courseID))
            return "Course ID: " + courseID + " is already used.";
        Database.getQuery("education.course.insert(\"" +
                "courseID:" + courseID + ",courseName:" + courseName +
                ",teacherID:" + teacherID + ",teacherName:" + teachers.get(teacherID) + ",status:1\"");
        courses.put(courseID, courseName);
        return "Course " + courseName + " course ID: " + courseID + " added successfully.";
    }

    public String getName() {
        return managerName;
    }

    public Map<Integer, String> getStudents() {
        return students;
    }

    public Map<Integer, String> getTeachers() {
        return teachers;
    }

    public Map<String, String> getCourses() {
        return courses;
    }
}
