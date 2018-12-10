package com.lyna.web.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyna.commons.infrustructure.controller.AbstractCustomController;
import com.lyna.web.domain.postCourse.PostCourse;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.stores.service.StoreService;
import com.lyna.web.domain.user.User;
import com.lyna.web.security.authorities.IsAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

    private static final String STORE_LIST_PAGE = "store/liststore";
    private static final String STORE_EDIT_PAGE = "store/editStore";
    private static final String REDIRECT_STORE_LIST_PAGE = "redirect:/store/list";
    private static final String STORE_REGISTER_PAGE = "store/registerStore";
    private final Logger log = LoggerFactory.getLogger(StoreController.class);
    @Autowired
    private StoreService storeService;

    private String codeBeforUpdate;

    @GetMapping(value = "/create")
    @IsAdmin
    public String registerStore(Model model, @ModelAttribute("store") Store store) {
        List<PostCourse> postCourses = new ArrayList<PostCourse>();
        postCourses.add(new PostCourse());
        store.setPostCourses(postCourses);
        model.addAttribute("store", store);
        return STORE_REGISTER_PAGE;
    }

    @PostMapping(value = "/create")
    public String createStore(UsernamePasswordAuthenticationToken principal, Model model, @Valid @ModelAttribute("store")
            Store store, BindingResult result, RedirectAttributes redirect) {
        if (Objects.isNull(store)) {
            model.addAttribute("store", store);
            return STORE_REGISTER_PAGE;
        }

        if (result.hasErrors()) {
            model.addAttribute("store", store);
            return STORE_REGISTER_PAGE;
        }

        Store existedStore = storeService.findOneByCode(store.getCode());
        if (!Objects.isNull(existedStore)) {
            model.addAttribute("errorCodeShow", "このコードは既に存在します。");
            model.addAttribute("store", store);
            return STORE_REGISTER_PAGE;
        }

        storeService.createStore(store, principal);

        return REDIRECT_STORE_LIST_PAGE;

    }

    @PostMapping(value = "/update")
    public String updateStore(UsernamePasswordAuthenticationToken principal, Model model, @Valid @ModelAttribute("store")
            Store store, BindingResult result, RedirectAttributes redirect) {

        if (Objects.isNull(store)) {
            model.addAttribute("store", store);
            return STORE_EDIT_PAGE;
        }

        if (result.hasErrors()) {
            model.addAttribute("store", store);
            return STORE_EDIT_PAGE;
        }

        Store existedStore = storeService.findOneByCode(store.getCode());
        if (!store.getCode().equals(codeBeforUpdate) && !Objects.isNull(existedStore)) {
            model.addAttribute("errorCodeShow", "このコードは既に存在します。");
            model.addAttribute("store", store);
            return STORE_REGISTER_PAGE;
        }


        storeService.updateStore(store, principal);

        return REDIRECT_STORE_LIST_PAGE;

    }

    @GetMapping(value = "/update/{storeId}")
    public String editStore(UsernamePasswordAuthenticationToken principal, @PathVariable("storeId") String storeId, Model model) {
        Store store = storeService.findOneByStoreId(storeId);
        codeBeforUpdate = store.getCode();
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

        return STORE_LIST_PAGE;
    }

    @SuppressWarnings("unused")
    @GetMapping(value = "/listtemp")
    @IsAdmin
    public String listStoretemp(
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

        return STORE_LIST_PAGE;
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
