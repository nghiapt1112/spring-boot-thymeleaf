package com.lyna.web.application;

import com.lyna.commons.infrustructure.controller.AbstractCustomController;
import com.lyna.web.domain.delivery.service.DeliveryDetailService;
import com.lyna.web.domain.logicstics.service.LogiticDetailService;
import com.lyna.web.domain.mpackage.Package;
import com.lyna.web.domain.mpackage.service.PackageService;
import com.lyna.web.domain.user.User;
import com.lyna.web.security.authorities.IsAdmin;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/package")
public class PackageController extends AbstractCustomController {

    private static final String PACKAGE_LIST_PAGE = "package/listPackage";
    private static final String PACKAGE_EDIT_PAGE = "package/editPackage";
    private static final String REDIRECT_PACKAGE_LIST_PAGE = "redirect:/package/list";
    private static final String PACKAGE_REGISTER_PAGE = "package/registerPackage";
    @Autowired
    private PackageService packageService;
    @Autowired
    private LogiticDetailService logiticDetailService;
    @Autowired
    private DeliveryDetailService deliveryDetailService;

    @GetMapping(value = "/create")
    public String createPackage(Model model) {
        Package mpackage = new Package();
        model.addAttribute("package", mpackage);
        return PACKAGE_REGISTER_PAGE;
    }

    @PostMapping(value = "/create")
    public String create(UsernamePasswordAuthenticationToken principal,
                         Model model, @Valid @ModelAttribute("package") Package mpackage,
                         BindingResult result) {
        if (Objects.isNull(mpackage) || result.hasErrors()) {
            model.addAttribute("package", mpackage);
            return PACKAGE_REGISTER_PAGE;
        }

        packageService.create(mpackage, principal);
        return REDIRECT_PACKAGE_LIST_PAGE;
    }

    @PostMapping(value = "/update")
    @IsAdmin
    public String update(UsernamePasswordAuthenticationToken principal, Model model, @Valid @ModelAttribute("package")
            Package mpackage, BindingResult result) {
        if (Objects.isNull(mpackage) || result.hasErrors()) {
            model.addAttribute("package", mpackage);
            return PACKAGE_EDIT_PAGE;
        }

        packageService.update(mpackage, principal);

        return REDIRECT_PACKAGE_LIST_PAGE;

    }

    @GetMapping(value = "/list")
    public String listPackage(Model model, UsernamePasswordAuthenticationToken principal) {
        User currentUser = (User) principal.getPrincipal();
        model.addAttribute("packages", packageService.findByTenantId(currentUser.getTenantId()));
        return PACKAGE_LIST_PAGE;
    }

    @GetMapping("/delete")
    public @ResponseBody
    String deleteByPackageIds(@RequestParam(value = "packageIds[]") List<String> packageIds) {
        if (!Objects.isNull(packageIds) && !CollectionUtils.isEmpty(packageIds)) {
            deliveryDetailService.deleteByPackageIds(packageIds);
            return "true";
        }
        return "false";
    }

    @GetMapping(value = "/update/{packageId}")
    public String updatePackage(@PathVariable("packageId") String packageId, Model model) {
        model.addAttribute("package", packageService.findOneByPakageId(packageId));
        return PACKAGE_EDIT_PAGE;
    }


}

