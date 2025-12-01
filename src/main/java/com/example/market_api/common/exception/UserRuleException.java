package com.example.market_api.common.exception;

public class UserRuleException extends RuntimeException {
    public UserRuleException(String message) {
        super(message);
    }

    // Opcional: Construtor com causa (para encadeamento de erros)
    public UserRuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
