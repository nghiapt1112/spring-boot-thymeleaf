package com.lyna.web.application;

import com.lyna.commons.infrustructure.controller.AbstractCustomController;
import com.lyna.commons.utils.Constants;
import com.lyna.commons.utils.DataUtils;
import com.lyna.web.domain.reader.service.ReaderDeliveryService;
import com.lyna.web.domain.reader.service.ReaderOrderService;
import com.lyna.web.domain.storagefile.exeption.StorageFileNotFoundException;
import com.lyna.web.domain.storagefile.service.StorageDeliveryService;
import com.lyna.web.domain.storagefile.service.StorageService;
import com.lyna.web.domain.storagefile.service.UploadDataService;
import com.lyna.web.domain.user.User;
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
    private final StorageDeliveryService storageDeliveryService;
    private final ReaderDeliveryService readerDeliveryService;
    private final ReaderOrderService readerOrderService;

    @Autowired
    public FileUploadController(StorageService storageService,
                                UploadDataService uploadDataService,
                                StorageDeliveryService storageDeliveryService,
                                ReaderDeliveryService readerDeliveryService, ReaderOrderService readerOrderService) {
        this.storageService = storageService;
        this.uploadDataService = uploadDataService;
        this.storageDeliveryService = storageDeliveryService;
        this.readerDeliveryService = readerDeliveryService;
        this.readerOrderService = readerOrderService;
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
    public String handleFile(Model model, @RequestParam("file") MultipartFile file, @RequestParam("typeUploadFile") String typeUploadFile) {
        int extensionFile = storageService.getExtension(file);
        Map<String, Integer> headerMap = storageService.getMapHeader(file, extensionFile);
        String fileName = storageService.storeFileServer(file);

        if (extensionFile == Constants.FILE_EXTENSION.CSV) {
            List<CSVRecord> mapData = storageService.getMapData(file);
            Set<Iterator<String>> data = mapData.stream().map(CSVRecord::iterator).collect(Collectors.toSet());
            if (mapData.size() > 0) {
                model.addAttribute("mapData", data);
            }
        }
        /*else if (extensionFile == Constants.FILE_EXTENSION.EXCEL) {
            Set<Iterator<String>> data = readerOrderService.getMapData(file);
            if (data.size() > 0) {
                model.addAttribute("mapData", data);
            }
        }*/

        model.addAttribute("headerOrder", storageService.getMapHeader());
        model.addAttribute("fileName", fileName);
        model.addAttribute("typeUpload", typeUploadFile);
        model.addAttribute("headerData", headerMap.keySet().toArray());

        return MATCHING_ORDER;
    }

    @PostMapping("/file/order")
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
        Map<Integer, String> mapError = storageService.checkDataAndCreateOrderWithGetDataAI(user, fileName, resource.getInputStream(), typeUploadFile, mapHeader);
        return getResponseMessage(model, mapError);
    }

    @PostMapping("/file/delivery")
    public ResponseEntity<Object> handleFileUploadDelivery(Model model, @RequestParam("file") MultipartFile file,
                                                           @RequestParam("typeUploadFile") String typeUploadFile,
                                                           UsernamePasswordAuthenticationToken principal) throws IOException {
        User user = (User) principal.getPrincipal();
        Map<Integer, String> mapError;
        int extensionFile = storageDeliveryService.getExtension(file);
        if (extensionFile == Constants.FILE_EXTENSION.CSV)
            mapError = storageDeliveryService.store(user, file, typeUploadFile);
        else
            mapError = readerDeliveryService.readAndSaveDataDelivery(user, file, typeUploadFile);

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
