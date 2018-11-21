package com.lyna.web.application;

import com.lyna.commons.infrustructure.controller.AbstractCustomController;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.stores.service.StoreService;
import com.lyna.web.domain.user.User;
import com.lyna.web.domain.user.UserAggregate;
import com.lyna.web.domain.user.service.UserService;
import com.lyna.web.security.authorities.IsAdmin;
import com.lyna.web.domain.view.UserList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/user")
public class UserController extends AbstractCustomController {
    private static final String REDIRECT_TO_USER_LIST_PAGE = "redirect:/user/list";

    //ToDo: cho vào file config nhé
    private static int currentPage = 1;
    private static int pageSize = 5;

    @Autowired
    private UserService userService;

    @Autowired
    private StoreService storeService;

    @PostMapping(value = {"/register", "/register/"})
    @IsAdmin
    public String registerUser(@ModelAttribute @Valid UserAggregate userRegisterAggregate, UsernamePasswordAuthenticationToken principal) {
        User currentUser = (User) principal.getPrincipal();
        this.userService.registerUser(currentUser, userRegisterAggregate);
        return REDIRECT_TO_USER_LIST_PAGE;
    }


    @GetMapping(value = {"/register", "/register/"})
    @IsAdmin
    public String registerUserPage(Model model, UsernamePasswordAuthenticationToken principal) {
        User currentUser = (User) principal.getPrincipal();
        UserAggregate userRegisterAggregate = new UserAggregate();
        userRegisterAggregate.updateRolePerStore(storeService.findAll(currentUser.getTenantId()));

        model.addAttribute("userPerRoles", userRegisterAggregate.getRolePerStore());
        model.addAttribute("userRegisterAggregate", userRegisterAggregate);

        return "user/user-create";
    }

    @GetMapping(value = {"/{userId}"})
    @IsAdmin
    public String updateUserPage(Model model, UsernamePasswordAuthenticationToken principal, @PathVariable String userId) {
        User currentUser = (User) principal.getPrincipal();
        UserAggregate aggregate = new UserAggregate().fromUserEntity(userService.findById(currentUser.getTenantId(), userId));

        model.addAttribute("aggregate", aggregate);
        return "user/user-update";
    }

    @PostMapping(value = {"/update", "/update/"})
    @IsAdmin
    public String updateUser(UsernamePasswordAuthenticationToken principal, @Valid UserAggregate aggregate) {
        User currentUser = (User) principal.getPrincipal();
        userService.update(currentUser, aggregate);
        return REDIRECT_TO_USER_LIST_PAGE;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listUsers(
            Model model,
            UsernamePasswordAuthenticationToken principal,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        page.ifPresent(p -> currentPage = p);
        size.ifPresent(s -> pageSize = s);

        List<Store> storeListAll = storeService.getStoreList((User) principal.getPrincipal());

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