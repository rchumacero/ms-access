package com.kplian.msaccess.domain.service;

import com.kplian.msaccess.api.service.I18nService;
import com.kplian.msaccess.domain.exception.I18nBusinessException;
import com.kplian.msaccess.domain.model.Profile;
import com.kplian.msaccess.domain.model.UserProfile;
import com.kplian.msaccess.infrastructure.persistence.repository.ProfileRepository;
import com.kplian.msaccess.infrastructure.persistence.repository.UserProfileRepository;
import com.kplian.msaccess.util.UserContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Transactional
public class UserProfileService {

    @Inject
    UserProfileRepository userProfileRepository;

    @Inject
    ProfileRepository profileRepository;

    @Inject
    I18nService i18nService;

    @Inject
    UserContext userContext;

    public List<UserProfile> findAll() {
        return userProfileRepository.find("deletedAt is null").list();
    }

    public UserProfile findById(UUID id) {
        return userProfileRepository.findByIdOptional(id)
            .orElseThrow(() -> new I18nBusinessException(
                i18nService,
                "error.user_profile.not_found",
                "USER_PROFILE_NOT_FOUND",
                id
            ));
    }

    public List<Profile> findProfilesByUserCode(String userCode) {
        List<UserProfile> userProfiles = userProfileRepository.findByUserCode(userCode);
        List<UUID> profileIds = userProfiles.stream()
            .map(UserProfile::getProfileId)
            .toList();
        if (profileIds.isEmpty()) {
            return List.of();
        }
        return profileRepository.find("id in ?1 and deletedAt is null", profileIds).list();
    }

    public UserProfile create(UserProfile userProfile) {
        validateUserProfile(userProfile);
        validateProfileExists(userProfile.getProfileId());
        userProfile.setAuditForCreate(getCurrentUser());
        userProfileRepository.persist(userProfile);
        return userProfile;
    }

    public UserProfile update(UUID id, UserProfile userProfile) {
        validateUserProfile(userProfile);
        validateProfileExists(userProfile.getProfileId());
        UserProfile existing = findById(id);
        existing.setUserCode(userProfile.getUserCode());
        existing.setProfileId(userProfile.getProfileId());
        existing.setValidFrom(userProfile.getValidFrom());
        existing.setValidTo(userProfile.getValidTo());
        existing.setAuditForUpdate(getCurrentUser());
        return existing;
    }

    public void delete(UUID id) {
        UserProfile userProfile = findById(id);
        userProfile.setAuditForDelete(getCurrentUser());
    }

    private void validateUserProfile(UserProfile userProfile) {
        if (userProfile == null) {
            throw new I18nBusinessException(
                i18nService,
                "error.user_profile.invalid",
                "INVALID_USER_PROFILE"
            );
        }
    }

    private void validateProfileExists(UUID profileId) {
        profileRepository.findByIdOptional(profileId)
            .orElseThrow(() -> new I18nBusinessException(
                i18nService,
                "error.profile.not_found",
                "PROFILE_NOT_FOUND",
                profileId
            ));
    }

    public void createByArray(com.kplian.msaccess.api.dto.request.UserProfileArrayRequestDTO request) {
        for (String profileCode : request.getProfiles()) {
            Profile profile = profileRepository.findByCode(profileCode)
                .orElseThrow(() -> new I18nBusinessException(
                    i18nService,
                    "error.profile.code.not_found",
                    "PROFILE_CODE_NOT_FOUND",
                    profileCode
                ));

            // Check if user already has this profile to avoid duplicates
            if (userProfileRepository.findByUserCodeAndProfileId(request.getUserCode(), profile.getId()).isPresent()) {
                continue;
            }

            UserProfile userProfile = new UserProfile();
            userProfile.setUserCode(request.getUserCode());
            userProfile.setProfileId(profile.getId());
            userProfile.setValidFrom(request.getValidFrom());
            userProfile.setValidTo(request.getValidTo());
            userProfile.setAuditForCreate(getCurrentUser());
            userProfileRepository.persist(userProfile);
        }
    }

    private String getCurrentUser() {
        return userContext.getUserId();
    }
}
