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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/package")
public class PackageController extends AbstractCustomController {

    @Autowired
    private PackageService packageService;

    @Autowired
    private LogiticDetailService logiticDetailService;

    @Autowired
    private DeliveryDetailService deliveryDetailService;


    @GetMapping(value = "/create")
    public String registerPackage(Model model) {
        Package mpackage = new Package();
        model.addAttribute("package", mpackage);
        return "package/registerPackage";
    }

    @PostMapping(value = "/create")
    public String savePackage(UsernamePasswordAuthenticationToken principal,
                              Model model, @Valid @ModelAttribute("package") Package mpackage,
                              BindingResult result, RedirectAttributes redirect) {
        if (Objects.isNull(mpackage)) {

            model.addAttribute("package", mpackage);
            return "package/registerPackage";
        }

        if (result.hasErrors()) {

            model.addAttribute("package", mpackage);
            return "package/registerPackage";
        }

        packageService.createPackage(mpackage, principal);
        return "redirect:/package/list";
    }

    @PostMapping(value = "/update")
    @IsAdmin
    public String updatePackage(UsernamePasswordAuthenticationToken principal, Model model, @Valid @ModelAttribute("package")
            Package mpackage, BindingResult result, RedirectAttributes redirect) {
        if (Objects.isNull(mpackage)) {
            model.addAttribute("package", mpackage);
            return "package/editPackage";
        }
        if (result.hasErrors()) {
            model.addAttribute("package", mpackage);
            return "package/editPackage";
        }


        packageService.updatePackage(mpackage, principal);

        return "redirect:/package/list";

    }

    @GetMapping(value = "/list")
    public String listPackage(Model model, UsernamePasswordAuthenticationToken principal) {
        User currentUser = (User) principal.getPrincipal();
        model.addAttribute("packages", packageService.findByTenantId(currentUser.getTenantId()));
        return "package/listPackage";
    }

    @GetMapping("/delete")
    public @ResponseBody
    String deleteByPackageIds(@RequestParam(value = "arrayPackageId[]") List<String> listPackageId) {
        if (!Objects.isNull(listPackageId) && !CollectionUtils.isEmpty(listPackageId)) {
            deliveryDetailService.deleteByPackageIds(listPackageId);
            logiticDetailService.deleteByPackageIds(listPackageId);
            packageService.deleteByPackageIds(listPackageId);
            return "true";
        }
        return "false";
    }

    @GetMapping(value = "/update/{packageId}")
    public String editPackage(@PathVariable("packageId") String packageId, Model model) {
        model.addAttribute("package", packageService.findOneByPakageId(packageId));
        return "package/editPackage";
    }


}

