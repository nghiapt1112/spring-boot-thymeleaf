package com.lyna.web.application;

import com.lyna.commons.infrustructure.controller.AbstractCustomController;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.stores.service.StoreService;
import com.lyna.web.domain.user.User;
import com.lyna.web.domain.user.UserRegisterAggregate;
import com.lyna.web.domain.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController extends AbstractCustomController {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private StoreService storeService;

    @PostMapping(value = {"/register", "/register/"})
    public String registerUser(@ModelAttribute @Valid UserRegisterAggregate userRegisterAggregate, UsernamePasswordAuthenticationToken principal) {
        User currentUser = (User) principal.getPrincipal();
        this.userService.registerUser(currentUser, userRegisterAggregate);
        return "user/user-create";
    }


    @GetMapping(value = {"/register", "/register/"})
    public String userPage(Model model, UsernamePasswordAuthenticationToken principal) {
        User currentUser = (User) principal.getPrincipal();
        UserRegisterAggregate userRegisterAggregate = new UserRegisterAggregate();


//        model.addAttribute("userInput", userRegisterAggregate);
        model.addAttribute("userRegisterAggregate", userRegisterAggregate);
        //TODO: =>NghiaPT put it to ModelAttributes (Using in MVC- check thymeleaf for convention)
        List<Store> stores = storeService.findAll(currentUser.getTenantId());

        //TODO: => nghiapt move to Object
        model.addAttribute("userPerRoles", UserRegisterAggregate.toUserStoreAuthorityAggregate(stores));

        return "user/user-create";
    }

}
