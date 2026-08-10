package com.kplian.msaccess.domain.service;

import com.kplian.msaccess.api.service.I18nService;
import com.kplian.msaccess.domain.exception.I18nBusinessException;
import com.kplian.msaccess.domain.model.Resource;
import com.kplian.msaccess.domain.model.Menu;
import com.kplian.msaccess.domain.model.LocalTranslation;
import com.kplian.msaccess.domain.model.LocalTranslationId;
import com.kplian.msaccess.infrastructure.persistence.repository.ResourceRepository;
import com.kplian.msaccess.infrastructure.persistence.repository.LocalTranslationRepository;
import com.kplian.msaccess.util.UserContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import io.vertx.core.json.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
public class ResourceService {

    @Inject
    ResourceRepository resourceRepository;

    @Inject
    I18nService i18nService;

    @Inject
    LocalTranslationRepository localTranslationRepository;

    @Inject
    UserContext userContext;

    @Inject
    MenuService menuService;

    public List<Resource> findAll() {
        return resourceRepository.findAllWithMenu();
    }

    public Resource findById(UUID id) {
        return resourceRepository.findByIdOptional(id)
                .orElseThrow(() -> new I18nBusinessException(
                        i18nService,
                        "error.resource.not_found",
                        "RESOURCE_NOT_FOUND",
                        id));
    }

    public Map<String, Map<String, Object>> getTranslationsForResources(List<Resource> resources, String domain,
            String entity) {
        if (resources == null || resources.isEmpty()) {
            return Map.of();
        }
        List<String> entityIds = resources.stream()
                .map(resource -> resource.getId().toString())
                .collect(Collectors.toList());

        Locale locale = i18nService.getLocale();
        String languageTag = locale == null ? null : locale.toLanguageTag();
        String languageShort = locale == null ? null : locale.getLanguage();

        Map<String, Map<String, Object>> translations = new HashMap<>();

        // 1. Fetch base language translations (e.g. "es")
        if (languageShort != null && !languageShort.isBlank()) {
            translations.putAll(fetchTranslations(domain, entity, languageShort, entityIds));
        }

        // 2. Fetch specific language translations and override (e.g. "es-ES")
        if (languageTag != null && !languageTag.isBlank() && !languageTag.equals(languageShort)) {
            Map<String, Map<String, Object>> specificTranslations = fetchTranslations(domain, entity, languageTag,
                    entityIds);
            for (Map.Entry<String, Map<String, Object>> entry : specificTranslations.entrySet()) {
                translations.put(entry.getKey(), entry.getValue());
            }
        }

        return translations;
    }

    public List<Resource> findChildren(UUID parentId) {
        findById(parentId);
        return resourceRepository.findChildren(parentId);
    }

    public Resource create(Resource resource, UUID menuId) {
        validateResource(resource);
        if (resourceRepository.existsByCode(resource.getCode())) {
            throw new I18nBusinessException(
                    i18nService,
                    "error.resource.code.exists",
                    "RESOURCE_CODE_EXISTS",
                    resource.getCode());
        }
        validateParentResource(resource.getResourceId());
        if (menuId != null) {
            resource.setMenu(menuService.findById(menuId));
        }
        resource.setAuditForCreate(getCurrentUser());
        resourceRepository.persist(resource);
        return resource;
    }

    public Resource update(UUID id, Resource resource, UUID menuId) {
        validateResource(resource);
        Resource existing = findById(id);
        if (!existing.getCode().equals(resource.getCode()) && resourceRepository.existsByCode(resource.getCode())) {
            throw new I18nBusinessException(
                    i18nService,
                    "error.resource.code.exists",
                    "RESOURCE_CODE_EXISTS",
                    resource.getCode());
        }
        validateParentResource(resource.getResourceId());
        if (menuId != null) {
            existing.setMenu(menuService.findById(menuId));
        } else {
            existing.setMenu(null);
        }
        existing.setCode(resource.getCode());
        existing.setName(resource.getName());
        existing.setDescription(resource.getDescription());
        existing.setType(resource.getType());
        existing.setRestricted(resource.getRestricted());
        existing.setEndpoint(resource.getEndpoint());
        existing.setResourceId(resource.getResourceId());
        existing.setModuleCode(resource.getModuleCode());
        existing.setAuditForUpdate(getCurrentUser());
        return existing;
    }

    public void delete(UUID id) {
        Resource resource = findById(id);
        resource.setAuditForDelete(getCurrentUser());
    }

    private void validateResource(Resource resource) {
        if (resource == null) {
            throw new I18nBusinessException(
                    i18nService,
                    "error.resource.invalid",
                    "INVALID_RESOURCE");
        }
    }

    private void validateParentResource(UUID parentId) {
        if (parentId != null) {
            resourceRepository.findByIdOptional(parentId)
                    .orElseThrow(() -> new I18nBusinessException(
                            i18nService,
                            "error.resource.not_found",
                            "RESOURCE_NOT_FOUND",
                            parentId));
        }
    }

    private String getCurrentUser() {
        return userContext.getUserId();
    }

    private Map<String, Map<String, Object>> fetchTranslations(
            String domain,
            String entity,
            String languageCode,
            List<String> entityIds) {
        if (languageCode == null || languageCode.isBlank()) {
            return Map.of();
        }
        List<LocalTranslation> translations = localTranslationRepository
                .findByDomainEntityLanguageAndEntityIds(domain, entity, languageCode, entityIds);
        Map<String, Map<String, Object>> map = new HashMap<>();
        for (LocalTranslation translation : translations) {
            String json = translation.getText();
            if (json == null || json.isBlank()) {
                continue;
            }
            JsonObject obj = new JsonObject(json);
            map.put(translation.getEntityId(), obj.getMap());
        }
        return map;
    }

    public Resource cloneHierarchy(UUID sourceResourceId, UUID targetMenuId) {
        Resource source = findById(sourceResourceId);
        Menu targetMenu = menuService.findById(targetMenuId);

        // Fetch all active resources to build the hierarchy tree in memory
        List<Resource> allResources = resourceRepository.findAllWithMenu();
        Map<UUID, List<Resource>> parentToChildren = allResources.stream()
                .filter(r -> r.getResourceId() != null)
                .collect(Collectors.groupingBy(Resource::getResourceId));

        List<Resource> hierarchy = new ArrayList<>();
        hierarchy.add(source);
        collectDescendants(source.getId(), parentToChildren, hierarchy);

        Map<UUID, Resource> oldIdToCloneMap = new HashMap<>();
        Map<UUID, UUID> oldToNewIdMap = new HashMap<>();
        List<Resource> clones = new ArrayList<>();
        Set<String> generatedCodes = new HashSet<>();

        for (Resource original : hierarchy) {
            Resource clone = new Resource();
            clone.setCode(generateUniqueCode(original.getCode(), generatedCodes));
            clone.setName(original.getName());
            clone.setDescription(original.getDescription());
            clone.setType(original.getType());
            clone.setRestricted(original.getRestricted());
            clone.setEndpoint(original.getEndpoint());
            clone.setModuleCode(original.getModuleCode());
            clone.setMenu(targetMenu);
            clone.setAuditForCreate(getCurrentUser());

            if (original.getId().equals(sourceResourceId)) {
                clone.setResourceId(null);
            } else {
                Resource clonedParent = oldIdToCloneMap.get(original.getResourceId());
                if (clonedParent != null) {
                    clone.setResourceId(clonedParent.getId());
                }
            }

            resourceRepository.persist(clone);

            oldIdToCloneMap.put(original.getId(), clone);
            oldToNewIdMap.put(original.getId(), clone.getId());
            clones.add(clone);
        }

        // Clone translations
        List<String> originalIdsStr = hierarchy.stream()
                .map(r -> r.getId().toString())
                .collect(Collectors.toList());
        List<LocalTranslation> originalTranslations = localTranslationRepository.list(
                "id.domain = ?1 and id.entityName = ?2 and id.entityId in ?3",
                "access", "resource", originalIdsStr
        );

        for (LocalTranslation translation : originalTranslations) {
            UUID newId = oldToNewIdMap.get(UUID.fromString(translation.getEntityId()));
            if (newId != null) {
                LocalTranslation cloneTranslation = new LocalTranslation();
                LocalTranslationId cloneId = new LocalTranslationId(
                        translation.getDomain(),
                        translation.getEntity(),
                        newId.toString(),
                        translation.getLanguageCode()
                );
                cloneTranslation.setId(cloneId);
                cloneTranslation.setText(translation.getText());
                localTranslationRepository.persist(cloneTranslation);
            }
        }

        // Return the root cloned resource
        return clones.get(0);
    }

    private void collectDescendants(UUID parentId, Map<UUID, List<Resource>> parentToChildren, List<Resource> collected) {
        List<Resource> children = parentToChildren.getOrDefault(parentId, List.of());
        for (Resource child : children) {
            collected.add(child);
            collectDescendants(child.getId(), parentToChildren, collected);
        }
    }

    private String generateUniqueCode(String originalCode, Set<String> generatedCodes) {
        String baseCode = originalCode;
        if (baseCode.length() > 40) {
            baseCode = baseCode.substring(0, 40);
        }
        String targetCode = baseCode + "_CLONE";
        int counter = 1;
        while (resourceRepository.existsByCode(targetCode) || generatedCodes.contains(targetCode)) {
            String suffix = "_" + counter;
            int maxBaseLength = 50 - "_CLONE".length() - suffix.length();
            if (originalCode.length() > maxBaseLength) {
                baseCode = originalCode.substring(0, maxBaseLength);
            } else {
                baseCode = originalCode;
            }
            targetCode = baseCode + "_CLONE" + suffix;
            counter++;
        }
        generatedCodes.add(targetCode);
        return targetCode;
    }
}
