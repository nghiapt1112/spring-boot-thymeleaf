package com.lyna.web.application;

import com.lyna.web.domain.delivery.service.DeliveryDetailService;
import com.lyna.web.domain.logicstics.service.LogiticDetailService;
import com.lyna.web.domain.mpackage.Package;
import com.lyna.web.domain.mpackage.repository.PackageRepository;
import com.lyna.web.domain.mpackage.service.PackageService;
import com.lyna.web.domain.postCourse.PostCourse;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.stores.service.StoreService;
import com.lyna.web.security.authorities.IsAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Controller
@RequestMapping("/package")
public class PackageController {

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
                              Model model,@Valid @ModelAttribute("package")Package mpackage,
                              BindingResult result, RedirectAttributes redirect) {

        if (result.hasErrors()) {
            System.out.println("requid error");
            model.addAttribute("package", mpackage);
            return "package/registerPackage";
        }
        if (Objects.isNull(mpackage)) {
            System.out.println("requid null");
            model.addAttribute("package", mpackage);
            return "package/registerPackage";
        }

        packageService.createPackage(mpackage,principal);
        return "redirect:/package/list";
    }
    @PostMapping(value = "/update")
    @IsAdmin
    public String updatePackage(UsernamePasswordAuthenticationToken principal, Model model, @Valid @ModelAttribute("package")
            Package mpackage, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("package", mpackage);
            return "package/editPackage";
        }
        if (Objects.isNull(mpackage)) {
            model.addAttribute("package", mpackage);
            return "package/editPackage";
        }

        packageService.updatePackage(mpackage,principal);

        return "redirect:/package/list";

    }

    @GetMapping(value = "/list")
    public String  listPackage(Model model){

        model.addAttribute("packages",packageService.findAll());
        return "package/listPackage";
    }

    @GetMapping("/delete")
    public @ResponseBody  String deletePackage(@RequestParam(value = "arrayPackageId[]") List<String> listPackageId ){
        if(!Objects.isNull(listPackageId) && !listPackageId.isEmpty()){
            deliveryDetailService.deletebyPackageId(listPackageId);
            logiticDetailService.deletebyPackageId(listPackageId);
            packageService.deletebyPackageId(listPackageId);
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

