package com.lyna.web.application;

import com.lyna.commons.infrustructure.controller.AbstractCustomController;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.stores.service.StoreService;
import com.lyna.web.domain.user.User;
import com.lyna.web.domain.user.UserList;
import com.lyna.web.domain.user.UserRegisterAggregate;
import com.lyna.web.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/user")
public class UserController extends AbstractCustomController {

    //ToDo: cho vào file config nhé
    private static int currentPage = 1;
    private static int pageSize = 5;

    @Autowired
    private UserService userService;
    @Autowired
    private StoreService storeService;

    @GetMapping
//    @IsAdmin
    public String getUserByEmail(Model model, @RequestParam String param, Principal principal) {
        model.addAttribute("appName", param.concat(UUID.randomUUID().toString()));
        userService.registerUser((User) principal, new UserRegisterAggregate());
        return "/home";
    }

    @PostMapping(name = "/")
    public User registerUser(@ModelAttribute User user) {
//        return this.userService.registerUser(user);
        return null;
    }


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listUsers(
            Model model,
            Principal principal,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        page.ifPresent(p -> currentPage = p);
        size.ifPresent(s -> pageSize = s);

        List<Store> storeListAll = storeService.getStoreList((User)principal);

        Page<UserList> userPage =
                userService.findPaginated(PageRequest.of(currentPage - 1, pageSize), storeListAll);


        model.addAttribute("userPage", userPage);
        model.addAttribute("storeModel", storeListAll);

        int totalPages = userPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "user/listUser";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(HttpServletRequest request, ModelMap modelMap) {
        try {
            for (String userid : request.getParameterValues("userid")) {

            }
        } catch (Exception ex) {

        }
        return "redirect:user/listUser";
    }
}
