package com.kplian.msaccess.domain.service;

import com.kplian.msaccess.api.service.I18nService;
import com.kplian.msaccess.domain.exception.I18nBusinessException;
import com.kplian.msaccess.domain.model.Profile;
import com.kplian.msaccess.infrastructure.persistence.repository.ProfileRepository;
import com.kplian.msaccess.util.UserContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Transactional
public class ProfileService {

    @Inject
    ProfileRepository profileRepository;

    @Inject
    I18nService i18nService;

    @Inject
    UserContext userContext;

    public List<Profile> findAll() {
        return profileRepository.find("deletedAt is null").list();
    }

    public Profile findById(UUID id) {
        return profileRepository.findByIdOptional(id)
            .orElseThrow(() -> new I18nBusinessException(
                i18nService,
                "error.profile.not_found",
                "PROFILE_NOT_FOUND",
                id
            ));
    }

    public Profile create(Profile profile) {
        validateProfile(profile);
        if (profileRepository.existsByCode(profile.getCode())) {
            throw new I18nBusinessException(
                i18nService,
                "error.profile.code.exists",
                "PROFILE_CODE_EXISTS",
                profile.getCode()
            );
        }
        profile.setAuditForCreate(getCurrentUser());
        profileRepository.persist(profile);
        return profile;
    }

    public Profile update(UUID id, Profile profile) {
        validateProfile(profile);
        Profile existing = findById(id);
        if (!existing.getCode().equals(profile.getCode()) && profileRepository.existsByCode(profile.getCode())) {
            throw new I18nBusinessException(
                i18nService,
                "error.profile.code.exists",
                "PROFILE_CODE_EXISTS",
                profile.getCode()
            );
        }
        existing.setCode(profile.getCode());
        existing.setName(profile.getName());
        existing.setModuleCode(profile.getModuleCode());
        existing.setVendorCode(profile.getVendorCode());
        existing.setAuditForUpdate(getCurrentUser());
        return existing;
    }

    public void delete(UUID id) {
        Profile profile = findById(id);
        profile.setAuditForDelete(getCurrentUser());
    }

    private void validateProfile(Profile profile) {
        if (profile == null) {
            throw new I18nBusinessException(
                i18nService,
                "error.profile.invalid",
                "INVALID_PROFILE"
            );
        }
    }

    private String getCurrentUser() {
        return userContext.getUserId();
    }
}
