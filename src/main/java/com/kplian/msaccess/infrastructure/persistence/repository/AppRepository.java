package com.kplian.msaccess.infrastructure.persistence.repository;

import com.kplian.msaccess.domain.model.App;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class AppRepository implements PanacheRepository<App> {

    public Optional<App> findByIdOptional(UUID id) {
        return find("id = ?1 and deletedAt is null", id).firstResultOptional();
    }

    public Optional<App> findByCode(String code) {
        return find("code = ?1 and deletedAt is null", code).firstResultOptional();
    }

    public boolean existsByCode(String code) {
        return count("code = ?1 and deletedAt is null", code) > 0;
    }
}
