package com.kplian.msaccess.domain.service;

import com.kplian.msaccess.api.service.I18nService;
import com.kplian.msaccess.domain.exception.I18nBusinessException;
import com.kplian.msaccess.domain.model.Role;
import com.kplian.msaccess.infrastructure.persistence.repository.RoleRepository;
import com.kplian.msaccess.util.UserContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Transactional
public class RoleService {

    @Inject
    RoleRepository roleRepository;

    @Inject
    I18nService i18nService;

    @Inject
    UserContext userContext;

    public List<Role> findAll() {
        return roleRepository.find("deletedAt is null").list();
    }

    public Role findById(UUID id) {
        return roleRepository.findByIdOptional(id)
            .orElseThrow(() -> new I18nBusinessException(
                i18nService,
                "error.role.not_found",
                "ROLE_NOT_FOUND",
                id
            ));
    }

    public Role create(Role role) {
        validateRole(role);
        if (roleRepository.existsByCode(role.getCode())) {
            throw new I18nBusinessException(
                i18nService,
                "error.role.code.exists",
                "ROLE_CODE_EXISTS",
                role.getCode()
            );
        }
        role.setAuditForCreate(getCurrentUser());
        roleRepository.persist(role);
        return role;
    }

    public Role update(UUID id, Role role) {
        validateRole(role);
        Role existing = findById(id);
        if (!existing.getCode().equals(role.getCode()) && roleRepository.existsByCode(role.getCode())) {
            throw new I18nBusinessException(
                i18nService,
                "error.role.code.exists",
                "ROLE_CODE_EXISTS",
                role.getCode()
            );
        }
        existing.setCode(role.getCode());
        existing.setName(role.getName());
        existing.setModuleCode(role.getModuleCode());
        existing.setVendorCode(role.getVendorCode());
        existing.setAuditForUpdate(getCurrentUser());
        return existing;
    }

    public void delete(UUID id) {
        Role role = findById(id);
        role.setAuditForDelete(getCurrentUser());
    }

    private void validateRole(Role role) {
        if (role == null) {
            throw new I18nBusinessException(
                i18nService,
                "error.role.invalid",
                "INVALID_ROLE"
            );
        }
    }

    private String getCurrentUser() {
        return userContext.getUserId();
    }
}
