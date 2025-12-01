package com.example.market_api.core.service_offering.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.market_api.common.base.AuditableMongoEntity;
import com.example.market_api.core.service_offering.model.enums.ServiceModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@Document(collection = "services")
public class ServiceOffering extends AuditableMongoEntity {

    @Indexed(unique = true)
    private String serviceName;

    private String description;

    private BigDecimal price;

    private Integer durationMinutes;

    private Boolean available;

    private ServiceModel model;

    private List<Integer> weekDaysAvailable;

    private Map<Integer, List<TimeRange>> dailyTimeRanges;

    @Indexed
    private UUID companyId;

    @Indexed
    private UUID categoryId;

    public void activate() {
        this.available = true;
    }

    public void deactivate() {
        this.available = false;
    }

    public boolean isAvailable() {
        return available != null && available;
    }

    public void updateDetails(String serviceName, String description, BigDecimal price, Integer durationMinutes) {
        if (serviceName != null) {
            this.serviceName = serviceName;
        }
        if (description != null) {
            this.description = description;
        }
        if (price != null) {
            this.price = price;
        }
        if (durationMinutes != null) {
            this.durationMinutes = durationMinutes;
        }
    }
}
