package com.kplian.msaccess.api.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class ProfileRoleRequestDTO {

    @NotNull(message = "{dto.profile_role.profile_id.required}")
    private UUID profileId;

    @NotNull(message = "{dto.profile_role.role_id.required}")
    private UUID roleId;

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
