package com.lyna.web.application;

import com.lyna.commons.infrustructure.controller.AbstractCustomController;
import com.lyna.commons.utils.Constants;
import com.lyna.commons.utils.DataUtils;
import com.lyna.web.domain.delivery.service.DeliveryDetailService;
import com.lyna.web.domain.mpackage.Package;
import com.lyna.web.domain.mpackage.service.PackageService;
import com.lyna.web.domain.storagefile.service.StorageService;
import com.lyna.web.domain.user.User;
import com.lyna.web.security.authorities.IsAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/package")
public class PackageController extends AbstractCustomController {

    private static final String PACKAGE_LIST_PAGE = "package/listPackage";
    private static final String PACKAGE_EDIT_PAGE = "package/editPackage";
    private static final String REDIRECT_PACKAGE_LIST_PAGE = "redirect:/package/list";
    private static final String PACKAGE_REGISTER_PAGE = "package/registerPackage";
    private final StorageService storageService;

    @Autowired
    private PackageService packageService;
    @Autowired
    private DeliveryDetailService deliveryDetailService;
    @Autowired
    public PackageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/file/package")
    public ResponseEntity<Object> handleFileUploadPackage(Model model, @RequestParam MultipartFile file,
                                                          UsernamePasswordAuthenticationToken principal) throws IOException {
        User user = (User) principal.getPrincipal();
        Map<Integer, String> mapError = storageService.store(user, file, 5);
        String result = "ファイルは成功にアップロードされた";
        if (mapError.size() > 0) {
            model.addAttribute("messageError", mapError);
            return new ResponseEntity<>(mapError, HttpStatus.INTERNAL_SERVER_ERROR);
        } else
            DataUtils.putMapData(Constants.ENTITY_STATUS.IMPORT, result);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

//    @ExceptionHandler(StorageFileNotFoundException.class)
//    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
//        return ResponseEntity.notFound().build();
//    }
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
        DataUtils.putMapData(Constants.ENTITY_STATUS.CREATED, mpackage.getPackageId());
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
        DataUtils.putMapData(Constants.ENTITY_STATUS.UPDATED, mpackage.getPackageId());
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
        DataUtils.putMapData(Constants.ENTITY_STATUS.DELETED, packageIds.toString());
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

