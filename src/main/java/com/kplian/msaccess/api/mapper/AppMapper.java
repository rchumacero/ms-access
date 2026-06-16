package com.kplian.msaccess.api.mapper;

import com.kplian.msaccess.api.dto.request.AppRequestDTO;
import com.kplian.msaccess.api.dto.response.AppResponseDTO;
import com.kplian.msaccess.domain.model.App;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class AppMapper {

    public App toEntity(AppRequestDTO dto) {
        App app = new App();
        app.setCode(dto.getCode());
        app.setName(dto.getName());
        app.setDescription(dto.getDescription());
        return app;
    }

    public AppResponseDTO toResponseDTO(App entity) {
        AppResponseDTO dto = new AppResponseDTO();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        mapAudit(entity, dto);
        return dto;
    }

    public List<AppResponseDTO> toResponseDTOs(List<App> entities) {
        return entities.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    private void mapAudit(App entity, AppResponseDTO dto) {
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setDeletedAt(entity.getDeletedAt());
        dto.setDeletedBy(entity.getDeletedBy());
        dto.setStatus(entity.getStatus());
    }
}
