package com.lyna.web.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyna.commons.infrustructure.controller.AbstractCustomController;
import com.lyna.commons.infrustructure.object.RequestPage;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.stores.service.StoreService;
import com.lyna.web.domain.user.User;
import com.lyna.web.domain.user.UserAggregate;
import com.lyna.web.domain.user.UserRequestPage;
import com.lyna.web.domain.user.UserResponsePage;
import com.lyna.web.domain.user.service.UserService;
import com.lyna.web.domain.view.UserList;
import com.lyna.web.security.authorities.IsAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/user")
public class UserController extends AbstractCustomController {
    private static final String REDIRECT_TO_USER_LIST_PAGE = "redirect:/user/list";
    @Autowired
    private UserService userService;
    @Autowired
    private StoreService storeService;

    private Integer[] PAGE_SIZE() {
        return env.getProperty("lyna.web.pageSize", Integer[].class);
    }

    private Integer LIMIT_ITEMS() {
        return env.getProperty("lyna.web.limitItems", Integer.class);
    }

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
        userRegisterAggregate.updateRolePerStore(storeService.findByTenantId(currentUser.getTenantId()));

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


    @GetMapping(value = {"/profile}"})
    @IsAdmin
    public String updateUserById(Model model, UsernamePasswordAuthenticationToken principal) {
        User currentUser = (User) principal.getPrincipal();
        UserAggregate aggregate = new UserAggregate().fromUserEntity(userService.findById(currentUser.getTenantId(), currentUser.getId()));

        model.addAttribute("aggregate", aggregate);
        model.addAttribute("userId", currentUser.getId());
        return "user/user-update";
    }

    @PostMapping(value = {"/update", "/update/"})
    @IsAdmin
    public String updateUser(UsernamePasswordAuthenticationToken principal, @Valid UserAggregate aggregate) {
        User currentUser = (User) principal.getPrincipal();
        userService.update(currentUser, aggregate);
        return REDIRECT_TO_USER_LIST_PAGE;
    }

    @GetMapping(value = "/list")
    @IsAdmin
    public String userPage(Model model, UsernamePasswordAuthenticationToken principal) {
        User user = (User) principal.getPrincipal();
        int tenantId = user.getTenantId();

        List<Store> storesInTenant = storeService.getStoreList(tenantId);
        Page<UserList> userPage =
                userService.findPaginated(storesInTenant, tenantId);

        model.addAttribute("pageData", userPage);
        model.addAttribute("storeModel", storesInTenant);
        model.addAttribute("userId", user.getId());

        return "user/listUser";
    }

    @SuppressWarnings("unused")
    @GetMapping(value = "/listtemp")
    public String userPage(Model model, UsernamePasswordAuthenticationToken principal,
                           @RequestParam(required = false, defaultValue = "10") Integer limit,
                           @RequestParam(required = false, defaultValue = "1") Integer cp,
                           @RequestParam(required = false) Date start, @RequestParam(required = false) Date end,
                           @RequestParam(required = false) String search) {

        User user = (User) principal.getPrincipal();
        int tenantId = user.getTenantId();
        RequestPage userRequestPage = new UserRequestPage();
        userRequestPage.setCurrentPage(cp);
        userRequestPage.setNoOfRowInPage(limit);
        userRequestPage.addSearchField("search", search).addSearchField("start", start).addSearchField("end", end);

        UserResponsePage userPage = userService.findUsersWithPaging(userRequestPage);
        List<Store> storesInTenant = storeService.getStoreList(tenantId);

        model.addAttribute("pageData", userPage);
        model.addAttribute("storeModel", storesInTenant);
        model.addAttribute("limit", Objects.isNull(limit) ? LIMIT_ITEMS() : limit);
        model.addAttribute("currentPage", cp);
        model.addAttribute("pageSizes", PAGE_SIZE());
        model.addAttribute("userId", user.getId());

        return "user/listUser";
    }

    @GetMapping(value = {"/delete"})
    public @ResponseBody
    String deleteUser(HttpServletRequest request) {
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
