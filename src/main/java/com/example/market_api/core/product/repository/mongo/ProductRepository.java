package com.example.market_api.core.product.repository.mongo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.market_api.core.product.model.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, UUID> {

    List<Product> findByCompanyId(UUID companyId);

    List<Product> findByCategoryId(UUID categoryId);

    @Query("{'productName': {$regex: ?0, $options: 'i'}}")
    List<Product> searchByName(String name);

    @Query("{'available': true, 'stockQuantity': {$gt: 0}}")
    List<Product> findAvailableWithStock();

    boolean existsByProductName(String productName);
}
