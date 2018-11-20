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

//    @GetMapping
////    @IsAdmin
//    public String getUserByEmail(Model model, @RequestParam String param, Principal principal) {
//        model.addAttribute("appName", param.concat(UUID.randomUUID().toString()));
//        return "/home";
//    }

    @PostMapping(value = {"/user-create", "/user-create/"})
    public void registerUser(@ModelAttribute UserRegisterAggregate userRegisterAggregate) {
//        return this.userService.registerUser(user);
        System.out.println("==================registerUser==================");
        System.out.println("==================registerUser==================");
//        return null;
    }


    @PostMapping(value = {"/create/"})
    public void registerUser1(@ModelAttribute User user) {
//        return this.userService.registerUser(user);
        System.out.println("==================registerUser1==================");
//        return null;
    }


    @GetMapping(value = {"/user-create", "/user-create/"})
    public String userPage(Model model) {
        UserRegisterAggregate userRegisterAggregate = new UserRegisterAggregate();

        model.addAttribute("userRegisterAggregate", userRegisterAggregate);
        model.addAttribute("userPerRoles", userRegisterAggregate.defaultRolePerStores());

        return "user/user-create";
    }

}
