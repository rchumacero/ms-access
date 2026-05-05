package com.kplian.msaccess.api.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public class InterinResponseDTO extends AuditResponseDTO {
    private UUID id;
    private String userCode;
    private String userInterinId;
    private LocalDate validFrom;
    private LocalDate validTo;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserInterinId() {
        return userInterinId;
    }

    public void setUserInterinId(String userInterinId) {
        this.userInterinId = userInterinId;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }
}
