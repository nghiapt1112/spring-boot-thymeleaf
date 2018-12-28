package com.lyna.web.application;

import com.lyna.commons.infrustructure.controller.AbstractCustomController;
import com.lyna.commons.utils.Constants;
import com.lyna.commons.utils.DataUtils;
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
import org.springframework.web.bind.annotation.*;

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
        User user = (User) principal.getPrincipal();
        if (Objects.isNull(product) || result.hasErrors()) {
            model.addAttribute("product", product);
            return PRODUCT_REGISTER_PAGE;
        }
        try {
            if (!Objects.isNull(productService.findOneByCodeAndTenantId(product.getCode(), user.getTenantId()))) {
                model.addAttribute("errorProductExitsted", "このコードは既に存在します。");
                model.addAttribute("product", product);
                return PRODUCT_REGISTER_PAGE;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        productService.create(product, user);
        DataUtils.putMapData(Constants.ENTITY_STATUS.CREATED, product.getProductId());
        return REDIRECT_PRODUCT_LIST_PAGE;
    }

    @PostMapping(value = "/update")
    @IsAdmin
    public String update(UsernamePasswordAuthenticationToken principal, Model model, @Valid @ModelAttribute("product")
            Product product, BindingResult result) {
        User user = (User) principal.getPrincipal();
        if (Objects.isNull(product) || result.hasErrors()) {
            model.addAttribute("product", product);
            return PRODUCT_EDIT_PAGE;
        }
        Product productExisted = productService.findOneByCodeAndTenantId(product.getCode(), user.getTenantId());
        try {
            if (!Objects.isNull(productExisted) && !productExisted.getCode().equals(product.getCode())) {
                model.addAttribute("errorProductExitsted", "このコードは既に存在します。");
                model.addAttribute("product", product);
                return PRODUCT_EDIT_PAGE;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        productService.update(product, user);
        DataUtils.putMapData(Constants.ENTITY_STATUS.UPDATED, product.getProductId());
        model.addAttribute("message", "成功に新規作成した");

        return REDIRECT_PRODUCT_LIST_PAGE;

    }

    @GetMapping(value = "/list")
    public String listPackage(Model model, UsernamePasswordAuthenticationToken principal) {

        User user = (User) principal.getPrincipal();
        model.addAttribute("products", productService.findByTenantIdAndTenantId(user.getTenantId()));
        model.addAttribute("message", DataUtils.getMapData());
        return PRODUCT_LIST_PAGE;
    }

    @GetMapping("/delete")
    public @ResponseBody
    String deleteByProductIds(@RequestParam(value = "ojectIds[]") List<String> productIds, UsernamePasswordAuthenticationToken principal) {
        User user = (User) principal.getPrincipal();
        if (!Objects.isNull(productIds) && !CollectionUtils.isEmpty(productIds)) {
            orderDetailService.deleteByProductIdsAndTenantId(productIds, user.getTenantId());
            DataUtils.putMapData(Constants.ENTITY_STATUS.DELETED, productIds.toString());
            return "true";
        }
        return "false";
    }

    @GetMapping(value = "/update/{productId}/{tenantId}")
    public String updateProduct(@PathVariable("productId") String productId, Model model,
                                @PathVariable("tenantId") int tenantId, UsernamePasswordAuthenticationToken principal) {
        model.addAttribute("product", productService.findOneByProductIdAndTenantId(productId, tenantId));
        return PRODUCT_EDIT_PAGE;

    }

}
