package com.example.market_api.core.order.service;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

import com.example.market_api.common.exception.UserRuleException;

import lombok.RequiredArgsConstructor;

/**
 * Componente responsável por gerar códigos únicos de retirada.
 * Segue SRP - responsabilidade única de geração de códigos.
 */
@Component
@RequiredArgsConstructor
public class PickupCodeGenerator {

    private static final String CHAR_POOL = "23456789ACDEFGHJKMNPQRSTUVWXYZ";
    private static final int CODE_LENGTH = 5;
    private static final int MAX_ATTEMPTS = 10;
    
    private final OrderService orderService;
    private final SecureRandom random = new SecureRandom();

    /**
     * Gera um código único de retirada no formato #XXXXX.
     * 
     * @return Código único de retirada
     * @throws UserRuleException se não conseguir gerar código único após MAX_ATTEMPTS tentativas
     */
    public String generateUniqueCode() {
        int attempts = 0;
        String code;

        do {
            code = buildCode();
            attempts++;
            
            if (attempts >= MAX_ATTEMPTS) {
                throw new UserRuleException(
                    "Não foi possível gerar código único de retirada. Tente novamente."
                );
            }
        } while (orderService.existsByPickupCode(code));

        return code;
    }

    private String buildCode() {
        StringBuilder codeBuilder = new StringBuilder(CODE_LENGTH + 1);
        codeBuilder.append("#");
        
        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomIndex = random.nextInt(CHAR_POOL.length());
            codeBuilder.append(CHAR_POOL.charAt(randomIndex));
        }
        
        return codeBuilder.toString();
    }
}
