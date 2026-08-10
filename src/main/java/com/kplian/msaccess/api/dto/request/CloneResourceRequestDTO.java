package com.kplian.msaccess.api.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class CloneResourceRequestDTO {

    @NotNull(message = "{dto.clone.menu_id.required}")
    private UUID menuId;

    public UUID getMenuId() {
        return menuId;
    }

    public void setMenuId(UUID menuId) {
        this.menuId = menuId;
    }
}
