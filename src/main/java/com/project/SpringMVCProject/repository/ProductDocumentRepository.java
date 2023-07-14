package com.project.SpringMVCProject.repository;

import com.project.SpringMVCProject.models.ProductDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductDocumentRepository {
    @Autowired
    MongoTemplate mongoTemplate;

    public ProductDocument save(ProductDocument productDocument){
        return mongoTemplate.save(productDocument);
    }

    public List<ProductDocument> findByTitle(String title){
        Query query = new Query(Criteria.where("title").is(title));
        return mongoTemplate.find(query, ProductDocument.class); // 3. parametre olarak collection name de verilebiliyor
    }

    public void createCollection(String collectionName){
        mongoTemplate.createCollection(collectionName);
    }

    public boolean collectionExists(String collectionName){
        return mongoTemplate.collectionExists(collectionName);
    }
}
