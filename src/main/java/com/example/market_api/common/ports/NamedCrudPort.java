package com.example.market_api.common.ports;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityNotFoundException;

public interface NamedCrudPort<T> {
    // Métodos básicos de busca
    T getById(UUID id);
    
    Optional<T> findById(UUID id);
    
    List<T> findAll();
    
    Page<T> findAll(Pageable pageable);
    
    // Métodos de persistência
    T save(T entity);
    
    List<T> saveAll(Iterable<T> entities);
    
    // Métodos de remoção
    void deleteById(UUID id);
    
    void delete(T entity);
    
    void deleteAll(Iterable<T> entities);
    
    void deleteAll();
    
    // Métodos de verificação
    boolean existsById(UUID id);
    
    long count();
    
    // Tratamento de exceções
    EntityNotFoundException throwEntityNotFound();
}
