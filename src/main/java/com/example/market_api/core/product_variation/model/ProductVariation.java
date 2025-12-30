package com.example.market_api.core.product_variation.model;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.mongodb.core.mapping.Document;

import com.example.market_api.common.base.AuditableMongoEntity;
import com.example.market_api.common.exception.BusinessRuleException;
import com.example.market_api.core.product_variation.model.enums.VariationCategoryType;
import com.example.market_api.core.product_variation.model.enums.VariationValueType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Document(collection = "product_variations")
@Getter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductVariation extends AuditableMongoEntity {

    @Setter
    private UUID productId;

    private UUID variationCategoryId;
    private VariationCategoryType variationCategoryType;
    private VariationValueType valueType;
    private String variationValue;
    private BigDecimal variationAdditionalPrice;
    private int stockQuantity;

    public boolean isAvailable() {
        return this.stockQuantity > 0;
    }

    public boolean hasStock(int requestedQuantity) {
        return this.stockQuantity >= requestedQuantity;
    }

    public void decreaseVariationStock(int quantity) {
        validQuantityPositive(quantity);
        if (this.stockQuantity < quantity)
            throw new BusinessRuleException("Estoque insuficiente na variação");
        this.stockQuantity -= quantity;
    }

    public void increaseVariationStock(int quantity) {
        validQuantityPositive(quantity);
        this.stockQuantity += quantity;
    }

    public void updateStockQuantity(int newQuantity) {
        if (newQuantity < 0) {
            throw new BusinessRuleException("Quantidade de estoque não pode ser negativa");
        }
        this.stockQuantity = newQuantity;
    }

    private void validQuantityPositive(int quantity) {
        if (quantity < 0)
            throw new BusinessRuleException("Quantidade de retirada não pode ser negativa");
    }
}