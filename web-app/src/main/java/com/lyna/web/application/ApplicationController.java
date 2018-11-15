package com.lyna.web.application;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ApplicationController {
    @RequestMapping("/login")
    public String login() {
        return "login/login";
    }

    @RequestMapping({ "/index", "/" })
    public String index() {
        return "index";
    }


    @RequestMapping({ "/invalidSession"})
    public String invalidSession() {
        return "invalidSession";
    }

    @RequestMapping({ "/layout", "/" })
    public String layout() {
        return "layout";
    }


    @RequestMapping({ "/listUser", "/" })
    public String listUser() {
        return "user/listUser";
    }

}
