package com.lyna.web.application;

import com.lyna.commons.infrustructure.controller.AbstractCustomController;
import com.lyna.web.domain.logicstics.LogisticRequestPage;
import com.lyna.web.domain.logicstics.service.LogisticService;
import com.lyna.web.domain.order.OrderRequestPage;
import com.lyna.web.domain.order.service.OrderService;
import com.lyna.web.domain.user.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.lyna.commons.utils.DateTimeUtils.getCurrentDate;
import static com.lyna.commons.utils.DateTimeUtils.fromNumber;
import static com.lyna.commons.utils.DateTimeUtils.convertDateToString;
import static com.lyna.web.domain.logicstics.LogisticRequestPage.END_DATE;
import static com.lyna.web.domain.logicstics.LogisticRequestPage.POST_NAME;
import static com.lyna.web.domain.logicstics.LogisticRequestPage.START_DATE;
import static com.lyna.web.domain.logicstics.service.LogisticService.LOGISTIC_DATA;
import static com.lyna.web.domain.logicstics.service.LogisticService.PKG_TYPE;

@Controller
public class MainController extends AbstractCustomController {

    private static final String REDIRECT_TO_MAIN_PAGE = "";


    @Autowired
    private LogisticService logisticService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/mainScreen")
    public String mainScreen(UsernamePasswordAuthenticationToken principal, Model model,
                             @RequestParam(required = false) Long start,
                             @RequestParam(required = false) Long end,
                             @RequestParam(required = false) String postName) {
        User currentUser = (User) principal.getPrincipal();
        Date startDay = Objects.isNull(start) ? getCurrentDate() : fromNumber(start);
        Date endDay = Objects.isNull(end) ? getCurrentDate() : fromNumber(end);
        String searchPostName = (StringUtils.isEmpty(postName) || StringUtils.isEmpty(postName.trim())) ? null : postName;

        LogisticRequestPage logisticQueryBuilder = new LogisticRequestPage();
        logisticQueryBuilder
                .withTenantId(currentUser.getTenantId())
                .addSearchField(START_DATE, startDay)
                .addSearchField(END_DATE, endDay)
                .addSearchField(POST_NAME, searchPostName)
                .build();

        Map<String, Object> logisticData = logisticService.findLogisticsView(currentUser.getTenantId(), logisticQueryBuilder);

        OrderRequestPage orderQueryBuilder = new OrderRequestPage();
        orderQueryBuilder
                .withTenantId(currentUser.getTenantId())
                .addSearchField(START_DATE, startDay)
                .addSearchField(END_DATE, endDay)
                .addSearchField(POST_NAME, searchPostName)
                .build();

        model.addAttribute(LOGISTIC_DATA, logisticData.get(LOGISTIC_DATA));
        model.addAttribute(PKG_TYPE, logisticData.get(PKG_TYPE));
        model.addAttribute("orderData", orderService.findOrderViews(currentUser.getTenantId(), orderQueryBuilder));
        model.addAttribute("dateStart", convertDateToString(startDay));
        model.addAttribute("dateEnd", convertDateToString(endDay));
        model.addAttribute("postName" , searchPostName);
        return "main/mainMenu";
    }

    @GetMapping("/upload")
    public String upload() {
        return "layout";
    }

    @PostMapping(value = {"/upload/"})
    public String uploadOrder() {
        return REDIRECT_TO_MAIN_PAGE;
    }
}
