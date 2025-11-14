package com.example.solid_classes.common.classes;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.solid_classes.common.interfaces.NamedCrudPort;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NamedCrudAdapter<T, R extends JpaRepository<T, UUID>> implements NamedCrudPort<T> {

    protected final R repository;
    protected final String entityName;

    @Override
    public T getById(UUID id) {
        return repository.findById(id).orElseThrow(this::throwEntityNotFound);
    }

    @Override
    public T save(T entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public EntityNotFoundException throwEntityNotFound() {
        return new EntityNotFoundException(this.entityName + " não encontrado(a)");
    }
}
