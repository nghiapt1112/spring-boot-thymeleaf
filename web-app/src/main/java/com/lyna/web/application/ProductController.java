package com.lyna.web.application;

import com.lyna.commons.infrustructure.controller.AbstractCustomController;
import com.lyna.web.domain.order.service.OrderDetailService;
import com.lyna.web.domain.product.Product;
import com.lyna.web.domain.product.service.ProductService;
import com.lyna.web.domain.user.User;
import com.lyna.web.security.authorities.IsAdmin;
import org.apache.commons.collections4.CollectionUtils;
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
    public String createProduct(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "product/registerProduct";
    }

    @PostMapping(value = "/create")
    public String create(UsernamePasswordAuthenticationToken principal,
                         Model model, @Valid @ModelAttribute("product") Product product,
                         BindingResult result, RedirectAttributes redirect) {

        if (Objects.isNull(product) || result.hasErrors()) {
            model.addAttribute("product", product);
            return "product/editProduct";
        }

        Product existedProduct = productService.findOneByCode(product.getCode());
        if (!Objects.isNull(existedProduct)) {
            model.addAttribute("errorCodeShow", "このコードは既に存在します。");
            model.addAttribute("product", product);
            return "product/registerProduct";
        }

        productService.create(product, principal);
        return "redirect:/product/list";
    }

    @PostMapping(value = "/update")
    @IsAdmin
    public String update(UsernamePasswordAuthenticationToken principal, Model model, @Valid @ModelAttribute("product")
            Product product, BindingResult result, RedirectAttributes redirect) {

        if (Objects.isNull(product) || result.hasErrors()) {
            model.addAttribute("product", product);
            return "product/editProduct";
        }

        Product existedProduct = productService.findOneByCode(product.getCode());
        if (!product.getCode().equals(codeBeforeUpdate) && !Objects.isNull(existedProduct)) {
            model.addAttribute("errorCodeShow", "このコードは既に存在します。");
            model.addAttribute("product", product);
            return "product/editProduct";
        }


        productService.update(product, principal);

        return "redirect:/product/list";

    }

    @GetMapping(value = "/list")
    public String listPackage(Model model, UsernamePasswordAuthenticationToken principal) {
        User currentUser = (User) principal.getPrincipal();
        model.addAttribute("products", productService.findByTenantId(currentUser.getTenantId()));
        return "product/listProduct";
    }

    @GetMapping("/delete")
    public @ResponseBody
    String deleteByProductIds(@RequestParam(value = "arrayProductId[]") List<String> listProductId) {
        if (!Objects.isNull(listProductId) && !CollectionUtils.isEmpty(listProductId)) {
            orderDetailService.deleteByProductId(listProductId);
            productService.deleteByProductIds(listProductId);
            return "true";
        }
        return "false";
    }

    @GetMapping(value = "/update/{productId}")
    public String updateProduct(@PathVariable("productId") String productId, Model model) {
        Product product = productService.findOneByProductId(productId);
        codeBeforeUpdate = product.getCode();
        model.addAttribute("product", product);
        return "product/editProduct";
    }

}
