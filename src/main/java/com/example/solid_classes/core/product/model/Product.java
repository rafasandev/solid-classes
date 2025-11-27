package com.example.solid_classes.core.product.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.solid_classes.common.base.AuditableMongoEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Document(collection = "products")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Product extends AuditableMongoEntity {

    @Indexed(unique = true)
    private String productName;

    private String description;

    private BigDecimal basePrice;

    private boolean available;

    private int stockQuantity;

    @Indexed
    private UUID companyId;

    @Indexed
    private UUID categoryId;

    private List<ProductVariationEmbedded> variations;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ProductVariationEmbedded {
        private UUID variationId; // ID único da variação
        private String categoryName; // Ex: "Tamanho", "Cor"
        private String value; // Ex: "Grande", "Vermelho"
        private BigDecimal additionalPrice;
        private int stockQuantity;
        private boolean available;
    }

    public void addVariation(ProductVariationEmbedded variation) {
        if (this.variations == null) {
            this.variations = new ArrayList<>();
        }
        this.variations.add(variation);
    }

    public void removeVariation(UUID variationId) {
        if (this.variations != null) {
            this.variations.removeIf(v -> v.getVariationId().equals(variationId));
        }
    }

    public ProductVariationEmbedded findVariationById(UUID variationId) {
        if (this.variations == null) return null;
        return this.variations.stream()
            .filter(v -> v.getVariationId().equals(variationId))
            .findFirst()
            .orElse(null);
    }

    public boolean hasStock(int quantity) {
        return this.available && this.stockQuantity >= quantity;
    }

    public void decreaseStock(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }
        this.stockQuantity -= quantity;
        if (this.stockQuantity < 0) {
            this.stockQuantity = 0;
        }
    }

    public void increaseStock(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }
        this.stockQuantity += quantity;
    }
}
