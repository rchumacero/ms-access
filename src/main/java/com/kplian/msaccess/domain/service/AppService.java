package com.kplian.msaccess.domain.service;

import com.kplian.msaccess.api.service.I18nService;
import com.kplian.msaccess.domain.exception.I18nBusinessException;
import com.kplian.msaccess.domain.model.App;
import com.kplian.msaccess.infrastructure.persistence.repository.AppRepository;
import com.kplian.msaccess.util.UserContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Transactional
public class AppService {

    @Inject
    AppRepository appRepository;

    @Inject
    I18nService i18nService;

    @Inject
    UserContext userContext;

    public List<App> findAll() {
        return appRepository.find("deletedAt is null").list();
    }

    public App findById(UUID id) {
        return appRepository.findByIdOptional(id)
            .orElseThrow(() -> new I18nBusinessException(
                i18nService,
                "error.app.not_found",
                "APP_NOT_FOUND",
                id
            ));
    }

    public App create(App app) {
        validateApp(app);
        if (appRepository.existsByCode(app.getCode())) {
            throw new I18nBusinessException(
                i18nService,
                "error.app.code.exists",
                "APP_CODE_EXISTS",
                app.getCode()
            );
        }
        app.setAuditForCreate(getCurrentUser());
        appRepository.persist(app);
        return app;
    }

    public App update(UUID id, App app) {
        validateApp(app);
        App existing = findById(id);
        if (!existing.getCode().equals(app.getCode()) && appRepository.existsByCode(app.getCode())) {
            throw new I18nBusinessException(
                i18nService,
                "error.app.code.exists",
                "APP_CODE_EXISTS",
                app.getCode()
            );
        }
        existing.setCode(app.getCode());
        existing.setName(app.getName());
        existing.setDescription(app.getDescription());
        existing.setAuditForUpdate(getCurrentUser());
        return existing;
    }

    public void delete(UUID id) {
        App app = findById(id);
        app.setAuditForDelete(getCurrentUser());
    }

    private void validateApp(App app) {
        if (app == null) {
            throw new I18nBusinessException(
                i18nService,
                "error.app.invalid",
                "INVALID_APP"
            );
        }
    }

    private String getCurrentUser() {
        return userContext.getUserId();
    }
}
