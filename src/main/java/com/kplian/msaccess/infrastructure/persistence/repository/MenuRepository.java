package com.kplian.msaccess.infrastructure.persistence.repository;

import com.kplian.msaccess.domain.model.Menu;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class MenuRepository implements PanacheRepository<Menu> {

    public Optional<Menu> findByIdOptional(UUID id) {
        return find("from Menu m left join fetch m.app where m.id = ?1 and m.deletedAt is null", id).firstResultOptional();
    }

    public Optional<Menu> findByCode(String code) {
        return find("code = ?1 and deletedAt is null", code).firstResultOptional();
    }

    public boolean existsByCode(String code) {
        return count("code = ?1 and deletedAt is null", code) > 0;
    }

    public List<Menu> findByAppId(UUID appId) {
        return find("from Menu m left join fetch m.app where m.app.id = ?1 and m.deletedAt is null", appId).list();
    }

    public List<Menu> findAllWithApp() {
        return find("from Menu m left join fetch m.app where m.deletedAt is null").list();
    }
}
