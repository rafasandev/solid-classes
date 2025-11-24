package com.example.solid_classes.core.variation_category.model.variation_global;

import java.util.List;

import com.example.solid_classes.core.category.model.Category;
import com.example.solid_classes.core.variation_category.model.VariationCategoryEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "global_variations")
@Getter
@SuperBuilder
@NoArgsConstructor
public class VariationCategoryGlobal extends VariationCategoryEntity {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "category_global_variations_mapping", joinColumns = @JoinColumn(name = "global_variation_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories;

    public void addCategory(Category category) {
        if (category != null && !this.categories.contains(category))
            this.categories.add(category);
    }

    public void removeCategory(Category category) {
        if (category != null && this.categories.contains(category))
            this.categories.remove(category);
    }
}
