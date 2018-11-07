package com.lyna.lyna.application;

import com.nghia.libraries.commons.mss.infrustructure.controller.AbstractCustomController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.UUID;

@Controller
@RequestMapping("/user/")
public class UserController extends AbstractCustomController {

    @GetMapping
    public String getUserByEmail(Model model, @RequestParam String param) {
        model.addAttribute("appName", param.concat(UUID.randomUUID().toString()));
        return "/home";
    }


}
