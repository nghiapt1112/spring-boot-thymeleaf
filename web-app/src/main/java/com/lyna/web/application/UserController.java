package com.lyna.web.application;

import com.lyna.commons.infrustructure.controller.AbstractCustomController;
import com.lyna.commons.infrustructure.object.RequestPage;
import com.lyna.commons.utils.Constants;
import com.lyna.commons.utils.DataUtils;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.stores.service.StoreService;
import com.lyna.web.domain.user.User;
import com.lyna.web.domain.user.UserAggregate;
import com.lyna.web.domain.user.UserRequestPage;
import com.lyna.web.domain.user.UserResponsePage;
import com.lyna.web.domain.user.service.UserService;
import com.lyna.web.domain.user.service.UserStoreAuthorityService;
import com.lyna.web.domain.view.UserList;
import com.lyna.web.security.authorities.IsAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AuthenticationManager;
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

    private static final String USER_REGISTER_PAGE = "user/user-create";

    private static final String USER_UPDATE_PAGE = "user/user-update";

    private static final String USER_PROFILE_PAGE = "user/profile";

    private final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    public AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private UserStoreAuthorityService userStoreAuthorityService;

    private Integer[] PAGE_SIZE() {
        return env.getProperty("lyna.web.pageSize", Integer[].class);
    }

    private Integer LIMIT_ITEMS() {
        return env.getProperty("lyna.web.limitItems", Integer.class);
    }

    @PostMapping(value = {"/register", "/register/"})
    @IsAdmin
    public String registerUser(@ModelAttribute @Valid UserAggregate userRegisterAggregate, UsernamePasswordAuthenticationToken principal,
                               Model model) {
        User currentUser = (User) principal.getPrincipal();

        // TODO: => Hieu. Should you try-finally instead of try-catch(no thing)
        try {
            if (!Objects.isNull(this.userService.findByEmail(userRegisterAggregate.getEmail()))) {
                model.addAttribute("errorEmailShow", true);
                model.addAttribute("userRegisterAggregate", userRegisterAggregate);
                model.addAttribute("userPerRoles", userRegisterAggregate.getRolePerStore());
                return USER_REGISTER_PAGE;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        this.userService.registerUser(currentUser, userRegisterAggregate);
        DataUtils.putMapData(Constants.ENTITY_STATUS.CREATED, currentUser.getId());
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

        return USER_REGISTER_PAGE;
    }

    @GetMapping(value = {"/update/{userId}"})
    @IsAdmin
    public String updateUserPage(Model model, UsernamePasswordAuthenticationToken principal, @PathVariable String userId) {
        User currentUser = (User) principal.getPrincipal();
        UserAggregate aggregate = new UserAggregate().fromUserEntity(userService.findByUserIdAndTenantId(currentUser.getTenantId(), userId));

        aggregate.updateRolePerStore(storeService.findAll(currentUser.getTenantId()));

        model.addAttribute("aggregate", aggregate);
        model.addAttribute("role", currentUser.getRole());

        return USER_UPDATE_PAGE;
    }

    @GetMapping(value = {"/profile"})
    public String updateProfilePage(Model model, UsernamePasswordAuthenticationToken principal) {
        User currentUser = (User) principal.getPrincipal();
        UserAggregate aggregate = new UserAggregate().fromUserEntity(userService.findByUserIdAndTenantId(currentUser.getTenantId(), currentUser.getId()));
        aggregate.updateRolePerStore(storeService.findAll(currentUser.getTenantId()));
        model.addAttribute("aggregate", aggregate);
        model.addAttribute("userId", currentUser.getId());
        model.addAttribute("role", currentUser.getRole());


        return USER_PROFILE_PAGE;
    }

    @PostMapping(value = {"/profile"})
    public String updateProfile(HttpServletRequest request, UsernamePasswordAuthenticationToken principal, UserAggregate aggregate, Model model) {
        User currentUser = (User) principal.getPrincipal();
        User userExisted = this.userService.findByUserIdAndTenantId(currentUser.getTenantId(), aggregate.getUserId());
        try {
            if (!userExisted.getEmail().equals(aggregate.getEmail()) && !Objects.isNull(this.userService.findByEmail(aggregate.getEmail()))) {
                aggregate.updateRolePerStore(storeService.findAll(currentUser.getTenantId()));
                model.addAttribute("errorEmailShow", true);
                model.addAttribute("aggregate", aggregate);
                model.addAttribute("message", DataUtils.getMapData());
                return USER_PROFILE_PAGE;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        aggregate.updateRolePerStore(storeService.findAll(currentUser.getTenantId()));
        this.userService.update(currentUser, aggregate);
        DataUtils.putMapData(Constants.ENTITY_STATUS.UPDATED, currentUser.getId());

        currentUser.setName(aggregate.getName());
//        currentUser.setName("AAAA");
        return "redirect:/mainScreen";
    }


    @PostMapping(value = {"/update", "/update/"})
    @IsAdmin
    public String updateUser(UsernamePasswordAuthenticationToken principal, @Valid UserAggregate aggregate, Model model) {
        User currentUser = (User) principal.getPrincipal();

        User userExisted = this.userService.findByUserIdAndTenantId(currentUser.getTenantId(), aggregate.getUserId());
        try {
            if (!userExisted.getEmail().equals(aggregate.getEmail()) && !Objects.isNull(this.userService.findByEmail(aggregate.getEmail()))) {
                aggregate.updateRolePerStore(storeService.findAll(currentUser.getTenantId()));
                model.addAttribute("errorEmailShow", true);
                model.addAttribute("aggregate", aggregate);
                return USER_UPDATE_PAGE;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        userService.update(currentUser, aggregate);
        DataUtils.putMapData(Constants.ENTITY_STATUS.UPDATED, currentUser.getId());
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
        model.addAttribute("message", DataUtils.getMapData());
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
    String deleteByUserIds(@RequestParam(value = "objectIds[]") List<String> userIds, UsernamePasswordAuthenticationToken principal) {
        User user = (User) principal.getPrincipal();
        userStoreAuthorityService.deleteUserStoreAuthorityByUserIds(userIds, user.getTenantId());
        DataUtils.putMapData(Constants.ENTITY_STATUS.DELETED, userIds.toString());
        return "true";
    }
}
