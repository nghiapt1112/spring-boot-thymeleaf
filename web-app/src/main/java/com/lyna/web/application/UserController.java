package com.lyna.web.application;

import com.lyna.commons.infrustructure.controller.AbstractCustomController;
import com.lyna.web.domain.user.User;
import com.lyna.web.domain.user.UserRegisterAggregate;
import com.lyna.web.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController extends AbstractCustomController {
    @Autowired
    private UserService userService;

    @GetMapping
//    @IsAdmin
    public String getUserByEmail(Model model, @RequestParam String param, Principal principal) {
        model.addAttribute("appName", param.concat(UUID.randomUUID().toString()));
        userService.registerUser((User) principal, new UserRegisterAggregate());
        return "/home";
    }

    @PostMapping(name = "/")
    public User registerUser(@ModelAttribute User user) {
//        return this.userService.registerUser(user);
        return null;
    }
}
