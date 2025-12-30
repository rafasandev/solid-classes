package com.example.market_api.core.presencial_cart.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.example.market_api.core.presencial_cart_item.dto.PresencialCartItemResponseDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PresencialCartResponseDto {
    private UUID id;
    private String companyName;
    private String clientName;
    private String clientDocument;
    private boolean finalized;
    private UUID orderId;
    private BigDecimal cartTotal;
    private LocalDateTime createdAt;
    private LocalDateTime finalizedAt;
    private List<PresencialCartItemResponseDto> items;
}
