package com.kplian.msaccess.api.config;

import com.kplian.msaccess.api.service.I18nService;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.USER)
@ApplicationScoped
public class LocaleResponseFilter implements ContainerResponseFilter {

    @Inject
    I18nService i18nService;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        i18nService.clearLocale();
    }
}
