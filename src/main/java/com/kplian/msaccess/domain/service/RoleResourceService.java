package com.kplian.msaccess.domain.service;

import com.kplian.msaccess.api.service.I18nService;
import com.kplian.msaccess.domain.exception.I18nBusinessException;
import com.kplian.msaccess.domain.model.ResourceEntity;
import com.kplian.msaccess.domain.model.RoleResource;
import com.kplian.msaccess.infrastructure.persistence.repository.ResourceRepository;
import com.kplian.msaccess.infrastructure.persistence.repository.RoleRepository;
import com.kplian.msaccess.infrastructure.persistence.repository.RoleResourceRepository;
import com.kplian.msaccess.util.UserContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Map;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Transactional
public class RoleResourceService {

    @Inject
    RoleResourceRepository roleResourceRepository;

    @Inject
    RoleRepository roleRepository;

    @Inject
    ResourceRepository resourceRepository;

    @Inject
    ResourceService resourceService;

    @Inject
    I18nService i18nService;

    @Inject
    UserContext userContext;

    public List<RoleResource> findAll() {
        return roleResourceRepository.find("deletedAt is null").list();
    }

    public List<ResourceEntity> findResourcesByRoleId(UUID roleId) {
        validateRoleExists(roleId);
        List<RoleResource> roleResources = roleResourceRepository.findByRoleId(roleId);
        List<UUID> resourceIds = roleResources.stream()
            .map(RoleResource::getResourceId)
            .toList();
        if (resourceIds.isEmpty()) {
            return List.of();
        }
        return resourceRepository.find("id in ?1 and deletedAt is null", resourceIds).list();
    }

    public Map<String, Map<String, Object>> getTranslationsForResources(List<ResourceEntity> resources) {
        return resourceService.getTranslationsForResources(resources, "access", "resource");
    }

    public int createRoleResourcesRecursive(UUID roleId, UUID rootResourceId) {
        validateRoleExists(roleId);
        validateResourceExists(rootResourceId);
        return roleResourceRepository.insertRoleResourcesRecursive(roleId, rootResourceId, getCurrentUser());
    }

    public int createRoleResourcesWithParents(UUID roleId, UUID rootResourceId) {
        validateRoleExists(roleId);
        validateResourceExists(rootResourceId);
        return roleResourceRepository.insertRoleResourcesWithParents(roleId, rootResourceId, getCurrentUser());
    }

    public int deleteRoleResourcesRecursive(UUID roleId, UUID rootResourceId) {
        validateRoleExists(roleId);
        validateResourceExists(rootResourceId);
        return roleResourceRepository.deleteRoleResourcesRecursive(roleId, rootResourceId, getCurrentUser());
    }

    public RoleResource findById(UUID id) {
        return roleResourceRepository.findByIdOptional(id)
            .orElseThrow(() -> new I18nBusinessException(
                i18nService,
                "error.role_resource.not_found",
                "ROLE_RESOURCE_NOT_FOUND",
                id
            ));
    }

    public RoleResource create(RoleResource roleResource) {
        validateRoleResource(roleResource);
        validateRoleExists(roleResource.getRoleId());
        validateResourceExists(roleResource.getResourceId());
        roleResource.setAuditForCreate(getCurrentUser());
        roleResourceRepository.persist(roleResource);
        return roleResource;
    }

    public RoleResource update(UUID id, RoleResource roleResource) {
        validateRoleResource(roleResource);
        validateRoleExists(roleResource.getRoleId());
        validateResourceExists(roleResource.getResourceId());
        RoleResource existing = findById(id);
        existing.setRoleId(roleResource.getRoleId());
        existing.setResourceId(roleResource.getResourceId());
        existing.setAuditForUpdate(getCurrentUser());
        return existing;
    }

    public void delete(UUID id) {
        RoleResource roleResource = findById(id);
        roleResource.setAuditForDelete(getCurrentUser());
    }

    private void validateRoleResource(RoleResource roleResource) {
        if (roleResource == null) {
            throw new I18nBusinessException(
                i18nService,
                "error.role_resource.invalid",
                "INVALID_ROLE_RESOURCE"
            );
        }
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

    private void validateResourceExists(UUID resourceId) {
        resourceRepository.findByIdOptional(resourceId)
            .orElseThrow(() -> new I18nBusinessException(
                i18nService,
                "error.resource.not_found",
                "RESOURCE_NOT_FOUND",
                resourceId
            ));
    }

    private String getCurrentUser() {
        return userContext.getUserId();
    }
}
