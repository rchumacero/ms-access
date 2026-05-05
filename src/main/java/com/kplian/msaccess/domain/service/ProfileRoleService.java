package com.kplian.msaccess.domain.service;

import com.kplian.msaccess.api.service.I18nService;
import com.kplian.msaccess.domain.exception.I18nBusinessException;
import com.kplian.msaccess.domain.model.ProfileRole;
import com.kplian.msaccess.domain.model.Role;
import com.kplian.msaccess.infrastructure.persistence.repository.ProfileRepository;
import com.kplian.msaccess.infrastructure.persistence.repository.ProfileRoleRepository;
import com.kplian.msaccess.infrastructure.persistence.repository.RoleRepository;
import com.kplian.msaccess.util.UserContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Transactional
public class ProfileRoleService {

    @Inject
    ProfileRoleRepository profileRoleRepository;

    @Inject
    ProfileRepository profileRepository;

    @Inject
    RoleRepository roleRepository;

    @Inject
    I18nService i18nService;

    @Inject
    UserContext userContext;

    public List<ProfileRole> findAll() {
        return profileRoleRepository.find("deletedAt is null").list();
    }

    public ProfileRole findById(UUID id) {
        return profileRoleRepository.findByIdOptional(id)
            .orElseThrow(() -> new I18nBusinessException(
                i18nService,
                "error.profile_role.not_found",
                "PROFILE_ROLE_NOT_FOUND",
                id
            ));
    }

    public List<Role> findRolesByProfileId(UUID profileId) {
        validateProfileExists(profileId);
        List<ProfileRole> profileRoles = profileRoleRepository.findByProfileId(profileId);
        List<UUID> roleIds = profileRoles.stream()
            .map(ProfileRole::getRoleId)
            .toList();
        if (roleIds.isEmpty()) {
            return List.of();
        }
        return roleRepository.find("id in ?1 and deletedAt is null", roleIds).list();
    }

    public ProfileRole create(ProfileRole profileRole) {
        validateProfileRole(profileRole);
        validateProfileExists(profileRole.getProfileId());
        validateRoleExists(profileRole.getRoleId());
        profileRole.setAuditForCreate(getCurrentUser());
        profileRoleRepository.persist(profileRole);
        return profileRole;
    }

    public ProfileRole update(UUID id, ProfileRole profileRole) {
        validateProfileRole(profileRole);
        validateProfileExists(profileRole.getProfileId());
        validateRoleExists(profileRole.getRoleId());
        ProfileRole existing = findById(id);
        existing.setProfileId(profileRole.getProfileId());
        existing.setRoleId(profileRole.getRoleId());
        existing.setAuditForUpdate(getCurrentUser());
        return existing;
    }

    public void delete(UUID id) {
        ProfileRole profileRole = findById(id);
        profileRole.setAuditForDelete(getCurrentUser());
    }

    private void validateProfileRole(ProfileRole profileRole) {
        if (profileRole == null) {
            throw new I18nBusinessException(
                i18nService,
                "error.profile_role.invalid",
                "INVALID_PROFILE_ROLE"
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

    private void validateRoleExists(UUID roleId) {
        roleRepository.findByIdOptional(roleId)
            .orElseThrow(() -> new I18nBusinessException(
                i18nService,
                "error.role.not_found",
                "ROLE_NOT_FOUND",
                roleId
            ));
    }

    private String getCurrentUser() {
        return userContext.getUserId();
    }
}
