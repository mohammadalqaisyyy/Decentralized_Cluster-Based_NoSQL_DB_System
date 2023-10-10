package com.demo.sgs.demo.service;

import com.demo.sgs.demo.config.Database;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@Service
public class Student {
    int ID;

    public Student() {
    }

    public void studentInfo(int ID) {
        this.ID = ID;
    }

    public StringBuilder showCourses() throws IOException, InterruptedException {
        StringBuilder coursesStr = new StringBuilder();
        ArrayList<Map<String, String>> myCourses = Database.getQuery(
                "education.enrollment.select(\"studentID:" + ID + ",status:1\")"
        );
        if(myCourses==null)
            return null;
        for (Map<String, String> courseMap : myCourses) {
            String teacherName = courseMap.get("teacherName");
            String courseName = courseMap.get("courseName");
            String grade = "0";
            if (courseMap.containsKey("grade"))
                grade = courseMap.get("grade");
            coursesStr.append("Course: ").append(courseName)
                    .append(" - Teacher: ").append(teacherName)
                    .append(" - Grade: ").append(grade).append("\n");
        }
        return coursesStr;
    }
}
