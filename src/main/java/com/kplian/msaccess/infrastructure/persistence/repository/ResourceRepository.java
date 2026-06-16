package com.kplian.msaccess.infrastructure.persistence.repository;

import com.kplian.msaccess.domain.model.Resource;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ResourceRepository implements PanacheRepository<Resource> {

    public Optional<Resource> findByIdOptional(UUID id) {
        return find("from Resource r left join fetch r.menu where r.id = ?1 and r.deletedAt is null", id).firstResultOptional();
    }

    public Optional<Resource> findByCode(String code) {
        return find("code = ?1 and deletedAt is null", code).firstResultOptional();
    }

    public boolean existsByCode(String code) {
        return count("code = ?1 and deletedAt is null", code) > 0;
    }

    public List<Resource> findChildren(UUID parentId) {
        return find("from Resource r left join fetch r.menu where r.resourceId = ?1 and r.deletedAt is null", parentId).list();
    }

    public List<Resource> findAllWithMenu() {
        return find("from Resource r left join fetch r.menu where r.deletedAt is null").list();
    }

    public List<Resource> findByMenuCode(String menuCode) {
        return find("from Resource r left join fetch r.menu where r.menu.code = ?1 and r.deletedAt is null", menuCode).list();
    }
}
