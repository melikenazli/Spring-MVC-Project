package com.project.SpringMVCProject.repository;

import com.project.SpringMVCProject.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByOrderByTitleAsc();
}
