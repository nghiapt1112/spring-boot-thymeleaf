package com.lyna.web.application;

import com.lyna.commons.infrustructure.controller.AbstractCustomController;
import com.lyna.web.domain.postCourse.PostCourse;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.stores.service.StoreService;
import com.lyna.web.domain.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
public class StoreController extends AbstractCustomController {

    private final Logger log = LoggerFactory.getLogger(StoreController.class);

    @Autowired
    private StoreService storeService;

    @GetMapping(value = "/store/registerStore")
    public String registerStore(Model model, @ModelAttribute("store") Store store) {
        List<PostCourse> postCourses = new ArrayList<PostCourse>();
        postCourses.add(new PostCourse());
        store.setPostCourses(postCourses);
        model.addAttribute("store", store);
        return "store/registerStore";
    }

    @PostMapping(value = "/store/registerStore")
    public String saveStore(UsernamePasswordAuthenticationToken principal, Model model, @Valid @ModelAttribute("store") Store store, BindingResult result, RedirectAttributes redirect) {
        System.out.println("registerStore()");
        User currentUser = (User) principal.getPrincipal();
        int tenantId = currentUser.getTenantId();
        String username = currentUser.getId();
        Date date = new Date();
        if (result.hasErrors()) {
            model.addAttribute("store", store);
            return "store/registerStore";
        }
        if (null == store) {
            log.error("store null");
            return "store/registerStore";
        }
        store.setCreateDate(date);
        store.setTenantId(tenantId);
        store.setCreateUser(username);
        List<PostCourse> postCourses = store.getPostCourses();
        if (null == postCourses) {
            log.error("postCourse null");
        } else if (postCourses.isEmpty()) {
            log.error("postCourse no have element");
        } else {
            for (PostCourse postCourse : postCourses) {
                postCourse.setTenantId(tenantId);
                postCourse.setStoreId(store.getStoreId());
                postCourse.setCreateDate(date);
                postCourse.setCreateUser(username);
            }
        }

        store.setPostCourses(postCourses);
        storeService.save(store);

        return "redirect:/store/listStore";

    }

    @PostMapping(value = "/store/editStore")
    public String editStore(UsernamePasswordAuthenticationToken principal, Model model, @Valid @ModelAttribute("store")
            Store store, BindingResult result, RedirectAttributes redirect) {
        User currentUser = (User) principal.getPrincipal();
        int tenantId = currentUser.getTenantId();
        String username = currentUser.getId();
        Date date = new Date();
        if (result.hasErrors()) {
            model.addAttribute("store", store);
            return "store/editStore";
        }
        if (Objects.isNull(store)) {
            log.error("store null");
            return "store/editStore";
        }
        store.setUpdateDate(date);
        store.setUpdateUser(username);
        store.setTenantId(tenantId);
        List<PostCourse> postCourses = store.getPostCourses();
        if (Objects.isNull(postCourses)) {
            log.error("postCourse null");
        } else if (postCourses.isEmpty()) {
            log.error("postCourse no have element");
        } else {
            PostCourse pc = null;
            for (PostCourse postCourse : postCourses) {
                if(null == postCourse.getPostCourseId()){
                    pc = new PostCourse();
                    postCourse.setPostCourseId(pc.getPostCourseId());
                    postCourse.setCreateUser(username);
                    postCourse.setCreateDate(date);
                    postCourse.setTenantId(tenantId);
                }else{
                    postCourse.setUpdateDate(date);
                    postCourse.setUpdateUser(username);
                    postCourse.setTenantId(tenantId);
                }

            }
        }
        store.setPostCourses(postCourses);
        storeService.save(store);

        return "redirect:/store/listStore";

    }

    @GetMapping(value = "/store/edit/{storeId}")
    public String editStore(@PathVariable("storeId") String storeId, Model model) {
        System.out.println(storeId);
        model.addAttribute("store", storeService.findOneByStoreId(storeId));
        return "store/editStore";
    }

    @GetMapping(value = "/store/listStore")
    public String listStore(Model model) {
        model.addAttribute("stores", storeService.findAll());
        return "store/listStore";
    }


}
