package com.project.SpringMVCProject.services;

import com.project.SpringMVCProject.models.ProductDocument;
import com.project.SpringMVCProject.repository.ProductDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductDocumentService {
    @Autowired
    private ProductDocumentRepository repository;

    public List<ProductDocument> findAllByTitle(String title){
        return repository.findByTitle(title);
    }
}
