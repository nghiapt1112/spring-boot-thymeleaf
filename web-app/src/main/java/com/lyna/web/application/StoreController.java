package com.lyna.web.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyna.commons.infrustructure.controller.AbstractCustomController;
import com.lyna.web.domain.postCourse.PostCourse;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.stores.service.StoreService;
import com.lyna.web.domain.user.User;
import com.lyna.web.security.authorities.IsAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/store")
public class StoreController extends AbstractCustomController {
    private static final String REDIRECT_TO_STORE_LIST_PAGE = "redirect:store/liststore";
    private static final String REDIRECT_TO_STORE_EDIT_PAGE = "redirect:store/editStore";
    private static final String REDIRECT_TO_STORE_REGISTER_PAGE = "redirect:store/registerStore";



    @Autowired
    private StoreService storeService;

    @GetMapping(value = "/store/create")
    public String registerStore(Model model, @ModelAttribute("store") Store store) {
        List<PostCourse> postCourses = new ArrayList<PostCourse>();
        postCourses.add(new PostCourse());
        store.setPostCourses(postCourses);
        model.addAttribute("store", store);
        return REDIRECT_TO_STORE_REGISTER_PAGE;
    }

    @PostMapping(value = "/store/create")
    public String CreateStore(UsernamePasswordAuthenticationToken principal, Model model, @Valid @ModelAttribute("store") Store store, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("store", store);
            return REDIRECT_TO_STORE_REGISTER_PAGE;
        }
        if (null == store) {
            return "store/registerStore";
        }
        storeService.createStore(store, principal);

        return REDIRECT_TO_STORE_LIST_PAGE;

    }

    @PostMapping(value = "/store/update")
    public String updateStore(UsernamePasswordAuthenticationToken principal, Model model, @Valid @ModelAttribute("store")
            Store store, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("store", store);
            return REDIRECT_TO_STORE_EDIT_PAGE;
        }
        if (Objects.isNull(store)) {
            return REDIRECT_TO_STORE_EDIT_PAGE;
        }
        storeService.updateStore(store, principal);
        return REDIRECT_TO_STORE_LIST_PAGE;

    }

    @GetMapping(value = "/store/update/{storeId}")
    public String editStore(@PathVariable("storeId") String storeId, Model model) {
        System.out.println(storeId);
        model.addAttribute("store", storeService.findOneByStoreId(storeId));
        return REDIRECT_TO_STORE_EDIT_PAGE;
    }

    @GetMapping(value = "/list")
    @IsAdmin
    public String listUsers(
            Model model,
            UsernamePasswordAuthenticationToken principal,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam("searchText") Optional<String> searchText,
            @RequestParam("columnsort") Optional<String> columnSort,
            @RequestParam(defaultValue = "asc") Optional<String> typeSort
    ) {
        String search = "";
        String sTextPage = "";
        List<Integer> pageNumbers = null;
        int tenantId = ((User) principal.getPrincipal()).getTenantId();
        int prevPage, nextPage = page;

        if (searchText.isPresent() && !searchText.get().isEmpty()) {
            model.addAttribute("searchText", searchText.get());
            search = searchText.get();
        }

        Page<Store> storePage =
                storeService.findPaginated(PageRequest.of(page - 1, size), tenantId, search);

        int totalPages = storePage.getTotalPages();
        if (totalPages > 0) {
            pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());

            sTextPage = pageNumbers.size() + "件中 " + page + " ~ " + pageNumbers.size() + " 件を表示";
            nextPage = page < pageNumbers.size() ? page + 1 : page;
        }

        prevPage = page > 1 ? page - 1 : page;
        model.addAttribute("prevPage", prevPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("storePage", storePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("spage", sTextPage);
        model.addAttribute("pageNumbers", pageNumbers);

        return REDIRECT_TO_STORE_LIST_PAGE;
    }

    @GetMapping(value = {"/delete"})
    public @ResponseBody
    String addNew(HttpServletRequest request) {
        ObjectMapper mapper = new ObjectMapper();
        String storeIds = request.getParameter("storeId");
        String ajaxResponse = "";
        try {
            String response = storeService.deleteStore(storeIds);
            ajaxResponse = mapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return ajaxResponse;
    }
}
