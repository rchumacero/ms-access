package com.kplian.msaccess.api.mapper;

import com.kplian.msaccess.api.dto.request.ProfileRequestDTO;
import com.kplian.msaccess.api.dto.response.ProfileResponseDTO;
import com.kplian.msaccess.domain.model.Profile;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProfileMapper {

    public Profile toEntity(ProfileRequestDTO dto) {
        Profile profile = new Profile();
        profile.setCode(dto.getCode());
        profile.setName(dto.getName());
        profile.setModuleCode(dto.getModuleCode());
        profile.setVendorCode(dto.getVendorCode());
        return profile;
    }

    public ProfileResponseDTO toResponseDTO(Profile entity) {
        ProfileResponseDTO dto = new ProfileResponseDTO();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setModuleCode(entity.getModuleCode());
        dto.setVendorCode(entity.getVendorCode());
        mapAudit(entity, dto);
        return dto;
    }

    public List<ProfileResponseDTO> toResponseDTOs(List<Profile> entities) {
        return entities.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    private void mapAudit(Profile entity, ProfileResponseDTO dto) {
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setDeletedAt(entity.getDeletedAt());
        dto.setDeletedBy(entity.getDeletedBy());
        dto.setStatus(entity.getStatus());
    }
}
