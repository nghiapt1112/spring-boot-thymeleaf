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
    private static final String REDIRECT_TO_USER_LIST_PAGE = "redirect:/store/list";
    private static int currentPage = 1;
    private static Integer pageSize = 5;

    @Autowired
    private StoreService storeService;

    @GetMapping(value = "/list")
    @IsAdmin
    public String listUsers(
            Model model,
            UsernamePasswordAuthenticationToken principal,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size,
            @RequestParam("currentPage") Optional<Integer> pCurrentPage,
            @RequestParam("pageSize") Optional<Integer> pPageSize,
            @RequestParam("searchText") Optional<String> searchText,
            @RequestParam("sort") Optional<String> sort
    ) {

        if (pCurrentPage.isPresent())
            currentPage = pCurrentPage.get();
        if (pPageSize.isPresent())
            pageSize = pPageSize.get();

        page.ifPresent(p -> currentPage = p);
        size.ifPresent(s -> pageSize = s);

        String search = "";

        int tenantId = ((User) principal.getPrincipal()).getTenantId();

        if (searchText.isPresent() && !searchText.get().isEmpty()) {
            model.addAttribute("searchText", searchText.get());
            search = searchText.get();
        }

        Page<Store> storePage =
                storeService.findPaginated(PageRequest.of(currentPage - 1, pageSize), tenantId, search);

        model.addAttribute("storePage", storePage);
        model.addAttribute("currentPage", currentPage);


        //TODO: LinhNM để vào 1 hàm để cho gọn code
        if (currentPage > 1)
            model.addAttribute("prevPage", currentPage - 1);
        else
            model.addAttribute("prevPage", currentPage);


        if (principal != null && principal.getPrincipal() != null)
            model.addAttribute("userId", ((User) principal.getPrincipal()).getId());

        int totalPages = storePage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
            String spage = pageNumbers.size() + "件中 " + currentPage + " ~ " + pageNumbers.size() + " 件を表示";
            model.addAttribute("spage", spage);

            if (currentPage < pageNumbers.size())
                model.addAttribute("nextPage", currentPage + 1);
            else
                model.addAttribute("nextPage", currentPage);
        }

        return "store/liststore";
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
