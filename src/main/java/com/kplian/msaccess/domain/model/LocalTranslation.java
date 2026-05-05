package com.kplian.msaccess.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "tlocal_translation")
public class LocalTranslation extends PanacheEntityBase {

    @EmbeddedId
    protected LocalTranslationId id;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "text", columnDefinition = "jsonb")
    protected String text;

    public LocalTranslationId getId() {
        return id;
    }

    public void setId(LocalTranslationId id) {
        this.id = id;
    }

    public String getDomain() {
        return id != null ? id.getDomain() : null;
    }

    public void setDomain(String domain) {
        ensureId();
        id.setDomain(domain);
    }

    public String getEntity() {
        return id != null ? id.getEntityName() : null;
    }

    public void setEntity(String entity) {
        ensureId();
        id.setEntityName(entity);
    }

    public String getEntityId() {
        return id != null ? id.getEntityId() : null;
    }

    public void setEntityId(String entityId) {
        ensureId();
        id.setEntityId(entityId);
    }

    public String getLanguageCode() {
        return id != null ? id.getLanguageCode() : null;
    }

    public void setLanguageCode(String languageCode) {
        ensureId();
        id.setLanguageCode(languageCode);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private void ensureId() {
        if (id == null) {
            id = new LocalTranslationId();
        }
    }
}
