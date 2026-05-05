package com.kplian.msaccess.api.mapper;

import com.kplian.msaccess.api.dto.request.RoleRequestDTO;
import com.kplian.msaccess.api.dto.response.RoleResponseDTO;
import com.kplian.msaccess.domain.model.Role;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class RoleMapper {

    public Role toEntity(RoleRequestDTO dto) {
        Role role = new Role();
        role.setCode(dto.getCode());
        role.setName(dto.getName());
        role.setModuleCode(dto.getModuleCode());
        role.setVendorCode(dto.getVendorCode());
        return role;
    }

    public RoleResponseDTO toResponseDTO(Role entity) {
        RoleResponseDTO dto = new RoleResponseDTO();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setModuleCode(entity.getModuleCode());
        dto.setVendorCode(entity.getVendorCode());
        mapAudit(entity, dto);
        return dto;
    }

    public List<RoleResponseDTO> toResponseDTOs(List<Role> entities) {
        return entities.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    private void mapAudit(Role entity, RoleResponseDTO dto) {
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setDeletedAt(entity.getDeletedAt());
        dto.setDeletedBy(entity.getDeletedBy());
        dto.setStatus(entity.getStatus());
    }
}
