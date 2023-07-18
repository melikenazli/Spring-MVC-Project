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

/**
 * Contoller class to handle requests to view products list and search for a product to see its price change through dates.
 */
@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDocumentService productDocumentService;

    /**
     * Handles get requests to see the products list stored in MySQL database.
     * @param model Model object to hold products list as an attribute to pass to the products-list view
     * @return products-list view that displays products list on browser
     */
    @GetMapping("/products")
    public String listProducts(Model model){
        List<Product> products = productService.findAllProductsOrderedByTitle();
        model.addAttribute("products", products);
        return "products-list";
    }

    /**
     * Handles get requests to see search bar 
     * @return form view that displays the search bar to get product title from user
     */
    @GetMapping("/products/search")
    public String createSearchBar(){
        return "form";
    }

    /**
     * Handles post requests to see the graph of the product with the title that user entered
     * If there is no product with that title user stays in the search bar page and sees an error message
     * If there is no error user is forwarded to the graph.html where the graph for the product is displayed
     * @param title title name of the product entered by the user on search bar
     * @param model Model object to add error message to or the list of products found with that title
     * @return view 
     */
    @PostMapping("/showGraph")
    public String showGraph(@RequestParam("title") String title, Model model){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        List<ProductDocument> products = productDocumentService.findAllByTitle(title);
        if(products.isEmpty()){
            model.addAttribute("err", "*There is no product with that title. Please try again.");
            return "form";
        }
        //System.out.println(products);
        try {
            model.addAttribute("products", objectMapper.writeValueAsString(products));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "graph";
    }
}
