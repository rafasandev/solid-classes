package com.example.market_api.common.exception;

public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String message) {
        super(message);
    }

    // Opcional: Construtor com causa (para encadeamento de erros)
    public BusinessRuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
