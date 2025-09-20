package io.github.djordjije11.reeledlegacy.service;

import io.github.djordjije11.reeledlegacy.commons.exception.NotFoundException;
import io.github.djordjije11.reeledlegacy.model.AuthorType;
import io.github.djordjije11.reeledlegacy.repository.AuthorTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author Djordjije Radovic
 */
@RequiredArgsConstructor
@Service
public class AuthorTypeQueryService {

    private final AuthorTypeRepository authorTypeRepository;

    public Set<AuthorType> getAll() {
        return authorTypeRepository.findAll();
    }

    public AuthorType get(Long id) {
        return authorTypeRepository.findById(id).orElseThrow(() -> new NotFoundException("Author type does not exist (id: %s)".formatted(id)));
    }
}
