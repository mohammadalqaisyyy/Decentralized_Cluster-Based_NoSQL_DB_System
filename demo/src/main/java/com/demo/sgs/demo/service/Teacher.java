package com.demo.sgs.demo.service;

import com.demo.sgs.demo.config.Database;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Teacher {

    public String getName(int teacherID) throws IOException, InterruptedException {
        List<Map<String, String>> myInfo = Database.getQuery(
                "education.course.select(\"teacherID:" + teacherID + ",status:1\")"
        );
        assert myInfo != null;
        if (myInfo.isEmpty())
            return null;
        return myInfo.get(0).get("teacherName");
    }

    public Map<String, String> getMyCourses(int teacherID) throws IOException, InterruptedException {
        Map<String, String> myCourses = new HashMap<>();
        List<Map<String, String>> courses = Database.getQuery(
                "education.course.select(\"teacherID:" + teacherID + "\")"
        );
        assert courses != null;
        for (Map<String, String> course : courses) {
            String courseID = course.get("courseID");
            String courseName = course.get("courseName");
            myCourses.put(courseID, courseName);
        }
        return myCourses;
    }

    public StringBuilder classList(int teacherID, String courseID) throws IOException, InterruptedException {
        List<Map<String, String>> classList = Database.getQuery(
                "education.enrollment.select(\"courseID:" + courseID + ",teacherID:" + teacherID + "\")"
        );
        StringBuilder classStr = new StringBuilder();
        assert classList != null;
        for (Map<String, String> _class : classList) {
            if (_class.containsValue(courseID)) {
                String studentID = _class.get("studentID");
                String studentName = _class.get("studentName");
                String grade = "0";
                if (_class.containsKey("grade"))
                    grade = _class.get("grade");
                classStr.append(studentID).append(" : ").append(studentName).append(" : ").append(grade).append("\n");
            }
        }
        return classStr;
    }

    public String editGrade(int teacherID, String courseID, int studentID, int newGrade) throws IOException, InterruptedException {
        if(!getMyCourses(teacherID).containsKey(courseID))
            return "Wrong course ID.";
        Database.getQuery(
                "education.enrollment.update(\"teacherID:" + teacherID +
                        ",courseID:" + courseID +
                        ",studentID:" + studentID +
                        "\",\"" + "grade:" + newGrade + "\")"
        );
        return "Grades updated successfully.";
    }

    public String addGrades(int teacherID, String courseID, Map<Integer, Integer> studentsGrades) throws IOException, InterruptedException {
        if(!getMyCourses(teacherID).containsKey(courseID))
            return "Wrong course ID.";
        for (Map.Entry<Integer, Integer> studentGrade : studentsGrades.entrySet()) {
            int newGrade = studentCourseGrade(courseID, studentGrade.getKey()) + studentGrade.getValue();
            Database.getQuery(
                    "education.enrollment.update(\"teacherID:" + teacherID +
                            ",courseID:" + courseID +
                            ",studentID:" + studentGrade.getKey() +
                            "\",\"" + "grade:" + newGrade + "\")"
            );
        }
        return "Grades added successfully.";
    }

    private int studentCourseGrade(String courseID, Integer studentID) throws IOException, InterruptedException {
        ArrayList<Map<String, String>> studentCourseInfo = Database.getQuery(
                "education.enrollment.select(\"studentID:" + studentID +
                        ",courseID:" + courseID + ",status:1\")"
        );
        String grade = "0";
        assert studentCourseInfo != null;
        for (Map<String, String> courseMap : studentCourseInfo)
            if (courseMap.containsKey("grade"))
                grade = courseMap.get("grade");

        return Integer.parseInt(grade);
    }


}