package com.kplian.msaccess.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

public class LocalTranslationRequestDTO {

    @NotBlank(message = "{dto.local_translation.domain.required}")
    private String domain;

    @NotBlank(message = "{dto.local_translation.entity.required}")
    private String entity;

    @NotBlank(message = "{dto.local_translation.entity_id.required}")
    private String entityId;

    @NotBlank(message = "{dto.local_translation.language_code.required}")
    private String languageCode;

    @NotNull(message = "{dto.local_translation.text.required}")
    private Map<String, Object> text;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public Map<String, Object> getText() {
        return text;
    }

    public void setText(Map<String, Object> text) {
        this.text = text;
    }
}
