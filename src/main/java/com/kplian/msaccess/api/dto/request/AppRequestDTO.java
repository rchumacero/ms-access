package com.kplian.msaccess.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AppRequestDTO {

    @NotBlank(message = "{dto.app.code.required}")
    @Size(min = 3, max = 50, message = "{dto.app.code.size}")
    private String code;

    @NotBlank(message = "{dto.app.name.required}")
    @Size(min = 3, max = 255, message = "{dto.app.name.size}")
    private String name;

    private String description;

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
