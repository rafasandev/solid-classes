package com.example.solid_classes.common.abs;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.persistence.EntityNotFoundException;

public abstract class CrudService<T, R extends JpaRepository<T, UUID>> {

    @Autowired
    protected R repository;
    protected String entityName = "Entidade";

    protected abstract String getEntityName();

    public T getById(UUID id) {
        T entity = repository.findById(id).orElseThrow(this::throwEntityNotFound);
        return entity;
    }

    public T save(T entity) {
        T newEntity = repository.save(entity);
        return newEntity;
    }

    protected EntityNotFoundException throwEntityNotFound() {
        return new EntityNotFoundException(this.entityName + " não encontrado(a)");
    }
}
