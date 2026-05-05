package com.kplian.msaccess.domain.service;

import com.kplian.msaccess.api.service.I18nService;
import com.kplian.msaccess.domain.exception.I18nBusinessException;
import com.kplian.msaccess.domain.model.Interin;
import com.kplian.msaccess.infrastructure.persistence.repository.InterinRepository;
import com.kplian.msaccess.util.UserContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Transactional
public class InterinService {

    @Inject
    InterinRepository interinRepository;

    @Inject
    I18nService i18nService;

    @Inject
    UserContext userContext;

    public List<Interin> findAll() {
        return interinRepository.find("deletedAt is null").list();
    }

    public Interin findById(UUID id) {
        return interinRepository.findByIdOptional(id)
            .orElseThrow(() -> new I18nBusinessException(
                i18nService,
                "error.interin.not_found",
                "INTERIN_NOT_FOUND",
                id
            ));
    }

    public Interin create(Interin interin) {
        validateInterin(interin);
        interin.setAuditForCreate(getCurrentUser());
        interinRepository.persist(interin);
        return interin;
    }

    public Interin update(UUID id, Interin interin) {
        validateInterin(interin);
        Interin existing = findById(id);
        existing.setUserCode(interin.getUserCode());
        existing.setUserInterinId(interin.getUserInterinId());
        existing.setValidFrom(interin.getValidFrom());
        existing.setValidTo(interin.getValidTo());
        existing.setAuditForUpdate(getCurrentUser());
        return existing;
    }

    public void delete(UUID id) {
        Interin interin = findById(id);
        interin.setAuditForDelete(getCurrentUser());
    }

    private void validateInterin(Interin interin) {
        if (interin == null) {
            throw new I18nBusinessException(
                i18nService,
                "error.interin.invalid",
                "INVALID_INTERIN"
            );
        }
    }

    private String getCurrentUser() {
        return userContext.getUserId();
    }
}
