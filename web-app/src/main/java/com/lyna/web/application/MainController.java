package com.lyna.web.application;

import com.lyna.commons.infrustructure.controller.AbstractCustomController;
import com.lyna.web.domain.user.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController extends AbstractCustomController {

    @GetMapping("/mainScreen")
    public String mainScreen() {
        return "main/mainMenu";
    }

    @GetMapping(value = {"/logistics", "/logistics/"})
    public void findLogistics(UsernamePasswordAuthenticationToken principal) {
        User currentUser = (User) principal.getPrincipal();


    }

}
