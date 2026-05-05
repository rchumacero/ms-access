package com.kplian.msaccess.infrastructure.persistence.repository;

import com.kplian.msaccess.domain.model.Interin;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class InterinRepository implements PanacheRepository<Interin> {

    public Optional<Interin> findByIdOptional(UUID id) {
        return find("id = ?1 and deletedAt is null", id).firstResultOptional();
    }
}
