package com.lyna.web.application;

import com.lyna.commons.infrustructure.controller.AbstractCustomController;
import com.lyna.commons.utils.DataUtils;
import com.lyna.web.domain.delivery.service.DeliveryDetailService;
import com.lyna.web.domain.mpackage.Package;
import com.lyna.web.domain.mpackage.service.PackageService;
import com.lyna.web.domain.user.User;
import com.lyna.web.security.authorities.IsAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
        User user = (User) principal.getPrincipal();
        if (Objects.isNull(mpackage) || result.hasErrors()) {
            model.addAttribute("package", mpackage);
            return PACKAGE_REGISTER_PAGE;
        }

        packageService.create(mpackage, user);
        //ToDo: set 1 to file contans!
        DataUtils.putMapData(1, mpackage.getPackageId());
        return REDIRECT_PACKAGE_LIST_PAGE;
    }

    @PostMapping(value = "/update")
    @IsAdmin
    public String update(UsernamePasswordAuthenticationToken principal, Model model, @Valid @ModelAttribute("package")
            Package mpackage, BindingResult result) {
        User user = (User) principal.getPrincipal();
        if (Objects.isNull(mpackage) || result.hasErrors()) {
            model.addAttribute("package", mpackage);
            return PACKAGE_EDIT_PAGE;
        }

        packageService.update(mpackage, user);
        //ToDo: Set 3 to file const
        DataUtils.putMapData(3, mpackage.getPackageId());
        return REDIRECT_PACKAGE_LIST_PAGE;

    }

    @GetMapping(value = "/list")
    public String listPackage(Model model, UsernamePasswordAuthenticationToken principal) {
        User user = (User) principal.getPrincipal();
        model.addAttribute("packages", packageService.findByTenantId(user.getTenantId()));
        model.addAttribute("message", DataUtils.getMapData());
        return PACKAGE_LIST_PAGE;
    }

    @GetMapping("/delete")
    public @ResponseBody
    String deleteByPackageIds(@RequestParam(value = "ojectIds[]") List<String> packageIds, UsernamePasswordAuthenticationToken principal) {
        User user = (User) principal.getPrincipal();
        deliveryDetailService.deleteDeliveryDetailByPackageIdsAndTenantId(packageIds, user.getTenantId());
        //ToDo: cho 2 to class const
        DataUtils.putMapData(2, packageIds.toString());
        return "true";
    }

    @GetMapping(value = "/update/{packageId}/{tenantId}")
    public String updatePackage(@PathVariable("packageId") String packageId, Model model,
                                @PathVariable("tenantId") int tenantId) {
        Package mpackage = packageService.findOneByPakageIdAndTenantId(packageId, tenantId);
        model.addAttribute("package", mpackage);
        return PACKAGE_EDIT_PAGE;
    }


}

