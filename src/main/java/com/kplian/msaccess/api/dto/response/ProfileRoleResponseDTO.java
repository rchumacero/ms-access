package com.kplian.msaccess.api.dto.response;

import java.util.UUID;

public class ProfileRoleResponseDTO extends AuditResponseDTO {
    private UUID id;
    private UUID profileId;
    private UUID roleId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProfileId() {
        return profileId;
    }

    public void setProfileId(UUID profileId) {
        this.profileId = profileId;
    }

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }
}
