package com.lyna.web.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyna.commons.infrustructure.controller.AbstractCustomController;
import com.lyna.commons.utils.Constants;
import com.lyna.commons.utils.DataUtils;
import com.lyna.web.domain.postCourse.PostCourse;
import com.lyna.web.domain.postCourse.sevice.PostCourseService;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.stores.service.StoreService;
import com.lyna.web.domain.user.User;
import com.lyna.web.security.authorities.IsAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/store")
public class StoreController extends AbstractCustomController {

    private static final String STORE_LIST_PAGE = "store/liststore";
    private static final String STORE_EDIT_PAGE = "store/editStore";
    private static final String REDIRECT_STORE_LIST_PAGE = "redirect:/store/list";
    private static final String STORE_REGISTER_PAGE = "store/registerStore";
    private final Logger log = LoggerFactory.getLogger(StoreController.class);
    @Autowired
    private PostCourseService postCourseService;
    @Autowired
    private StoreService storeService;

    @GetMapping(value = "/create")
    @IsAdmin
    public String createStore(Model model, @ModelAttribute("store") Store store) {
        List<PostCourse> postCourses = new ArrayList<PostCourse>();
        postCourses.add(new PostCourse());
        store.setPostCourses(postCourses);
        model.addAttribute("store", store);
        return STORE_REGISTER_PAGE;
    }

    @PostMapping(value = "/create")
    public String create(UsernamePasswordAuthenticationToken principal, Model model, @Valid @ModelAttribute("store")
            Store store, BindingResult result) {
        User user = (User) principal.getPrincipal();
        if (Objects.isNull(store) || result.hasErrors()) {
            model.addAttribute("store", store);
            return STORE_REGISTER_PAGE;
        }

        try {
            if (!Objects.isNull(storeService.findOneByCode(store.getCode()))) {
                model.addAttribute("errorStoreExitsted", "このコードは既に存在します。");
                model.addAttribute("store", store);
                return STORE_REGISTER_PAGE;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        storeService.create(store, user);
        DataUtils.putMapData(Constants.ENTITY_STATUS.CREATED, store.getStoreId());
        return REDIRECT_STORE_LIST_PAGE;
    }

    @PostMapping(value = "/update")
    public String update(UsernamePasswordAuthenticationToken principal, Model model, @Valid @ModelAttribute("store")
            Store store, BindingResult result) {
        User user = (User) principal.getPrincipal();
        if (Objects.isNull(store) || result.hasErrors()) {
            model.addAttribute("store", store);
            return STORE_EDIT_PAGE;
        }
        Store storeExisted = storeService.findOneByStoreIdAndTenantId(store.getStoreId(), store.getTenantId());
        try {
            if (!store.getCode().equals(storeExisted.getCode()) && !Objects.isNull(storeService.findOneByCode(store.getCode()))) {
                model.addAttribute("errorStoreExitsted", "このコードは既に存在します。");
                model.addAttribute("store", store);
                return STORE_EDIT_PAGE;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        storeService.update(store, user);
        DataUtils.putMapData(Constants.ENTITY_STATUS.UPDATED, store.getStoreId());
        return REDIRECT_STORE_LIST_PAGE;

    }

    @GetMapping(value = "/update/{storeId}/{tenantId}")
    public String updateStore(@PathVariable("storeId") String storeId,
                              @PathVariable("tenantId") int tenantId, Model model) {
        Store store = storeService.findOneByStoreIdAndTenantId(storeId, tenantId);
        model.addAttribute("store", store);
        return STORE_EDIT_PAGE;
    }

    @GetMapping(value = "/list")
    @IsAdmin
    public String listStore(
            Model model,
            UsernamePasswordAuthenticationToken principal
    ) {
        int tenantId = ((User) principal.getPrincipal()).getTenantId();

        Page<Store> storePage =
                storeService.findPaginated(tenantId);

        model.addAttribute("storePage", storePage);
        model.addAttribute("message", DataUtils.getMapData());
        DataUtils.evicMapData();
        return STORE_LIST_PAGE;
    }

    @GetMapping(value = {"/delete"})
    public @ResponseBody
    String deleteStore(UsernamePasswordAuthenticationToken principal, HttpServletRequest request, @RequestParam(value = "storeIds[]") List<String> storeIds) {
        User user = (User) principal.getPrincipal();
        ObjectMapper mapper = new ObjectMapper();
        String ajaxResponse = "";
        try {
            boolean response = postCourseService.deleteByStoreIdsAndTenantId(storeIds, user.getTenantId());
            ajaxResponse = mapper.writeValueAsString(response);
            DataUtils.putMapData(Constants.ENTITY_STATUS.DELETED, storeIds.toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return ajaxResponse;
    }
}
