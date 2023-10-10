package com.demo.sgs.demo.controller;

import com.demo.sgs.demo.service.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

@Controller
@RequestMapping("/student")
public class StudentController {

    final
    Student student;
    ArrayList<String> menu;

    public StudentController(Student student) {
        this.student = student;
        menu = new ArrayList<>();
        menu.add("view");
        menu.add("logout");
    }

    @GetMapping
    public String getCheck(Model model, @RequestParam("id") String id) {
        if (id == null)
            return "error";

        model.addAttribute("name", id);
        model.addAttribute("type", "student");
        model.addAttribute("menu", menu);

        student.studentInfo(Integer.parseInt(id));

        try {
            StringBuilder courses = student.showCourses();
            if (courses == null || courses.isEmpty())
                model.addAttribute("courses", "you don't have courses");
            else
                model.addAttribute("courses", courses.toString());
        } catch (Exception e) {
            model.addAttribute("courses", "Error");
        }

        return "studentDashboard";
    }
}
