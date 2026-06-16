package com.kplian.msaccess.domain.service;

import com.kplian.msaccess.api.service.I18nService;
import com.kplian.msaccess.domain.exception.I18nBusinessException;
import com.kplian.msaccess.domain.model.App;
import com.kplian.msaccess.domain.model.Menu;
import com.kplian.msaccess.infrastructure.persistence.repository.MenuRepository;
import com.kplian.msaccess.util.UserContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Transactional
public class MenuService {

    @Inject
    MenuRepository menuRepository;

    @Inject
    AppService appService;

    @Inject
    I18nService i18nService;

    @Inject
    UserContext userContext;

    public List<Menu> findAll() {
        return menuRepository.findAllWithApp();
    }

    public List<Menu> findByAppId(UUID appId) {
        return menuRepository.findByAppId(appId);
    }

    public Menu findById(UUID id) {
        return menuRepository.findByIdOptional(id)
            .orElseThrow(() -> new I18nBusinessException(
                i18nService,
                "error.menu.not_found",
                "MENU_NOT_FOUND",
                id
            ));
    }

    public Menu create(UUID appId, Menu menu) {
        validateMenu(menu);
        App app = appService.findById(appId);
        if (menuRepository.existsByCode(menu.getCode())) {
            throw new I18nBusinessException(
                i18nService,
                "error.menu.code.exists",
                "MENU_CODE_EXISTS",
                menu.getCode()
            );
        }
        menu.setApp(app);
        menu.setAuditForCreate(getCurrentUser());
        menuRepository.persist(menu);
        return menu;
    }

    public Menu update(UUID id, Menu menu) {
        validateMenu(menu);
        Menu existing = findById(id);
        if (!existing.getCode().equals(menu.getCode()) && menuRepository.existsByCode(menu.getCode())) {
            throw new I18nBusinessException(
                i18nService,
                "error.menu.code.exists",
                "MENU_CODE_EXISTS",
                menu.getCode()
            );
        }
        existing.setCode(menu.getCode());
        existing.setName(menu.getName());
        existing.setDescription(menu.getDescription());
        existing.setAuditForUpdate(getCurrentUser());
        return existing;
    }

    public void delete(UUID id) {
        Menu menu = findById(id);
        menu.setAuditForDelete(getCurrentUser());
    }

    private void validateMenu(Menu menu) {
        if (menu == null) {
            throw new I18nBusinessException(
                i18nService,
                "error.menu.invalid",
                "INVALID_MENU"
            );
        }
    }

    private String getCurrentUser() {
        return userContext.getUserId();
    }
}
