package com.kplian.msaccess.domain.service;

import com.kplian.msaccess.api.service.I18nService;
import com.kplian.msaccess.domain.exception.I18nBusinessException;
import com.kplian.msaccess.domain.model.LocalTranslation;
import com.kplian.msaccess.domain.model.LocalTranslationId;
import com.kplian.msaccess.infrastructure.persistence.repository.LocalTranslationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Transactional
public class LocalTranslationService {

    @Inject
    LocalTranslationRepository localTranslationRepository;

    @Inject
    I18nService i18nService;

    public List<LocalTranslation> findAll() {
        return localTranslationRepository.findAll().list();
    }

    public LocalTranslation findById(LocalTranslationId id) {
        return localTranslationRepository.findByIdOptional(id)
            .orElseThrow(() -> new I18nBusinessException(
                i18nService,
                "error.local_translation.not_found",
                "LOCAL_TRANSLATION_NOT_FOUND",
                id.getDomain(),
                id.getEntityName(),
                id.getEntityId(),
                id.getLanguageCode()
            ));
    }

    public LocalTranslation create(LocalTranslation translation) {
        validateTranslation(translation);
        LocalTranslationId id = translation.getId();
        if (id != null && localTranslationRepository.findByIdOptional(id).isPresent()) {
            throw new I18nBusinessException(
                i18nService,
                "error.local_translation.exists",
                "LOCAL_TRANSLATION_EXISTS",
                id.getDomain(),
                id.getEntityName(),
                id.getEntityId(),
                id.getLanguageCode()
            );
        }
        localTranslationRepository.persist(translation);
        return translation;
    }

    public LocalTranslation update(LocalTranslationId id, LocalTranslation translation) {
        validateTranslation(translation);
        LocalTranslation existing = findById(id);
        existing.setText(translation.getText());
        return existing;
    }

    public void delete(LocalTranslationId id) {
        LocalTranslation existing = findById(id);
        localTranslationRepository.delete(existing);
    }

    public LocalTranslation upsert(LocalTranslationId id, String text) {
        LocalTranslation existing = localTranslationRepository.findByIdOptional(id).orElse(null);
        if (existing == null) {
            LocalTranslation translation = new LocalTranslation();
            translation.setId(id);
            translation.setText(text);
            localTranslationRepository.persist(translation);
            return translation;
        }
        existing.setText(text);
        return existing;
    }

    private void validateTranslation(LocalTranslation translation) {
        if (translation == null) {
            throw new I18nBusinessException(
                i18nService,
                "error.local_translation.invalid",
                "INVALID_LOCAL_TRANSLATION"
            );
        }
    }
}
