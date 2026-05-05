package com.kplian.msaccess.api.config;

import com.kplian.msaccess.util.UserContext;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class UserRequestFilter implements ContainerRequestFilter {

    @Inject
    UserContext userContext;

    private static final String X_USER_ID = "X-User-Id";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String userId = requestContext.getHeaderString(X_USER_ID);
        if (userId != null && !userId.isBlank()) {
            userContext.setUserId(userId);
        }
    }
}
