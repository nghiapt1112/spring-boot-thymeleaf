package com.lyna.web.application;

import com.lyna.commons.infrustructure.controller.AbstractCustomController;
import com.lyna.web.domain.order.service.OrderDetailService;
import com.lyna.web.domain.product.Product;
import com.lyna.web.domain.product.service.ProductService;
import com.lyna.web.domain.user.User;
import com.lyna.web.security.authorities.IsAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/product")
public class ProductController extends AbstractCustomController {
    private final Logger log = LoggerFactory.getLogger(StoreController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderDetailService orderDetailService;

    private String codeBeforeUpdate;

    @GetMapping(value = "/create")
    public String registerProduct(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "product/registerProduct";
    }

    @PostMapping(value = "/create")
    public String saveProduct(UsernamePasswordAuthenticationToken principal,
                              Model model, @Valid @ModelAttribute("product") Product product,
                              BindingResult result, RedirectAttributes redirect) {

        System.out.println(product.getPrice() + product.getCode());
        if (Objects.isNull(product)) {
            model.addAttribute("product", product);
            return "product/registerProduct";
        }

        if (result.hasErrors()) {
            model.addAttribute("product", product);
            return "product/registerProduct";
        }
        try {
            Product productIsCode = productService.findOneByCode(product.getCode());
            System.out.println(productIsCode.getCode());
            if (!Objects.isNull(productIsCode)) {
                model.addAttribute("errorCodeShow", "このコードは既に存在します。");
                model.addAttribute("product", product);
                return "product/registerProduct";
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        productService.createProduct(product, principal);
        return "redirect:/product/list";
    }

    @PostMapping(value = "/update")
    @IsAdmin
    public String updateProduct(UsernamePasswordAuthenticationToken principal, Model model, @Valid @ModelAttribute("product")
            Product product, BindingResult result, RedirectAttributes redirect) {

        if (Objects.isNull(product)) {
            model.addAttribute("product", product);
            return "product/editProduct";
        }
        if (result.hasErrors()) {
            model.addAttribute("product", product);
            return "product/editProduct";
        }

        try {

            Product productIsCode = productService.findOneByCode(product.getCode());
            if (!product.getCode().equals(codeBeforeUpdate) && !Objects.isNull(productIsCode)) {
                model.addAttribute("errorCodeShow", "このコードは既に存在します。");
                model.addAttribute("product", product);
                return "product/editProduct";
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }


        productService.updateProduct(product, principal);

        return "redirect:/product/list";

    }

    @GetMapping(value = "/list")
    public String listPackage(Model model, UsernamePasswordAuthenticationToken principal) {
        User currentUser = (User) principal.getPrincipal();
        int tenantId = currentUser.getTenantId();
        model.addAttribute("products", productService.findAll(tenantId));
        return "product/listProduct";
    }

    @GetMapping("/delete")
    public @ResponseBody
    String deletePackage(@RequestParam(value = "arrayProductId[]") List<String> listProductId) {
        if (!Objects.isNull(listProductId) && !listProductId.isEmpty()) {
            orderDetailService.deletebyProductId(listProductId);
            productService.deletebyProductId(listProductId);
            return "true";
        }
        return "false";
    }

    @GetMapping(value = "/update/{productId}")
    public String editPackage(@PathVariable("productId") String productId, Model model) {
        Product product = productService.findOneByProductId(productId);
        codeBeforeUpdate = product.getCode();
        model.addAttribute("product", product);
        return "product/editProduct";
    }

}
