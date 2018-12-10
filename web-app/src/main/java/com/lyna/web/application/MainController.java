package com.lyna.web.application;

import com.lyna.commons.infrustructure.controller.AbstractCustomController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController extends AbstractCustomController {

    @GetMapping("/mainScreen")
    public String mainScreen() {
        return "main/mainMenu";
    }
}
