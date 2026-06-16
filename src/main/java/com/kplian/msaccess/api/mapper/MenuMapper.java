package com.kplian.msaccess.api.mapper;

import com.kplian.msaccess.api.dto.request.MenuRequestDTO;
import com.kplian.msaccess.api.dto.response.MenuResponseDTO;
import com.kplian.msaccess.domain.model.Menu;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class MenuMapper {

    public Menu toEntity(MenuRequestDTO dto) {
        Menu menu = new Menu();
        menu.setCode(dto.getCode());
        menu.setName(dto.getName());
        menu.setDescription(dto.getDescription());
        return menu;
    }

    public MenuResponseDTO toResponseDTO(Menu entity) {
        MenuResponseDTO dto = new MenuResponseDTO();
        dto.setId(entity.getId());
        if (entity.getApp() != null) {
            dto.setAppId(entity.getApp().getId());
            dto.setAppName(entity.getApp().getName());
        }
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        mapAudit(entity, dto);
        return dto;
    }

    public List<MenuResponseDTO> toResponseDTOs(List<Menu> entities) {
        return entities.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    private void mapAudit(Menu entity, MenuResponseDTO dto) {
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setDeletedAt(entity.getDeletedAt());
        dto.setDeletedBy(entity.getDeletedBy());
        dto.setStatus(entity.getStatus());
    }
}
