package com.lyna.web.application;

import com.lyna.web.domain.user.User;
import com.lyna.web.domain.user.UserRegisterAggregate;
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

    @RequestMapping({"/index"})
    public String index() {
        return "index";
    }


    @RequestMapping({"/invalidSession"})
    public String invalidSession() {
        return "invalidSession";
    }



    @RequestMapping({"/layout", "/"})
    public String layout() {
        return "layout";
    }
}
