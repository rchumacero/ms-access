package com.kplian.msaccess.api.config;

import com.kplian.msaccess.api.service.I18nService;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import java.util.Locale;

@Provider
@Priority(Priorities.AUTHENTICATION)
@ApplicationScoped
public class LocaleRequestFilter implements ContainerRequestFilter {

    @Inject
    I18nService i18nService;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String language = requestContext.getHeaderString("Accept-Language");
        Locale locale = language == null || language.isBlank()
            ? Locale.forLanguageTag("es-ES")
            : Locale.forLanguageTag(language.split(",")[0].trim());
        i18nService.setLocale(locale);
    }
}
