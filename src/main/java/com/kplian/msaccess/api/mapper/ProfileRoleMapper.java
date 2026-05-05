package com.kplian.msaccess.api.mapper;

import com.kplian.msaccess.api.dto.request.ProfileRoleRequestDTO;
import com.kplian.msaccess.api.dto.response.ProfileRoleResponseDTO;
import com.kplian.msaccess.domain.model.ProfileRole;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProfileRoleMapper {

    public ProfileRole toEntity(ProfileRoleRequestDTO dto) {
        ProfileRole profileRole = new ProfileRole();
        profileRole.setProfileId(dto.getProfileId());
        profileRole.setRoleId(dto.getRoleId());
        return profileRole;
    }

    public ProfileRoleResponseDTO toResponseDTO(ProfileRole entity) {
        ProfileRoleResponseDTO dto = new ProfileRoleResponseDTO();
        dto.setId(entity.getId());
        dto.setProfileId(entity.getProfileId());
        dto.setRoleId(entity.getRoleId());
        mapAudit(entity, dto);
        return dto;
    }

    public List<ProfileRoleResponseDTO> toResponseDTOs(List<ProfileRole> entities) {
        return entities.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    private void mapAudit(ProfileRole entity, ProfileRoleResponseDTO dto) {
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setDeletedAt(entity.getDeletedAt());
        dto.setDeletedBy(entity.getDeletedBy());
        dto.setStatus(entity.getStatus());
    }
}
