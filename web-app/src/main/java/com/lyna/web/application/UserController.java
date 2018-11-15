package com.lyna.web.application;

import com.lyna.commons.infrustructure.controller.AbstractCustomController;
import com.lyna.web.domain.user.User;
import com.lyna.web.domain.user.service.UserService;
import com.lyna.web.security.authorities.IsAdmin;
import com.lyna.web.security.authorities.IsEditAble;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.validation.Valid;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController extends AbstractCustomController {

    @Autowired
    private UserService userService;

    @GetMapping
    @IsAdmin
    public String getUserByEmail(Model model, @RequestParam String param) {
        model.addAttribute("appName", param.concat(UUID.randomUUID().toString()));
        return "/home";
    }

    @PostMapping(name = "/")
    public User registerUser(@ModelAttribute @Valid User user) {
//        return this.userService.registerUser(user);
        return null;
    }
}
