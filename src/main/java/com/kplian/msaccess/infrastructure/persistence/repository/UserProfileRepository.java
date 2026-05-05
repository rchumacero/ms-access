package com.kplian.msaccess.infrastructure.persistence.repository;

import com.kplian.msaccess.domain.model.UserProfile;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserProfileRepository implements PanacheRepository<UserProfile> {

    public Optional<UserProfile> findByIdOptional(UUID id) {
        return find("id = ?1 and deletedAt is null", id).firstResultOptional();
    }

    public List<UserProfile> findByUserCode(String userCode) {
        return find("userCode = ?1 and deletedAt is null", userCode).list();
    }

    public Optional<UserProfile> findByUserCodeAndProfileId(String userCode, UUID profileId) {
        return find("userCode = ?1 and profileId = ?2 and deletedAt is null", userCode, profileId).firstResultOptional();
    }
}
