package com.lyna.web.application;

import com.lyna.web.domain.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ApplicationController {
    @RequestMapping("/login")
    public String login() {
        return "login/login";
    }

    /*@RequestMapping({ "/index", "/" })
    public String index() {
        return "index";
    }*/


    @RequestMapping({ "/invalidSession"})
    public String invalidSession() {
        return "invalidSession";
    }

    @GetMapping("/registerUser")
    public String userPage(Model model) {
        model.addAttribute("user", new User());
        return "user/user-create";
    }

    @RequestMapping({ "/layout", "/" })
    public String layout() {
        return "layout";
    }
}
