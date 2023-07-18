package com.project.SpringMVCProject.repository;

import com.project.SpringMVCProject.models.ProductDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * This class uses MongoTemplate to create operations on MongoDB.
 */

@Repository
public class ProductDocumentRepository {
    @Autowired
    MongoTemplate mongoTemplate;

    public ProductDocument save(ProductDocument productDocument){
        return mongoTemplate.save(productDocument);
    }

    /**
     * Finds the products with the given title in MongoDB database.
     * @param title product title to search
     * @return all products with that title
     */
    public List<ProductDocument> findByTitle(String title){
        System.out.println(title);
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(title));
        return mongoTemplate.find(query, ProductDocument.class, "productDocument");
    }
}
