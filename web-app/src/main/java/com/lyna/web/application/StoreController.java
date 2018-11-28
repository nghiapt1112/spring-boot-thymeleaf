package com.lyna.web.application;

import com.lyna.commons.infrustructure.controller.AbstractCustomController;
import com.lyna.web.domain.postCourse.PostCourse;
import com.lyna.web.domain.postCourse.sevice.PostCourseService;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.stores.service.StoreService;
import com.lyna.web.domain.user.User;
import org.hibernate.SessionFactory;
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
import java.util.ListIterator;
import java.util.Objects;

@Controller
public class StoreController extends AbstractCustomController {

    private final Logger log = LoggerFactory.getLogger(StoreController.class);

    @Autowired
    private StoreService storeService;

    @Autowired
    private PostCourseService postCourseService;

    @GetMapping(value = "/store/create")
    public String registerStore(Model model, @ModelAttribute("store") Store store) {
        List<PostCourse> postCourses = new ArrayList<PostCourse>();
        postCourses.add(new PostCourse());
        store.setPostCourses(postCourses);
        model.addAttribute("store", store);
        return "store/registerStore";
    }

    @PostMapping(value = "/store/create")
    public String CreateStore(UsernamePasswordAuthenticationToken principal, Model model, @Valid @ModelAttribute("store") Store store, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("store", store);
            return "store/registerStore";
        }
        if (null == store) {
            log.error("store null");
            return "store/registerStore";
        }
        storeService.createStore(store, principal);

        return "redirect:/store/listStore";

    }

    @PostMapping(value = "/store/update")
    public String updateStore(UsernamePasswordAuthenticationToken principal, Model model, @Valid @ModelAttribute("store")
            Store store, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("store", store);
            return "store/editStore";
        }
        if (Objects.isNull(store)) {
            log.error("store null");
            return "store/editStore";
        }
        storeService.updateStore(store, principal);
        return "redirect:/store/listStore";

    }

    @GetMapping(value = "/store/update/{storeId}")
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
