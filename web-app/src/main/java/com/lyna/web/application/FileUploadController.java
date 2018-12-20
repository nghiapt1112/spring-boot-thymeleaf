package com.lyna.web.application;

import com.lyna.web.domain.storagefile.exeption.StorageException;
import com.lyna.web.domain.storagefile.service.StorageService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/upload")
public class FileUploadController {
    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
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

    @PostMapping("/file")
    public ResponseEntity<Object> handleFileUpload(Model model, @RequestParam("file") MultipartFile file,
                                                   UsernamePasswordAuthenticationToken principal) throws IOException {
        User user = (User) principal.getPrincipal();
        int tenantId = user.getTenantId();
        Set<StorageException> mapError = storageService.store(tenantId, file, 1);

        if (mapError.size() > 0) {
            model.addAttribute("messageError", mapError);
            return new ResponseEntity<>(mapError, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            List<String> results = new ArrayList<>();
            results.add("ファイルは成功にアップロードされた");
            return new ResponseEntity<>(results, HttpStatus.OK);
        }
    }

    private ResponseEntity<Object> getObjectResponseEntity(Model model, Set<StorageException> mapError) {
        if (mapError.size() > 0) {
            model.addAttribute("messageError", mapError);
            return new ResponseEntity<>(mapError, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            List<String> results = new ArrayList<>();
            results.add("ファイルは成功にアップロードされた");
            return new ResponseEntity<>(results, HttpStatus.OK);
        }
    }

    @PostMapping("/fileDelivery")
    public ResponseEntity<Object> handleFileUploadDelivery(Model model, @RequestParam("file") MultipartFile file,
                                                           UsernamePasswordAuthenticationToken principal) throws IOException {
        User user = (User) principal.getPrincipal();
        int tenantId = user.getTenantId();
        Set<StorageException> mapError = storageService.store(tenantId, file, 2);

        return getObjectResponseEntity(model, mapError);
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageException exc) {
        return ResponseEntity.notFound().build();
    }
}
