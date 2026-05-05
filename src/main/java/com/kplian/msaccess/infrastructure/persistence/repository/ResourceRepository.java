package com.kplian.msaccess.infrastructure.persistence.repository;

import com.kplian.msaccess.domain.model.ResourceEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ResourceRepository implements PanacheRepository<ResourceEntity> {

    public Optional<ResourceEntity> findByIdOptional(UUID id) {
        return find("id = ?1 and deletedAt is null", id).firstResultOptional();
    }

    public Optional<ResourceEntity> findByCode(String code) {
        return find("code = ?1 and deletedAt is null", code).firstResultOptional();
    }

    public boolean existsByCode(String code) {
        return count("code = ?1 and deletedAt is null", code) > 0;
    }

    public List<ResourceEntity> findChildren(UUID parentId) {
        return find("resourceId = ?1 and deletedAt is null", parentId).list();
    }
}
