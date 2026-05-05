package com.kplian.msaccess.api.mapper;

import com.kplian.msaccess.api.dto.request.LocalTranslationRequestDTO;
import com.kplian.msaccess.api.dto.response.LocalTranslationResponseDTO;
import com.kplian.msaccess.domain.model.LocalTranslation;
import com.kplian.msaccess.domain.model.LocalTranslationId;
import jakarta.enterprise.context.ApplicationScoped;
import io.vertx.core.json.JsonObject;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class LocalTranslationMapper {

    public LocalTranslation toEntity(LocalTranslationRequestDTO dto) {
        LocalTranslation entity = new LocalTranslation();
        entity.setDomain(dto.getDomain());
        entity.setEntity(dto.getEntity());
        entity.setEntityId(dto.getEntityId());
        entity.setLanguageCode(dto.getLanguageCode());
        entity.setText(encodeText(dto.getText()));
        return entity;
    }

    public LocalTranslation toEntity(LocalTranslationRequestDTO dto, LocalTranslationId id) {
        LocalTranslation entity = new LocalTranslation();
        entity.setId(id);
        entity.setText(encodeText(dto.getText()));
        return entity;
    }

    public LocalTranslationResponseDTO toResponseDTO(LocalTranslation entity) {
        LocalTranslationResponseDTO dto = new LocalTranslationResponseDTO();
        dto.setDomain(entity.getDomain());
        dto.setEntity(entity.getEntity());
        dto.setEntityId(entity.getEntityId());
        dto.setLanguageCode(entity.getLanguageCode());
        dto.setText(decodeText(entity.getText()));
        return dto;
    }

    public List<LocalTranslationResponseDTO> toResponseDTOs(List<LocalTranslation> entities) {
        return entities.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    private String encodeText(Map<String, Object> text) {
        if (text == null) {
            return null;
        }
        return new JsonObject(text).encode();
    }

    private Map<String, Object> decodeText(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        return new JsonObject(text).getMap();
    }
}
