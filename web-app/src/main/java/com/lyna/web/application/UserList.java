package com.lyna.web.application;

import com.lyna.commons.infrustructure.controller.AbstractCustomController;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.stores.repository.StoreRepository;
import com.lyna.web.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class UserList extends AbstractCustomController {

    private static int currentPage = 1;
    private static int pageSize = 5;
    @Autowired
    private UserService userService;

    @Autowired
    private StoreRepository storeRepository;

    @RequestMapping(value = "/userlist", method = RequestMethod.GET)
    public String listUsers(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        page.ifPresent(p -> currentPage = p);
        size.ifPresent(s -> pageSize = s);


        List<Store> storeListAll = storeRepository.getAll();

        Page<com.lyna.web.domain.user.UserList> userPage =
                userService.findPaginated(PageRequest.of(currentPage - 1, pageSize), storeListAll);


        model.addAttribute("userPage", userPage);
        model.addAttribute("storeModel", storeListAll);

        int totalPages = userPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "user/listUser";
    }

    @RequestMapping(value = "/deleteuser", method = RequestMethod.POST)
    public String delete(HttpServletRequest request, ModelMap modelMap) {
        try {
            for (String userid : request.getParameterValues("userid")) {

            }
        } catch (Exception ex) {

        }
        return "redirect:user/listUser";
    }
}
