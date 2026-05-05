package com.kplian.msaccess.infrastructure.persistence.repository;

import com.kplian.msaccess.domain.model.ProfileRole;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ProfileRoleRepository implements PanacheRepository<ProfileRole> {

    public Optional<ProfileRole> findByIdOptional(UUID id) {
        return find("id = ?1 and deletedAt is null", id).firstResultOptional();
    }

    public List<ProfileRole> findByProfileId(UUID profileId) {
        return find("profileId = ?1 and deletedAt is null", profileId).list();
    }
}
