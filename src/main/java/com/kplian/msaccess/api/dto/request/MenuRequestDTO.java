package com.kplian.msaccess.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public class MenuRequestDTO {

    @NotNull(message = "{dto.menu.app_id.required}")
    private UUID appId;

    @NotBlank(message = "{dto.menu.code.required}")
    @Size(min = 3, max = 50, message = "{dto.menu.code.size}")
    private String code;

    @NotBlank(message = "{dto.menu.name.required}")
    @Size(min = 3, max = 255, message = "{dto.menu.name.size}")
    private String name;

    private String description;

    public UUID getAppId() {
        return appId;
    }

    public void setAppId(UUID appId) {
        this.appId = appId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
