package com.kplian.msaccess.util;

import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class UserContext {
    private String userId;

    public String getUserId() {
        return userId != null ? userId : "system";
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
