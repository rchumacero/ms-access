package com.kplian.msaccess.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public class ResourceRequestDTO {

    @NotBlank(message = "{dto.resource.code.required}")
    @Size(min = 3, max = 50, message = "{dto.resource.code.size}")
    private String code;

    private String description;

    @NotBlank(message = "{dto.resource.type.required}")
    private String type;

    @NotBlank(message = "{dto.resource.name.required}")
    @Size(min = 3, max = 255, message = "{dto.resource.name.size}")
    private String name;

    @NotBlank(message = "{dto.resource.module_code.required}")
    @Size(min = 2, max = 50, message = "{dto.resource.module_code.size}")
    private String moduleCode;

    private Boolean restricted;

    private String endpoint;

    private UUID resourceId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public Boolean getRestricted() {
        return restricted;
    }

    public void setRestricted(Boolean restricted) {
        this.restricted = restricted;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public UUID getResourceId() {
        return resourceId;
    }

    public void setResourceId(UUID resourceId) {
        this.resourceId = resourceId;
    }
}
