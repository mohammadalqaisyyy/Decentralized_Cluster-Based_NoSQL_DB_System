package com.demo.sgs.demo.controller;

import com.demo.sgs.demo.service.Manager;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    final
    Manager manager = new Manager();
    ArrayList<String> menu, out;

    public ManagerController() throws IOException, InterruptedException {
        menu = new ArrayList<>();
        out = new ArrayList<>();
        menu.add("Add user");
        menu.add("View teachers");
        menu.add("View students");
        menu.add("Add course");
        menu.add("View courses");
        menu.add("Enroll student in course");
        menu.add("Exit");
    }

    @GetMapping
    public String getDashboard(Model model, @RequestParam("id") String id) {
        out.clear();
        if (id == null)
            return "error";
        else {
            manager.managerInfo(id);
            out.clear();
            out.add("Empty");
            model.addAttribute("name", id);
            model.addAttribute("type", "manager");
            model.addAttribute("sideList", out);
            model.addAttribute("menu", menu);
            return "managerDashboard";
        }
    }

    @PostMapping
    public String processForm(HttpServletRequest request) {
        out.clear();
        request.setAttribute("type", "manager");
        request.setAttribute("name", manager.getName());
        request.setAttribute("sideList", out);
        request.setAttribute("menu", menu);
        String numberString = request.getParameter("number");
        switch (numberString) {
            case "1" -> {
                try {
                    String role = request.getParameter("userType");
                    String name = request.getParameter("nameUser");
                    String password = request.getParameter("password");
                    out.add(manager.addUser(role, name, password));
                } catch (Exception e) {
                    out.add("Wrong information");
                }
            }
            case "2" -> {
                try {
                    out.add("Teachers");
                    for (Map.Entry<Integer, String> entry : manager.getTeachers().entrySet())
                        out.add(entry.getKey().toString() + ". " + entry.getValue());
                } catch (Exception e) {
                    out.add("Wrong information");
                }
            }
            case "3" -> {
                try {
                    out.add("Students");
                    for (Map.Entry<Integer, String> entry : manager.getStudents().entrySet())
                        out.add(entry.getKey().toString() + ". " + entry.getValue());
                } catch (Exception e) {
                    out.add("Wrong information");
                }
            }
            case "4" -> {
                try {
                    String courseName = request.getParameter("courseName");
                    String courseID = request.getParameter("courseID");
                    int teacherID = Integer.parseInt(request.getParameter("teacherID"));
                    out.add(manager.addCourse(courseID, courseName, teacherID));
                } catch (Exception e) {
                    out.add("Wrong information");
                }
            }
            case "5" -> {
                try {
                    out.add("Courses");
                    for (Map.Entry<String, String> entry : manager.getCourses().entrySet())
                        out.add(entry.getKey() + ". " + entry.getValue());
                } catch (Exception e) {
                    out.add("Wrong information");
                }
            }
            case "6" -> {
                try {
                    int studentID = Integer.parseInt(request.getParameter("studentID"));
                    String courseID = request.getParameter("courseID");
                    out.add(manager.enrollStudent(studentID, courseID));
                } catch (Exception e) {
                    out.add("Wrong information");
                }
            }
        }
        System.out.println(out);
        request.setAttribute("sideList", out);
        return "managerDashboard";
    }
}
