package com.kplian.msaccess.api.dto.response;

import java.util.UUID;

public class RoleResourceResponseDTO extends AuditResponseDTO {
    private UUID id;
    private UUID roleId;
    private UUID resourceId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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
