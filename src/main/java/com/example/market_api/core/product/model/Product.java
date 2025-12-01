package com.example.market_api.core.product.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.market_api.common.base.AuditableMongoEntity;
import com.example.market_api.core.product_variation.model.ProductVariation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Document(collection = "products")
@Getter
@SuperBuilder
@NoArgsConstructor
public class Product extends AuditableMongoEntity {

    @Indexed(unique = true)
    private String productName;

    private String description;

    private BigDecimal basePrice;

    private int stockQuantity;

    @Indexed
    private UUID companyId;

    @Indexed
    private UUID categoryId;

    private List<ProductVariation> variations;

    // ----------------- Product methods -----------------

    public void addVariation(ProductVariation variation) {
        if (this.variations == null) {
            this.variations = new ArrayList<>();
        }
        this.variations.add(variation);
        this.increaseProductStock(variation.getStockQuantity());
    }

    public void removeVariation(UUID variationId) {
        if (this.variations != null) {
            ProductVariation variation = findVariation(variationId);
            this.decreaseProductStock(variation.getStockQuantity());
            this.variations.remove(variation);
        }
    }

    public boolean productIsAvaiable() {
        return stockQuantity > 0;
    }

    public boolean productHasStock(int quantity) {
        return stockQuantity >= quantity;
    }

    private void decreaseProductStock(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }
        this.stockQuantity -= quantity;
        if (this.stockQuantity < 0) {
            this.stockQuantity = 0;
        }
    }

    private void increaseProductStock(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }
        this.stockQuantity += quantity;
    }

    // --------------- Product Variations methods -----------------

    public ProductVariation findVariation(UUID variationId) {
        return variations.stream()
                .filter(v -> v.getId().equals(variationId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Variação não encontrada"));
    }

    public void decreaseVariationStock(ProductVariation variation, int quantity) {
        decreaseVariationStockById(variation.getId(), quantity);
    }

    public void increaseVariationStock(ProductVariation variation, int quantity) {
        increaseVariationStock(variation.getId(), quantity);
    }

    public void decreaseVariationStockById(UUID variationId, int quantity) {
        ProductVariation variation = findVariation(variationId);

        if (quantity < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }

        if(variation.getStockQuantity() < quantity) {
            throw new IllegalArgumentException("Estoque insuficiente na variação");
        }

        variation.setStockQuantity(variation.getStockQuantity() - quantity);
        this.decreaseProductStock(quantity);
    }

    public void increaseVariationStock(UUID variationId, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }

        ProductVariation variation = findVariation(variationId);

        int variationNewStock = variation.getStockQuantity() + quantity;
        variation.setStockQuantity(variationNewStock);

        this.increaseProductStock(quantity);
    }

    public boolean variationIsAvaiable(UUID variationId) {
        ProductVariation variation = findVariation(variationId);
        return variation.isAvailable();
    }

    public BigDecimal calculateVariationPrice(UUID variationId) {
        ProductVariation variation = findVariation(variationId);
        return this.basePrice.add(variation.getVariationAdditionalPrice());
    }

}
