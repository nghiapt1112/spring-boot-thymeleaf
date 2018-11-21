package com.lyna.web.application;

import com.lyna.commons.infrustructure.controller.AbstractCustomController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController extends AbstractCustomController {

    @RequestMapping("/mainScreen")
    public String mainScreen() {
        return "main/mainMenu";
    }
}
