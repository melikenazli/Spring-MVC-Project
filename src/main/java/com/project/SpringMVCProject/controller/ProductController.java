package com.project.SpringMVCProject.controller;

import com.project.SpringMVCProject.models.Product;
import com.project.SpringMVCProject.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public String listProducts(Model model){
        List<Product> products = productService.findAllProductsOrderedByTitle();
        model.addAttribute("products", products);
        return "products-list";
    }

    @GetMapping("/products/search")
    public String createSearchBar(Model model){
        String title = new String();
        model.addAttribute("title", title);
        return "form";
    }
}
