package com.lyna.web.application;

import com.lyna.commons.utils.Constants;
import com.lyna.commons.utils.DataUtils;
import com.lyna.web.domain.storagefile.exeption.StorageFileNotFoundException;
import com.lyna.web.domain.storagefile.service.StorageService;
import com.lyna.web.domain.user.User;
import com.lyna.web.security.authorities.IsAdmin;
import org.apache.commons.csv.CSVRecord;
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
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/upload")
public class FileUploadController {
    private static final String MATCHING_ORDER = "matching/order";
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
    @IsAdmin
    public String handleFile(Model model, @RequestParam("file") MultipartFile file) throws IOException {
        Map<String, Integer> headerMap = storageService.getMapHeader(file);
        List<CSVRecord> mapData = storageService.getMapData(file);
        String fileName = storageService.store(file);
        Set<Iterator<String>> data = mapData.stream().map(CSVRecord::iterator).collect(Collectors.toSet());
        model.addAttribute("headerOrder", storageService.getMapHeader());
        model.addAttribute("fileName", fileName);
        if (mapData.size() > 0) {
            model.addAttribute("headerData", headerMap.keySet().toArray());
            model.addAttribute("mapData", data);
        }
        return MATCHING_ORDER;
    }

    @PostMapping("/file/order")
    @IsAdmin
    public ResponseEntity<Object> handleFileUpload(Model model, @RequestParam("fileName") String fileName, @RequestParam("selectedHeader") List<String> headerOrders,
                                                   UsernamePasswordAuthenticationToken principal) throws IOException {
        User user = (User) principal.getPrincipal();
        AtomicInteger index = new AtomicInteger();
        Map<Integer, String> mapHeader =
                headerOrders.stream().collect(
                        Collectors.toMap(s -> index.getAndIncrement(), s -> s, (oldV, newV) -> newV));
        Resource resource = storageService.loadAsResource(fileName);
        Map<Integer, String> mapError = storageService.store(user, fileName, resource.getInputStream(), mapHeader);
        String result = "ファイルは成功にアップロードされた";
        if (mapError.size() > 0) {
            model.addAttribute("messageError", mapError);
            return new ResponseEntity<>(mapError, HttpStatus.INTERNAL_SERVER_ERROR);
        } else
            DataUtils.putMapData(Constants.ENTITY_STATUS.IMPORT, result);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/file/delivery")
    @IsAdmin
    public ResponseEntity<Object> handleFileUploadDelivery(Model model, @RequestParam("file") MultipartFile file,
                                                           UsernamePasswordAuthenticationToken principal) throws IOException {
        User user = (User) principal.getPrincipal();
        Map<Integer, String> mapHeader = new HashMap<>();
        Map<Integer, String> mapError = storageService.store(user, file);
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
