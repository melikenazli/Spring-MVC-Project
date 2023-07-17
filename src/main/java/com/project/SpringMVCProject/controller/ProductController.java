package com.project.SpringMVCProject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.SpringMVCProject.models.Product;
import com.project.SpringMVCProject.models.ProductDocument;
import com.project.SpringMVCProject.services.ProductDocumentService;
import com.project.SpringMVCProject.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String createSearchBar(){
        return "form";
    }

    @PostMapping("/showGraph")
    public String showGraph(@RequestParam("title") String title, Model model){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        List<ProductDocument> products = productDocumentService.findAllByTitle(title);
        if(products.isEmpty()){
            model.addAttribute("err", "*There is no product with that title. Please try again.");
            return "form";
        }
        System.out.println(products);
        try {
            model.addAttribute("products", objectMapper.writeValueAsString(products));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "graph";
    }
}
