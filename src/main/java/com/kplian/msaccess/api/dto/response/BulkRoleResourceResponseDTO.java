package com.kplian.msaccess.api.dto.response;

import java.util.UUID;

public class BulkRoleResourceResponseDTO {
    private UUID roleId;
    private UUID rootResourceId;
    private int insertedCount;

    public BulkRoleResourceResponseDTO() {
    }

    public BulkRoleResourceResponseDTO(UUID roleId, UUID rootResourceId, int insertedCount) {
        this.roleId = roleId;
        this.rootResourceId = rootResourceId;
        this.insertedCount = insertedCount;
    }

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public UUID getRootResourceId() {
        return rootResourceId;
    }

    public void setRootResourceId(UUID rootResourceId) {
        this.rootResourceId = rootResourceId;
    }

    public int getInsertedCount() {
        return insertedCount;
    }

    public void setInsertedCount(int insertedCount) {
        this.insertedCount = insertedCount;
    }
}
