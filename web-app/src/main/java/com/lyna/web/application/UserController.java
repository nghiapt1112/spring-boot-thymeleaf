package com.lyna.web.application;

import com.lyna.commons.infrustructure.controller.AbstractCustomController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController extends AbstractCustomController {

    @GetMapping
//    @PreAuthorize("hasRole('ADMIN')")
    public String getUserByEmail(Model model, @RequestParam String param) {
        model.addAttribute("appName", param.concat(UUID.randomUUID().toString()));
        return "/home";
    }


}
