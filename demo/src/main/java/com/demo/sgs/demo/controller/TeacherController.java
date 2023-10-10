package com.demo.sgs.demo.controller;

import com.demo.sgs.demo.service.Teacher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/teacher")
public class TeacherController {
    Teacher teacher;
    ArrayList<String> menu, out;
    int id;

    public TeacherController(Teacher teacher) {
        this.teacher = teacher;
        menu = new ArrayList<>();
        out = new ArrayList<>();
        menu.add("My courses");
        menu.add("List of student");
        menu.add("Add marks");
        menu.add("Edit mark");
        menu.add("Logout");
    }

    @GetMapping
    public String getDashboard(Model model, @RequestParam("id") int id) {
        out.clear();
        out.add("Empty");
        this.id = id;
        model.addAttribute("name", id);
        model.addAttribute("type", "teacher");
        model.addAttribute("sideList", out);
        model.addAttribute("menu", menu);
        return "teacherDashboard";
    }

    @PostMapping
    public String processForm(HttpServletRequest request) throws IOException, InterruptedException {
        out.clear();
        request.setAttribute("type", "teacher");
        request.setAttribute("name", id);
        request.setAttribute("sideList", out);
        request.setAttribute("menu", menu);
        String choice = request.getParameter("number");
        switch (choice) {
            case "1" -> {
                out.add("Course ID, Course name");
                for (Map.Entry<String, String> entry : teacher.getMyCourses(id).entrySet()) {
                    String s = entry.getKey() + ", " + entry.getValue();
                    out.add(s);
                }
            }
            case "2" -> {
                try {
                    String courseID = request.getParameter("courseID");
                    out.add(teacher.classList(id, courseID).toString());
                } catch (Exception e) {
                    out.add("Error");
                }
            }
            case "3" -> {
                try {
                    String courseID = request.getParameter("courseID");
                    String[] marks = request.getParameter("marks").split(",");
                    Map<Integer, Integer> studentsGrades = new HashMap<>();
                    for (String mark : marks) {
                        String[] sG = mark.split(":");
                        studentsGrades.put(Integer.parseInt(sG[0]), Integer.parseInt(sG[1]));
                    }
                    out.add(teacher.addGrades(id, courseID, studentsGrades));
                } catch (Exception e) {
                    out.add("Error");
                }
            }
            case "4" -> {
                try {
                    String courseID = request.getParameter("courseID");
                    int studentID = Integer.parseInt(request.getParameter("studentID"));
                    int newGrade = Integer.parseInt(request.getParameter("newGrade"));
                    out.add(teacher.editGrade(id, courseID, studentID, newGrade));
                } catch (Exception e) {
                    out.add("Error");
                }
            }
        }
        System.out.println(out);
        request.setAttribute("sideList", out);
        return "teacherDashboard";
    }
}
