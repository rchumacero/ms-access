package com.kplian.msaccess.domain.service;

import com.kplian.msaccess.api.service.I18nService;
import com.kplian.msaccess.domain.exception.I18nBusinessException;
import com.kplian.msaccess.domain.model.ResourceEntity;
import com.kplian.msaccess.infrastructure.persistence.repository.ResourceRepository;
import com.kplian.msaccess.infrastructure.persistence.repository.RoleResourceRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@ApplicationScoped
@Transactional
public class AccessMenuService {

    @Inject
    ResourceRepository resourceRepository;

    @Inject
    RoleResourceRepository roleResourceRepository;

    @Inject
    I18nService i18nService;

    public List<ResourceEntity> findAllResources() {
        return resourceRepository.find("deletedAt is null").list();
    }

    public Set<UUID> findAllowedResourceIdsByUserCode(String userCode) {
        if (userCode == null || userCode.isBlank()) {
            throw new I18nBusinessException(
                i18nService,
                "common.required",
                "USER_CODE_REQUIRED"
            );
        }
        return Set.copyOf(roleResourceRepository.findResourceIdsByUserCode(userCode));
    }
}
