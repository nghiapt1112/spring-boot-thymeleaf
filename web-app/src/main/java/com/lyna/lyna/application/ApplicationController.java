package com.lyna.lyna.application;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ApplicationController {
    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping({ "/index", "/" })
    public String index() {
        return "index";
    }


    @RequestMapping({ "/invalidSession"})
    public String invalidSession() {
        return "invalidSession";
    }



}
