package com.kplian.msaccess.domain.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class LocalTranslationId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "language_code", length = 15)
    protected String languageCode;

    @Column(name = "domain", length = 50)
    protected String domain;

    @Column(name = "entity", length = 50)
    protected String entityName;

    @Column(name = "entity_id", length = 50)
    protected String entityId;

    public LocalTranslationId() {
    }

    public LocalTranslationId(String domain, String entityName, String entityId, String languageCode) {
        this.domain = domain;
        this.entityName = entityName;
        this.entityId = entityId;
        this.languageCode = languageCode;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LocalTranslationId that = (LocalTranslationId) o;
        return Objects.equals(domain, that.domain)
            && Objects.equals(entityName, that.entityName)
            && Objects.equals(entityId, that.entityId)
            && Objects.equals(languageCode, that.languageCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain, entityName, entityId, languageCode);
    }
}
