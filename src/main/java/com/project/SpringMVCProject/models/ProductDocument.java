package com.project.SpringMVCProject.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

/**
 * Model class for products to be hold as documents inside productDocument collection in MongoDB.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class ProductDocument {
    private String title;
    private List<Long> prices;
    private List<LocalDate> dates;
    private String website;

}
