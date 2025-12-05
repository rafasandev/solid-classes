package com.example.market_api.core.product.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
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

    private int totalStockCache;

    @Indexed
    private UUID companyId;

    @Indexed
    private UUID categoryId;

    @DBRef(lazy = true)
    private List<ProductVariation> variations;

    // Referência textual do ponto físico (ex.: "Biblioteca - Mesa 12")
    private String locationReference;

    // ----------------- Product methods -----------------

    public void addVariation(ProductVariation variation) {
        validatePersistedVariation(variation);
        ensureVariationListInitialized();

        boolean alreadyLinked = this.variations.stream()
                .anyMatch(existing -> existing.getId().equals(variation.getId()));

        if (!alreadyLinked) {
            this.variations.add(variation);
        }

        recalculateTotalStock();
    }

    public void removeVariation(UUID variationId) {
        if (variationId == null || this.variations == null) {
            return;
        }

        boolean removed = this.variations.removeIf(variation -> variationId.equals(variation.getId()));
        if (removed) {
            recalculateTotalStock();
        }
    }

    public boolean productIsAvaiable() {
        return totalStockCache > 0;
    }

    public boolean productHasStock(int quantity) {
        return totalStockCache >= quantity;
    }

    public int getStockQuantity() {
        return totalStockCache;
    }

    public void recalculateTotalStock() {
        if (this.variations == null || this.variations.isEmpty()) {
            this.totalStockCache = 0;
            return;
        }

        this.totalStockCache = this.variations.stream()
                .mapToInt(ProductVariation::getStockQuantity)
                .sum();
    }

    private void decreaseProductStock(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }
        this.totalStockCache -= quantity;
        if (this.totalStockCache < 0) {
            this.totalStockCache = 0;
        }
    }

    private void increaseProductStock(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }
        this.totalStockCache += quantity;
    }

    // --------------- Product Variations methods -----------------

    public ProductVariation findVariation(UUID variationId) {
        if (this.variations == null) {
            throw new IllegalArgumentException("Produto não possui variações carregadas");
        }

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
        recalculateTotalStock();
    }

    public void increaseVariationStock(UUID variationId, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }

        ProductVariation variation = findVariation(variationId);

        int variationNewStock = variation.getStockQuantity() + quantity;
        variation.setStockQuantity(variationNewStock);

        this.increaseProductStock(quantity);
        recalculateTotalStock();
    }

    public boolean variationIsAvaiable(UUID variationId) {
        ProductVariation variation = findVariation(variationId);
        return variation.isAvailable();
    }

    public BigDecimal calculateVariationPrice(UUID variationId) {
        ProductVariation variation = findVariation(variationId);
        return this.basePrice.add(variation.getVariationAdditionalPrice());
    }

    private void ensureVariationListInitialized() {
        if (this.variations == null) {
            this.variations = new ArrayList<>();
        }
    }

    private void validatePersistedVariation(ProductVariation variation) {
        if (variation == null || variation.getId() == null) {
            throw new IllegalArgumentException("Variação precisa estar persistida antes de ser vinculada");
        }
    }
}
