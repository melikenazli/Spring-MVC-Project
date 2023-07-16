package com.project.SpringMVCProject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.SpringMVCProject.models.Product;
import com.project.SpringMVCProject.models.ProductDocument;
import com.project.SpringMVCProject.services.ProductDocumentService;
import com.project.SpringMVCProject.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDocumentService productDocumentService;

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

    @PostMapping("/showGraph")
    public String showGraph(@ModelAttribute("title") String title){
        System.out.println(title);
        /*ObjectMapper objectMapper = new ObjectMapper();
        List<ProductDocument> products = productDocumentService.findAllByTitle(title);
        System.out.println(products);
        try {
            model.addAttribute("products", objectMapper.writeValueAsString(products));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }*/
        return "graph";
    }
}
