package com.demo.sgs.demo.controller;

import com.demo.sgs.demo.service.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@RequestMapping("/login")
public class LoginController {

    final
    User user;

    public LoginController(User user) {
        this.user = user;
    }

    @GetMapping
    public String getLogin(Model model) {
        model.addAttribute("message", "welcome");
        return "login";
    }

    @PostMapping
    public String processForm(Model model, @RequestParam("name") String name, @RequestParam("password") String password) throws IOException, InterruptedException {
        int userType;

        userType = user.checkUser(Integer.parseInt(name), password);
        if (userType == 3) {
            model.addAttribute("errorMessage", "Invalid Credentials!!");
            return "login";
        }
        System.out.println(User.userTypes[userType] + " " + name + " - login");

        switch (User.userTypes[userType]) {
            case "Manager" -> {
                return "redirect:/manager?id=" + name;
            }
            case "Teacher" -> {
                return "redirect:/teacher?id=" + name;
            }
            case "Student" -> {
                return "redirect:/student?id=" + name;
            }
        }
        return "login";
    }
}
