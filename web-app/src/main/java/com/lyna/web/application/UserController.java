package com.lyna.web.application;

import com.lyna.commons.infrustructure.controller.AbstractCustomController;
import com.lyna.web.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.security.Principal;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController extends AbstractCustomController {

    @Autowired
    private UserService userService1;

    @Autowired
    private UserService userService;

    @GetMapping
//    @PreAuthorize("hasRole('ADMIN')")
    public String getUserByEmail(Model model, @RequestParam String param, Principal principal) {
        model.addAttribute("appName", param.concat(UUID.randomUUID().toString()));
        userService.findByEmail("nghia");
        userService1.findByEmail("nghia2");
        return "/home";
    }

}
