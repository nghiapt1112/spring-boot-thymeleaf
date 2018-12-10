package com.lyna.web.application;

import com.lyna.commons.infrustructure.controller.AbstractCustomController;
import com.lyna.web.domain.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class MainController extends AbstractCustomController {

    private static final String REDIRECT_TO_MAIN_PAGE = "";

    @Autowired
    private OrderService orderService;

    @GetMapping("/mainScreen")
    public String mainScreen() {
        return "main/mainMenu";
    }

    @GetMapping("/upload")
    public String upload() {
        return "layout";
    }

    @PostMapping(value = {"/upload/"})
    public String uploadOrder() {
        return REDIRECT_TO_MAIN_PAGE;
    }
}
