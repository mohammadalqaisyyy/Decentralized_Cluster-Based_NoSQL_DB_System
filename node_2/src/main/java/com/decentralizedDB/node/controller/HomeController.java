package com.decentralizedDB.node.controller;

import com.decentralizedDB.node.commands.ExecuteHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping
public class HomeController {

    @GetMapping
    public RedirectView redirectToAnotherPage() {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("/login");
        return redirectView;
    }

    @GetMapping("/queries")
    public String queries() {
        return ExecuteHandler.queriesList.toString();
    }
}
