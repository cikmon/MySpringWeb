package com.app.cikmon.market.controller;

import com.app.cikmon.market.UrlUtil;
import com.app.cikmon.market.model.Message;
import com.app.cikmon.market.model.Product;
import com.app.cikmon.market.model.User;
import com.app.cikmon.market.service.ProductService;
import com.app.cikmon.market.service.UserService;
import com.app.cikmon.market.validator.UserValidator;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Controller
 *
 * @author cikmon
 * @version 1.0
 */
@RequestMapping("/products")
@Controller
public class MyController {
    private final Logger logger = LoggerFactory.getLogger(MyController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserValidator userValidator;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model uiModel) {
        logger.info("Listing products");

        List<Product> products = productService.findAll();
        uiModel.addAttribute("products", products);
        logger.info("No. of products: " + products.size());

        return "products/list";
    }

    @RequestMapping(value = "/cartList", method = RequestMethod.GET)
    public String listCart1(
            @RequestParam(value = "idJson", required = false) String idJson,
            Model uiModel, HttpServletRequest req) {
        logger.info("Listing carts get");
        String userName = idJson;
        logger.info(idJson);

        if(userName == null || "".equals(userName.trim())){
            userName = "Anonymous";
        }else {
            if (userName == null || "".equals(userName.trim())) {
                userName = "Anonymous";
            }
            JSONObject obj = new JSONObject();
            JSONArray ar = (JSONArray) JSONValue.parse(userName);

            List<Long> listId = new ArrayList<Long>();
            for (int i = 0; i < ar.size(); i++) {
                obj = (JSONObject) ar.get(i);
                String ree1 = obj.get("id").toString();
                listId.add(Long.parseLong(ree1));
            }
            List<Product> products = productService.iterable(listId);
            uiModel.addAttribute("products", products);
            logger.info("Products : " + products.toString());
        }
        return "products/cartList";
    }

    @RequestMapping(value ="/cartList" ,method = RequestMethod.POST)
    public String listCart(Model uiModel,HttpServletRequest req)  {
        logger.info("Listing cart post");

        String userName = req.getParameter("userName");
        if(userName == null || "".equals(userName.trim())){
            userName = "Anonymous";
        }else {
            JSONObject obj = new JSONObject();
            JSONArray ar = (JSONArray) JSONValue.parse(userName);

            List<Long> listId = new ArrayList<Long>();
            for (int i = 0; i < ar.size(); i++) {
                // listId.add((Long)ar.get(i));
                obj = (JSONObject) ar.get(i);
                String ree1 = obj.get("id").toString();
                listId.add(Long.parseLong(ree1));
            }
            List<Product> products = productService.iterable(listId);
            uiModel.addAttribute("products", products);
            logger.info("Products : " + products.toString());
        }
        return "redirect:/products/cartList";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, Model uiModel) {
        logger.info("Show product");
        Product product = productService.findById(id);
        uiModel.addAttribute("product", product);
        logger.info("ID. of product: " + product.getId());

        return "products/show";
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.POST)
    public String update(@Valid Product product, BindingResult bindingResult, Model uiModel,
                         HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes,
                         Locale locale) {
        logger.info("Updating product");
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("message", new Message("error",
                    messageSource.getMessage("product_save_fail", new Object[]{}, locale)));
            uiModel.addAttribute("product", product);
            return "products/update";
        }
        uiModel.asMap().clear();
        redirectAttributes.addFlashAttribute("message", new Message("success",
                messageSource.getMessage("product_save_success", new Object[]{}, locale)));
        productService.save(product);
        return "redirect:/products/" + UrlUtil.encodeUrlPathSegment(product.getId().toString(),
                httpServletRequest);
    }


    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        logger.info("Updating form product");

        uiModel.addAttribute("product", productService.findById(id));
        return "products/update";
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = RequestMethod.POST)
    public String create(@Valid Product product, BindingResult bindingResult, Model uiModel,
                         HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes,
                         Locale locale, @RequestParam(value="file", required=false) Part file) {
        logger.info("Creating product");
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("message", new Message("error",
                    messageSource.getMessage("product_save_fail", new Object[]{}, locale)));
            uiModel.addAttribute("product", product);
            return "products/create";
        }
        uiModel.asMap().clear();
        redirectAttributes.addFlashAttribute("message", new Message("success",
                messageSource.getMessage("product_save_success", new Object[]{}, locale)));

        logger.info("Product id: " + product.getId());

    // Process upload file
        if (file != null) {
        logger.info("File name:  " + file.getName());
        logger.info("File size: " + file.getSize());
        logger.info("File product type: " + file.getContentType());
        byte[] fileContent = null;
        try {
            InputStream inputStream = file.getInputStream();
            if (inputStream == null) logger.info("File inputstream is null");
            fileContent = IOUtils.toByteArray(inputStream);
            product.setPhoto(fileContent);
        } catch (IOException ex) {
            logger.error("Error saving uploaded file"); }
        product.setPhoto(fileContent);
    }
        productService.save(product);
        return "redirect:/products";
}

    @RequestMapping(value = "/photo/{id}", method = RequestMethod.GET)
    @ResponseBody
    public byte[] downloadPhoto(@PathVariable("id") Long id) {
        Product product = productService.findById(id);

        if (product.getPhoto() != null) {
            logger.info("Downloading photo for id: {} with size: {}", product.getId(), product.getPhoto().length); }
        return product.getPhoto();
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(params = "form", method = RequestMethod.GET)
    public String createForm(Model uiModel) {
        Product product= new Product();
        uiModel.addAttribute("product", product);
        return "products/create";
    }


    @PreAuthorize("isAnonymous()")
    @RequestMapping(value ="/registration" ,method = RequestMethod.POST)
    public String create(@Valid User user, BindingResult bindingResult, Model uiModel,
                         HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes,
                         Locale locale, @RequestParam(value="file", required=false) Part file) {
        logger.info("Creating user");

        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            logger.info("user errors");
            uiModel.addAttribute("message", new Message("error",
                    messageSource.getMessage("product_save_fail", new Object[]{}, locale)));
            uiModel.addAttribute("user", user);
            return "products/registration";
        }
        logger.info("No errors user");
        uiModel.asMap().clear();
        redirectAttributes.addFlashAttribute("message", new Message("success",
                messageSource.getMessage("product_save_success", new Object[]{}, locale)));

        logger.info(user.toString());

        userService.save(user);
        return "redirect:/products";
    }


    @PreAuthorize("isAnonymous()")
    @RequestMapping(value = "/registration", params = "form", method = RequestMethod.GET)
    public String registrationForm(Model uiModel){
        uiModel.addAttribute("user", new User());
        return "products/registration";
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/{id}", params="delete", method = RequestMethod.POST)
    public String delete(@PathVariable("id") Long id){
        logger.info("Delete product id: "+id);
        productService.delete(id);
        return "redirect:/products";
    }


}
