package com.lyna.web.application;

import com.lyna.commons.infrustructure.controller.AbstractCustomController;
import com.lyna.web.domain.logicstics.LogisticRequestPage;
import com.lyna.web.domain.logicstics.StoreRequestPage;
import com.lyna.web.domain.logicstics.StoreResponsePage;
import com.lyna.web.domain.logicstics.service.LogisticService;
import com.lyna.web.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController extends AbstractCustomController {

    @Autowired
    private LogisticService logisticService;

    @GetMapping("/mainScreen")
    public String mainScreen() {
        return "main/mainMenu";
    }

    @GetMapping(value = {"/logistics", "/logistics/"})
    public void findLogistics(UsernamePasswordAuthenticationToken principal) {
        User currentUser = (User) principal.getPrincipal();

        LogisticRequestPage requestPage = new LogisticRequestPage();
        requestPage.setTenantId(currentUser.getTenantId());
        this.logisticService.findLogisticsAndPaging(null);
    }

    @GetMapping(value = {"/order", "/order/"})
    public String findOrders(UsernamePasswordAuthenticationToken principal, Model model) {
        User currentUser = (User) principal.getPrincipal();
        StoreRequestPage requestPage = new StoreRequestPage();
        requestPage.setTenantId(currentUser.getTenantId());

        StoreResponsePage orderResponsePage = this.logisticService.findOrdersAndPaging(requestPage);
        model.addAttribute("pageData", orderResponsePage.getResults());

        return "main/mainMenu";
    }

}
