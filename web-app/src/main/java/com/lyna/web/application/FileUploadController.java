package com.lyna.web.application;

import com.lyna.commons.infrustructure.controller.AbstractCustomController;
import com.lyna.commons.utils.Constants;
import com.lyna.commons.utils.DataUtils;
import com.lyna.web.domain.storagefile.exeption.StorageFileNotFoundException;
import com.lyna.web.domain.storagefile.service.StorageService;
import com.lyna.web.domain.storagefile.service.UploadDataService;
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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/upload")
public class FileUploadController extends AbstractCustomController {
    private static final String MATCHING_ORDER = "matching/order";

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

    @PostMapping("/file")
    @IsAdmin
    public String handleFile(Model model, @RequestParam("file") MultipartFile file, @RequestParam("typeUploadFile") String typeUploadFile) {
        Map<String, Integer> headerMap = storageService.getMapHeader(file);
        List<CSVRecord> mapData = storageService.getMapData(file);
        String fileName = storageService.store(file);
        Set<Iterator<String>> data = mapData.stream().map(CSVRecord::iterator).collect(Collectors.toSet());
        model.addAttribute("headerOrder", storageService.getMapHeader());
        model.addAttribute("fileName", fileName);
        model.addAttribute("typeUpload", typeUploadFile);

        if (mapData.size() > 0) {
            model.addAttribute("headerData", headerMap.keySet().toArray());
            model.addAttribute("mapData", data);
        }
        return MATCHING_ORDER;
    }

    @PostMapping("/file/order")
    @IsAdmin
    public ResponseEntity<Object> handleFileUpload(Model model, @RequestParam("fileName") String fileName,
                                                   @RequestParam("selectedHeader") List<String> headerOrders,
                                                   @RequestParam("typeUploadFile") String typeUploadFile,
                                                   UsernamePasswordAuthenticationToken principal) throws IOException {
        User user = (User) principal.getPrincipal();
        AtomicInteger index = new AtomicInteger();
        Map<Integer, String> mapHeader =
                headerOrders.stream().collect(
                        Collectors.toMap(s -> index.getAndIncrement(), s -> s, (oldV, newV) -> newV));
        Resource resource = storageService.loadAsResource(fileName);
        Map<Integer, String> mapError = storageService.store(user, fileName, resource.getInputStream(), typeUploadFile, mapHeader);
        return getResponseMessage(model, mapError);
    }

    @PostMapping("/file/delivery")
    @IsAdmin
    public ResponseEntity<Object> handleFileUploadDelivery(Model model, @RequestParam("file") MultipartFile file,
                                                           @RequestParam("typeUploadFile") String typeUploadFile,
                                                           UsernamePasswordAuthenticationToken principal) throws IOException {
        User user = (User) principal.getPrincipal();
        Map<Integer, String> mapError = storageService.store(user, file, typeUploadFile);
        return getResponseMessage(model, mapError);
    }

    @PostMapping("/file/store")
    public ResponseEntity<Object> handleFileUploadStore(Model model, @RequestParam MultipartFile file,
                                                        @RequestParam("typeUploadFile") String typeUploadFile,
                                                        UsernamePasswordAuthenticationToken principal) throws IOException {
        User user = (User) principal.getPrincipal();
        Map<Integer, String> mapError = uploadDataService.store(user, file, 3, typeUploadFile);
        return getResponseMessage(model, mapError);
    }

    @PostMapping("/file/product")
    public ResponseEntity<Object> handleFileUploadProduct(Model model, @RequestParam MultipartFile file,
                                                          @RequestParam("typeUploadFile") String typeUploadFile,
                                                          UsernamePasswordAuthenticationToken principal) throws IOException {
        User user = (User) principal.getPrincipal();
        Map<Integer, String> mapError = uploadDataService.store(user, file, 4, typeUploadFile);
        return getResponseMessage(model, mapError);
    }

    @PostMapping("/file/package")
    public ResponseEntity<Object> handleFileUploadPackage(Model model, @RequestParam MultipartFile file,
                                                          @RequestParam("typeUploadFile") String typeUploadFile,
                                                          UsernamePasswordAuthenticationToken principal) throws IOException {
        User user = (User) principal.getPrincipal();
        Map<Integer, String> mapError = uploadDataService.store(user, file, 5, typeUploadFile);
        return getResponseMessage(model, mapError);
    }

    private ResponseEntity<Object> getResponseMessage(Model model, Map<Integer, String> mapError) {
        String result = toStr("file.uploadSuccess");
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
