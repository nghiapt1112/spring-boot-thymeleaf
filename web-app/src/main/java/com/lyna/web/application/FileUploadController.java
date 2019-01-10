package com.lyna.web.application;

import com.lyna.commons.utils.Constants;
import com.lyna.commons.utils.DataUtils;
import com.lyna.web.domain.storagefile.exeption.StorageFileNotFoundException;
import com.lyna.web.domain.storagefile.service.StorageService;
import com.lyna.web.domain.storagefile.service.UploadDataService;
import com.lyna.web.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/upload")
public class FileUploadController {

    private final StorageService storageService;
    private final UploadDataService uploadDataService;

    @Autowired
    public FileUploadController(StorageService storageService, UploadDataService uploadDataService) {
        this.storageService = storageService;
        this.uploadDataService = uploadDataService;
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

    @PostMapping("/file/order")
    public ResponseEntity<Object> handleFileUpload(Model model, @RequestParam("file") MultipartFile file,
                                                   UsernamePasswordAuthenticationToken principal) throws IOException {
        User user = (User) principal.getPrincipal();
        Map<Integer, String> mapError = storageService.store(user, file, 1);
        String result = "ファイルは成功にアップロードされた";
        if (mapError.size() > 0) {
            model.addAttribute("messageError", mapError);
            return new ResponseEntity<>(mapError, HttpStatus.INTERNAL_SERVER_ERROR);
        } else
            DataUtils.putMapData(Constants.ENTITY_STATUS.IMPORT, result);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/file/delivery")
    public ResponseEntity<Object> handleFileUploadDelivery(Model model, @RequestParam("file") MultipartFile file,
                                                           UsernamePasswordAuthenticationToken principal) throws IOException {
        User user = (User) principal.getPrincipal();
        Map<Integer, String> mapError = storageService.store(user, file, 2);
        String result = "ファイルは成功にアップロードされた";
        if (mapError.size() > 0) {
            model.addAttribute("messageError", mapError);
            return new ResponseEntity<>(mapError, HttpStatus.INTERNAL_SERVER_ERROR);
        } else
            DataUtils.putMapData(Constants.ENTITY_STATUS.IMPORT, result);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/file/store")
    public ResponseEntity<Object> handleFileUploadStore(Model model, @RequestParam MultipartFile file,
                                                        UsernamePasswordAuthenticationToken principal) throws IOException {
        User user = (User) principal.getPrincipal();
        Map<Integer, String> mapError = uploadDataService.store(user, file, 3);
        String result = "ファイルは成功にアップロードされた";
        if (mapError.size() > 0) {
            model.addAttribute("messageError", mapError);
            return new ResponseEntity<>(mapError, HttpStatus.INTERNAL_SERVER_ERROR);
        } else
            DataUtils.putMapData(Constants.ENTITY_STATUS.IMPORT, result);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/file/product")
    public ResponseEntity<Object> handleFileUploadProduct(Model model, @RequestParam MultipartFile file,
                                                          UsernamePasswordAuthenticationToken principal) throws IOException {
        User user = (User) principal.getPrincipal();
        Map<Integer, String> mapError = uploadDataService.store(user, file, 4);
        String result = "ファイルは成功にアップロードされた";
        if (mapError.size() > 0) {
            model.addAttribute("messageError", mapError);
            return new ResponseEntity<>(mapError, HttpStatus.INTERNAL_SERVER_ERROR);
        } else
            DataUtils.putMapData(Constants.ENTITY_STATUS.IMPORT, result);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/file/package")
    public ResponseEntity<Object> handleFileUploadPackage(Model model, @RequestParam MultipartFile file,
                                                          UsernamePasswordAuthenticationToken principal) throws IOException {
        User user = (User) principal.getPrincipal();
        Map<Integer, String> mapError = uploadDataService.store(user, file, 5);
        String result = "ファイルは成功にアップロードされた";
        if (mapError.size() > 0) {
            model.addAttribute("messageError", mapError);
            return new ResponseEntity<>(mapError, HttpStatus.INTERNAL_SERVER_ERROR);
        } else
            DataUtils.putMapData(Constants.ENTITY_STATUS.IMPORT, result);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
