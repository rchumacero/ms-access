package com.kplian.msaccess.api.mapper;

import com.kplian.msaccess.api.dto.request.UserProfileRequestDTO;
import com.kplian.msaccess.api.dto.response.UserProfileResponseDTO;
import com.kplian.msaccess.domain.model.UserProfile;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserProfileMapper {

    public UserProfile toEntity(UserProfileRequestDTO dto) {
        UserProfile userProfile = new UserProfile();
        userProfile.setUserCode(dto.getUserCode());
        userProfile.setProfileId(dto.getProfileId());
        userProfile.setValidFrom(dto.getValidFrom());
        userProfile.setValidTo(dto.getValidTo());
        return userProfile;
    }

    public UserProfileResponseDTO toResponseDTO(UserProfile entity) {
        UserProfileResponseDTO dto = new UserProfileResponseDTO();
        dto.setId(entity.getId());
        dto.setUserCode(entity.getUserCode());
        dto.setProfileId(entity.getProfileId());
        dto.setValidFrom(entity.getValidFrom());
        dto.setValidTo(entity.getValidTo());
        mapAudit(entity, dto);
        return dto;
    }

    public List<UserProfileResponseDTO> toResponseDTOs(List<UserProfile> entities) {
        return entities.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    private void mapAudit(UserProfile entity, UserProfileResponseDTO dto) {
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setDeletedAt(entity.getDeletedAt());
        dto.setDeletedBy(entity.getDeletedBy());
        dto.setStatus(entity.getStatus());
    }
}
