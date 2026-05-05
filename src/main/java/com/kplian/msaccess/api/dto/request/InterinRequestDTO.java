package com.kplian.msaccess.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class InterinRequestDTO {

    @NotBlank(message = "{dto.interin.user_code.required}")
    private String userCode;

    @NotBlank(message = "{dto.interin.user_interin_id.required}")
    private String userInterinId;

    @NotNull(message = "{dto.interin.valid_from.required}")
    private LocalDate validFrom;

    private LocalDate validTo;

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
