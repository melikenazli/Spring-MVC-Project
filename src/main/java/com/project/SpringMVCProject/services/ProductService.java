package com.project.SpringMVCProject.services;

import com.project.SpringMVCProject.models.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAllProductsOrderedByTitle(); //read



}
