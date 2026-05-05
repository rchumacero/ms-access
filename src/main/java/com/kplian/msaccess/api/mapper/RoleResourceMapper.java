package com.kplian.msaccess.api.mapper;

import com.kplian.msaccess.api.dto.request.RoleResourceRequestDTO;
import com.kplian.msaccess.api.dto.response.RoleResourceResponseDTO;
import com.kplian.msaccess.domain.model.RoleResource;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RoleResourceMapper {

    public RoleResource toEntity(RoleResourceRequestDTO dto) {
        RoleResource roleResource = new RoleResource();
        roleResource.setRoleId(dto.getRoleId());
        roleResource.setResourceId(dto.getResourceId());
        return roleResource;
    }

    public RoleResourceResponseDTO toResponseDTO(RoleResource entity) {
        RoleResourceResponseDTO dto = new RoleResourceResponseDTO();
        dto.setId(entity.getId());
        dto.setRoleId(entity.getRoleId());
        dto.setResourceId(entity.getResourceId());
        mapAudit(entity, dto);
        return dto;
    }

    public List<RoleResourceResponseDTO> toResponseDTOs(List<RoleResource> entities) {
        return entities.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    private void mapAudit(RoleResource entity, RoleResourceResponseDTO dto) {
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setDeletedAt(entity.getDeletedAt());
        dto.setDeletedBy(entity.getDeletedBy());
        dto.setStatus(entity.getStatus());
    }
}
