package com.example.solid_classes.common.base;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.solid_classes.common.ports.NamedCrudPort;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NamedMongoAdapter<T, R extends MongoRepository<T, UUID>> implements NamedCrudPort<T> {

    protected final R repository;
    protected final String entityName;

    @Override
    public T getById(UUID id) {
        return repository.findById(id).orElseThrow(this::throwEntityNotFound);
    }

    @Override
    public Optional<T> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        long total = repository.count();
        List<T> content = repository.findAll();
        
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), content.size());
        
        List<T> pageContent = content.subList(start, end);
        return new PageImpl<>(pageContent, pageable, total);
    }

    @Override
    public T save(T entity) {
        return repository.save(entity);
    }

    @Override
    public List<T> saveAll(Iterable<T> entities) {
        return repository.saveAll(entities);
    }

    @Override
    public void delete(T entity) {
        repository.delete(entity);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteAll(Iterable<T> entities) {
        repository.deleteAll(entities);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public EntityNotFoundException throwEntityNotFound() {
        return new EntityNotFoundException(entityName + " não encontrado(a)");
    }
}
