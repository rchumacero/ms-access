package com.kplian.msaccess.api.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class RoleResourceRequestDTO {

    @NotNull(message = "{dto.role_resource.role_id.required}")
    private UUID roleId;

    @NotNull(message = "{dto.role_resource.resource_id.required}")
    private UUID resourceId;

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public UUID getResourceId() {
        return resourceId;
    }

    public void setResourceId(UUID resourceId) {
        this.resourceId = resourceId;
    }
}
