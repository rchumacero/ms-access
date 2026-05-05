package com.kplian.msaccess.api.mapper;

import com.kplian.msaccess.api.dto.request.InterinRequestDTO;
import com.kplian.msaccess.api.dto.response.InterinResponseDTO;
import com.kplian.msaccess.domain.model.Interin;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class InterinMapper {

    public Interin toEntity(InterinRequestDTO dto) {
        Interin interin = new Interin();
        interin.setUserCode(dto.getUserCode());
        interin.setUserInterinId(dto.getUserInterinId());
        interin.setValidFrom(dto.getValidFrom());
        interin.setValidTo(dto.getValidTo());
        return interin;
    }

    public InterinResponseDTO toResponseDTO(Interin entity) {
        InterinResponseDTO dto = new InterinResponseDTO();
        dto.setId(entity.getId());
        dto.setUserCode(entity.getUserCode());
        dto.setUserInterinId(entity.getUserInterinId());
        dto.setValidFrom(entity.getValidFrom());
        dto.setValidTo(entity.getValidTo());
        mapAudit(entity, dto);
        return dto;
    }

    public List<InterinResponseDTO> toResponseDTOs(List<Interin> entities) {
        return entities.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    private void mapAudit(Interin entity, InterinResponseDTO dto) {
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setDeletedAt(entity.getDeletedAt());
        dto.setDeletedBy(entity.getDeletedBy());
        dto.setStatus(entity.getStatus());
    }
}
