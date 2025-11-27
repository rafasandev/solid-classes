package com.example.solid_classes.core.product.repository.mongo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.solid_classes.core.product.model.mongo.ProductDocument;

@Repository
public interface ProductMongoRepository extends MongoRepository<ProductDocument, UUID> {

    List<ProductDocument> findByCompanyId(UUID companyId);

    List<ProductDocument> findByCategoryId(UUID categoryId);

    List<ProductDocument> findByAvailableTrue();

    @Query("{'productName': {$regex: ?0, $options: 'i'}}")
    List<ProductDocument> searchByName(String name);

    @Query("{'available': true, 'stockQuantity': {$gt: 0}}")
    List<ProductDocument> findAvailableWithStock();

    boolean existsByProductName(String productName);
}
