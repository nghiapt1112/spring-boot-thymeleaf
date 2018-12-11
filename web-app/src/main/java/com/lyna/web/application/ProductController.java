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

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/product")
public class ProductController extends AbstractCustomController {
    private static final String PRODUCT_LIST_PAGE = "product/listProduct";
    private static final String PRODUCT_EDIT_PAGE = "product/editProduct";
    private static final String REDIRECT_PRODUCT_LIST_PAGE = "redirect:/product/list";
    private static final String PRODUCT_REGISTER_PAGE = "product/registerProduct";
    private final Logger log = LoggerFactory.getLogger(StoreController.class);
    @Autowired
    private ProductService productService;

    @Autowired
    private OrderDetailService orderDetailService;

    private String codeExisted;


    @GetMapping(value = "/create")
    public String createProduct(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return PRODUCT_REGISTER_PAGE;
    }

    @PostMapping(value = "/create")
    public String create(UsernamePasswordAuthenticationToken principal,
                         Model model, @Valid @ModelAttribute("product") Product product,
                         BindingResult result) {

        if (Objects.isNull(product) || result.hasErrors()) {
            model.addAttribute("product", product);
            return PRODUCT_REGISTER_PAGE;
        }
        try {
            if (!Objects.isNull(productService.findOneByCode(product.getCode()))) {
                model.addAttribute("errorCodeShow", "このコードは既に存在します。");
                model.addAttribute("product", product);
                return PRODUCT_REGISTER_PAGE;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }


        productService.create(product, principal);
        return REDIRECT_PRODUCT_LIST_PAGE;
    }

    @PostMapping(value = "/update")
    @IsAdmin
    public String update(UsernamePasswordAuthenticationToken principal, Model model, @Valid @ModelAttribute("product")
            Product product, BindingResult result) {

        if (Objects.isNull(product) || result.hasErrors()) {
            model.addAttribute("product", product);
            return PRODUCT_EDIT_PAGE;
        }

        try {
            if (!Objects.isNull(productService.findOneByCode(product.getCode()))) {
                model.addAttribute("errorCodeShow", "このコードは既に存在します。");
                model.addAttribute("product", product);
                return PRODUCT_EDIT_PAGE;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        productService.update(product, principal);

        return REDIRECT_PRODUCT_LIST_PAGE;

    }

    @GetMapping(value = "/list")
    public String listPackage(Model model, UsernamePasswordAuthenticationToken principal) {
        User currentUser = (User) principal.getPrincipal();
        model.addAttribute("products", productService.findByTenantId(currentUser.getTenantId()));
        return PRODUCT_LIST_PAGE;
    }

    @GetMapping("/delete")
    public @ResponseBody
    String deleteByProductIds(@RequestParam(value = "productIds[]") List<String> productIds) {
        if (!Objects.isNull(productIds) && !CollectionUtils.isEmpty(productIds)) {
            orderDetailService.deleteByProductIds(productIds);
            return "true";
        }
        return "false";
    }

    @GetMapping(value = "/update/{productId}")
    public String updateProduct(@PathVariable("productId") String productId, Model model) {
        Product product = productService.findOneByProductId(productId);
        codeExisted = product.getCode();
        model.addAttribute("product", product);
        return PRODUCT_EDIT_PAGE;

    }

}
