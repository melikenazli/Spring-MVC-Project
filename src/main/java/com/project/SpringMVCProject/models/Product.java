package com.project.SpringMVCProject.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;
    private Long productId;
    private Long price;
    private LocalDate date;
    private String title;
    private String brand;
    private String category;
    private String url;

    public  String toString(){
        return "Product [id=" + id+ ", productId="+productId + ", price=" + price + ", date=" +
                date + ", title=" + title + ", brand=" + brand + ", category=" +
                category + ", url=" + url + "]";
    }
}
