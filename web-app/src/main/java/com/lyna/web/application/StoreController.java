package com.lyna.web.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyna.commons.infrustructure.controller.AbstractCustomController;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/store")
public class StoreController extends AbstractCustomController {
    private static final String REDIRECT_TO_STORE_LIST_PAGE = "redirect:/store/list";
    private static final String STORE_LIST_PAGE = "store/liststore";

    @Autowired
    private StoreService storeService;

    @GetMapping(value = "/list")
    @IsAdmin
    public String listUsers(
            Model model,
            UsernamePasswordAuthenticationToken principal,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            @RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam("searchText") Optional<String> searchText,
            @RequestParam("columnsort") Optional<String> columnSort,
            @RequestParam(defaultValue = "asc") Optional<String> typeSort
    ) {
        String search = "";
        String sTextPage = "";
        List<Integer> pageNumbers = null;
        int tenantId = ((User) principal.getPrincipal()).getTenantId();
        int prevPage, nextPage = currentPage;

        if (searchText.isPresent() && !searchText.get().isEmpty()) {
            model.addAttribute("searchText", searchText.get());
            search = searchText.get();
        }

        Page<Store> storePage =
                storeService.findPaginated(PageRequest.of(currentPage - 1, pageSize), tenantId, search);

        int totalPages = storePage.getTotalPages();
        if (totalPages > 0) {
            pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());

            sTextPage = pageNumbers.size() + "件中 " + currentPage + " ~ " + pageNumbers.size() + " 件を表示";
            nextPage = currentPage < pageNumbers.size() ? currentPage + 1 : currentPage;
        }

        prevPage = currentPage > 1 ? currentPage - 1 : currentPage;
        model.addAttribute("prevPage", prevPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("storePage", storePage);
        model.addAttribute("currentPage", currentPage);
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
