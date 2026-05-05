package com.kplian.msaccess.api.config;

import com.kplian.msaccess.api.service.I18nService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Locale;
import org.hibernate.validator.spi.messageinterpolation.LocaleResolver;
import org.hibernate.validator.spi.messageinterpolation.LocaleResolverContext;

@ApplicationScoped
public class ValidationLocaleResolver implements LocaleResolver {

    @Inject
    I18nService i18nService;

    @Override
    public Locale resolve(LocaleResolverContext context) {
        return i18nService.getLocale();
    }
}
