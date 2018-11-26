package com.lyna.web.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyna.commons.infrustructure.controller.AbstractCustomController;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.stores.service.StoreService;
import com.lyna.web.domain.user.User;
import com.lyna.web.domain.user.UserAggregate;
import com.lyna.web.domain.user.UserRequestPage;
import com.lyna.web.domain.user.service.UserService;
import com.lyna.web.domain.view.UserList;
import com.lyna.commons.infrustructure.object.RequestPage;
import com.lyna.web.security.authorities.IsAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
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

    @GetMapping(value = {"/update/{userId}"})
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

    @GetMapping(value = "/list2")
    public String userPage(Model model, UsernamePasswordAuthenticationToken principal, @RequestParam Integer limit,
                           @RequestParam Integer cp, @RequestParam String name, @RequestParam String mail,
                           @RequestParam Date start, @RequestParam Date end, @RequestParam String umail) {

        RequestPage userRequestPage = new UserRequestPage();
        userRequestPage.setCurrentPage(cp);
        userRequestPage.setNoOfRowInPage(limit);
        // searchFields
        // sortFields

        userService.findUsersWithPaging(userRequestPage);
        return REDIRECT_TO_USER_LIST_PAGE;
    }

    @GetMapping(value = "/list")
    @IsAdmin
    public String listUsers(
            Model model,
            UsernamePasswordAuthenticationToken principal,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        page.ifPresent(p -> currentPage = p);
        size.ifPresent(s -> pageSize = s);

        int tenantId = ((User) principal.getPrincipal()).getTenantId();

        List<Store> storeListAll = storeService.getStoreList(tenantId);

        Page<UserList> userPage =
                userService.findPaginated(PageRequest.of(currentPage - 1, pageSize), storeListAll, tenantId);


        model.addAttribute("userPage", userPage);
        model.addAttribute("storeModel", storeListAll);
        if (principal != null && principal.getPrincipal() != null)
            model.addAttribute("userId", ((User) principal.getPrincipal()).getId());

        int totalPages = userPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "user/listUser";
    }

    @GetMapping(value = {"/delete"})
    public @ResponseBody
    String addNew(HttpServletRequest request) {
        String userIds = request.getParameter("userIds");
        ObjectMapper mapper = new ObjectMapper();
        String ajaxResponse = "";
        try {
            String response = userService.deleteUser(userIds);//
            ajaxResponse = mapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return ajaxResponse;
    }
}