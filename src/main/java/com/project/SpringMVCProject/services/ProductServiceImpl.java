package com.project.SpringMVCProject.services;

import com.project.SpringMVCProject.models.Product;
import com.project.SpringMVCProject.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductRepository repository;

    @Override
    public List<Product> findAllProductsOrderedByTitle() {
        List<Product> products = repository.findByOrderByTitleAsc();
        return products;
    }
}
