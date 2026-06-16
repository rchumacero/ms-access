package com.kplian.msaccess.api.mapper;

import com.kplian.msaccess.api.dto.request.ResourceRequestDTO;
import com.kplian.msaccess.api.dto.response.ResourceResponseDTO;
import com.kplian.msaccess.api.dto.response.ResourceTreeResponseDTO;
import com.kplian.msaccess.api.dto.response.ResourceTreeSlimResponseDTO;
import com.kplian.msaccess.domain.model.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class ResourceMapper {

    public Resource toEntity(ResourceRequestDTO dto) {
        Resource resource = new Resource();
        resource.setCode(dto.getCode());
        resource.setDescription(dto.getDescription());
        resource.setType(dto.getType());
        resource.setName(dto.getName());
        resource.setRestricted(dto.getRestricted());
        resource.setEndpoint(dto.getEndpoint());
        resource.setResourceId(dto.getResourceId());
        resource.setModuleCode(dto.getModuleCode());
        return resource;
    }

    public ResourceResponseDTO toResponseDTO(Resource entity) {
        ResourceResponseDTO dto = new ResourceResponseDTO();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setDescription(entity.getDescription());
        dto.setType(entity.getType());
        dto.setName(entity.getName());
        dto.setRestricted(entity.getRestricted());
        dto.setEndpoint(entity.getEndpoint());
        dto.setResourceId(entity.getResourceId());
        dto.setModuleCode(entity.getModuleCode());
        if (entity.getMenu() != null) {
            dto.setMenuId(entity.getMenu().getId());
            dto.setMenuName(entity.getMenu().getName());
        }
        mapAudit(entity, dto);
        return dto;
    }

    public ResourceResponseDTO toResponseDTO(Resource entity, java.util.Map<String, Object> translation) {
        ResourceResponseDTO dto = toResponseDTO(entity);
        applyTranslation(dto, translation);
        return dto;
    }

    public List<ResourceResponseDTO> toResponseDTOs(List<Resource> entities) {
        return entities.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    public List<ResourceResponseDTO> toResponseDTOs(
            List<Resource> entities,
            java.util.Map<String, java.util.Map<String, Object>> translations) {
        return entities.stream()
                .map(entity -> {
                    java.util.Map<String, Object> translation = translations.get(entity.getId().toString());
                    return toResponseDTO(entity, translation);
                })
                .collect(Collectors.toList());
    }

    public ResourceTreeResponseDTO toTree(Resource root, List<Resource> allResources) {
        Map<UUID, List<Resource>> childrenByParent = new HashMap<>();
        for (Resource resource : allResources) {
            UUID parentId = resource.getResourceId();
            childrenByParent.computeIfAbsent(parentId, key -> new ArrayList<>()).add(resource);
        }
        return buildTree(root, childrenByParent);
    }

    public ResourceTreeResponseDTO toTree(
            Resource root,
            List<Resource> allResources,
            java.util.Map<String, java.util.Map<String, Object>> translations) {
        Map<UUID, List<Resource>> childrenByParent = new HashMap<>();
        for (Resource resource : allResources) {
            UUID parentId = resource.getResourceId();
            childrenByParent.computeIfAbsent(parentId, key -> new ArrayList<>()).add(resource);
        }
        return buildTree(root, childrenByParent, translations);
    }

    public ResourceTreeSlimResponseDTO toSlimTree(
            Resource root,
            List<Resource> allResources,
            java.util.Map<String, java.util.Map<String, Object>> translations) {
        Map<UUID, List<Resource>> childrenByParent = new HashMap<>();
        for (Resource resource : allResources) {
            UUID parentId = resource.getResourceId();
            childrenByParent.computeIfAbsent(parentId, key -> new ArrayList<>()).add(resource);
        }
        return buildSlimTree(root, childrenByParent, translations);
    }

    public List<ResourceTreeSlimResponseDTO> toSlimTreeForest(
            List<Resource> allResources,
            java.util.Map<String, java.util.Map<String, Object>> translations,
            java.util.Set<UUID> allowedIds) {
        Map<UUID, List<Resource>> childrenByParent = new HashMap<>();
        for (Resource resource : allResources) {
            UUID parentId = resource.getResourceId();
            childrenByParent.computeIfAbsent(parentId, key -> new ArrayList<>()).add(resource);
        }
        List<Resource> roots = childrenByParent.getOrDefault(null, List.of());
        return roots.stream()
                .map(root -> buildSlimTreeFiltered(root, childrenByParent, translations, allowedIds))
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
    }

    private ResourceTreeResponseDTO buildTree(Resource entity, Map<UUID, List<Resource>> childrenByParent) {
        ResourceTreeResponseDTO dto = new ResourceTreeResponseDTO();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setDescription(entity.getDescription());
        dto.setType(entity.getType());
        dto.setName(entity.getName());
        dto.setRestricted(entity.getRestricted());
        dto.setEndpoint(entity.getEndpoint());
        dto.setResourceId(entity.getResourceId());
        dto.setModuleCode(entity.getModuleCode());
        mapAudit(entity, dto);

        List<Resource> children = childrenByParent.getOrDefault(entity.getId(), List.of());
        List<ResourceTreeResponseDTO> childDtos = children.stream()
                .map(child -> buildTree(child, childrenByParent))
                .collect(Collectors.toList());
        dto.setChildren(childDtos);
        return dto;
    }

    private ResourceTreeResponseDTO buildTree(
            Resource entity,
            Map<UUID, List<Resource>> childrenByParent,
            java.util.Map<String, java.util.Map<String, Object>> translations) {
        ResourceTreeResponseDTO dto = new ResourceTreeResponseDTO();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setDescription(entity.getDescription());
        dto.setType(entity.getType());
        dto.setName(entity.getName());
        dto.setRestricted(entity.getRestricted());
        dto.setEndpoint(entity.getEndpoint());
        dto.setResourceId(entity.getResourceId());
        dto.setModuleCode(entity.getModuleCode());
        mapAudit(entity, dto);
        applyTranslation(dto, translations.get(entity.getId().toString()));

        List<Resource> children = childrenByParent.getOrDefault(entity.getId(), List.of());
        List<ResourceTreeResponseDTO> childDtos = children.stream()
                .map(child -> buildTree(child, childrenByParent, translations))
                .collect(Collectors.toList());
        dto.setChildren(childDtos);
        return dto;
    }

    private ResourceTreeSlimResponseDTO buildSlimTree(
            Resource entity,
            Map<UUID, List<Resource>> childrenByParent,
            java.util.Map<String, java.util.Map<String, Object>> translations) {
        ResourceTreeSlimResponseDTO dto = new ResourceTreeSlimResponseDTO();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setDescription(entity.getDescription());
        dto.setType(entity.getType());
        dto.setName(entity.getName());
        dto.setRestricted(entity.getRestricted());
        dto.setEndpoint(entity.getEndpoint());
        applyTranslation(dto, translations.get(entity.getId().toString()));

        List<Resource> children = childrenByParent.getOrDefault(entity.getId(), List.of());
        List<ResourceTreeSlimResponseDTO> childDtos = children.stream()
                .map(child -> buildSlimTree(child, childrenByParent, translations))
                .collect(Collectors.toList());
        dto.setChildren(childDtos);
        return dto;
    }

    private ResourceTreeSlimResponseDTO buildSlimTreeFiltered(
            Resource entity,
            Map<UUID, List<Resource>> childrenByParent,
            java.util.Map<String, java.util.Map<String, Object>> translations,
            java.util.Set<UUID> allowedIds) {
        List<Resource> children = childrenByParent.getOrDefault(entity.getId(), List.of());
        List<ResourceTreeSlimResponseDTO> childDtos = children.stream()
                .map(child -> buildSlimTreeFiltered(child, childrenByParent, translations, allowedIds))
                .filter(dto -> dto != null)
                .collect(Collectors.toList());

        boolean isAllowed = allowedIds.contains(entity.getId());
        if (!isAllowed && childDtos.isEmpty()) {
            return null;
        }

        ResourceTreeSlimResponseDTO dto = new ResourceTreeSlimResponseDTO();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setDescription(entity.getDescription());
        dto.setType(entity.getType());
        dto.setName(entity.getName());
        dto.setRestricted(entity.getRestricted());
        dto.setEndpoint(entity.getEndpoint());
        applyTranslation(dto, translations.get(entity.getId().toString()));
        dto.setChildren(childDtos);
        return dto;
    }

    private void mapAudit(Resource entity, ResourceResponseDTO dto) {
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setDeletedAt(entity.getDeletedAt());
        dto.setDeletedBy(entity.getDeletedBy());
        dto.setStatus(entity.getStatus());
    }

    private void mapAudit(Resource entity, ResourceTreeResponseDTO dto) {
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setDeletedAt(entity.getDeletedAt());
        dto.setDeletedBy(entity.getDeletedBy());
        dto.setStatus(entity.getStatus());
    }

    private void applyTranslation(ResourceResponseDTO dto, java.util.Map<String, Object> translation) {
        if (translation == null || translation.isEmpty()) {
            return;
        }
        if (translation.containsKey("name")) {
            dto.setName(String.valueOf(translation.get("name")));
        }
        if (translation.containsKey("description")) {
            dto.setDescription(String.valueOf(translation.get("description")));
        }
        if (translation.containsKey("endpoint")) {
            dto.setEndpoint(String.valueOf(translation.get("endpoint")));
        }
        if (translation.containsKey("type")) {
            dto.setType(String.valueOf(translation.get("type")));
        }
    }

    private void applyTranslation(ResourceTreeResponseDTO dto, java.util.Map<String, Object> translation) {
        if (translation == null || translation.isEmpty()) {
            return;
        }
        if (translation.containsKey("name")) {
            dto.setName(String.valueOf(translation.get("name")));
        }
        if (translation.containsKey("description")) {
            dto.setDescription(String.valueOf(translation.get("description")));
        }
        if (translation.containsKey("endpoint")) {
            dto.setEndpoint(String.valueOf(translation.get("endpoint")));
        }
        if (translation.containsKey("type")) {
            dto.setType(String.valueOf(translation.get("type")));
        }
    }

    private void applyTranslation(ResourceTreeSlimResponseDTO dto, java.util.Map<String, Object> translation) {
        if (translation == null || translation.isEmpty()) {
            return;
        }
        if (translation.containsKey("name")) {
            dto.setName(String.valueOf(translation.get("name")));
        }
        if (translation.containsKey("description")) {
            dto.setDescription(String.valueOf(translation.get("description")));
        }
        if (translation.containsKey("endpoint")) {
            dto.setEndpoint(String.valueOf(translation.get("endpoint")));
        }
        if (translation.containsKey("type")) {
            dto.setType(String.valueOf(translation.get("type")));
        }
    }
}
