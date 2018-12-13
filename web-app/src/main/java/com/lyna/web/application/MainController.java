package com.lyna.web.application;

import com.lyna.commons.infrustructure.controller.AbstractCustomController;
import com.lyna.web.domain.logicstics.service.LogisticService;
import com.lyna.web.domain.order.service.OrderService;
import com.lyna.web.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;


@Controller
public class MainController extends AbstractCustomController {

    private static final String REDIRECT_TO_MAIN_PAGE = "";
    @Autowired
    private LogisticService logisticService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/mainScreen")
    public String mainScreen(UsernamePasswordAuthenticationToken principal, Model model) {
        User currentUser = (User) principal.getPrincipal();

        model.addAttribute("logisticData", Collections.EMPTY_LIST);
        model.addAttribute("orderData", orderService.findOrderViews(currentUser.getTenantId()));
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
