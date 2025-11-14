package com.example.solid_classes.common.interfaces;

import java.util.UUID;

import jakarta.persistence.EntityNotFoundException;

public interface NamedCrudPort<T> {
    T getById(UUID id);

    T save(T entity);

    void deleteById(UUID id);

    EntityNotFoundException throwEntityNotFound();
}
