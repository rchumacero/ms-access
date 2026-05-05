package com.kplian.msaccess.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public class UserProfileRequestDTO {

    @NotBlank(message = "{dto.user_profile.user_code.required}")
    private String userCode;

    @NotNull(message = "{dto.user_profile.profile_id.required}")
    private UUID profileId;

    @NotNull(message = "{dto.user_profile.valid_from.required}")
    private LocalDate validFrom;

    private LocalDate validTo;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public UUID getProfileId() {
        return profileId;
    }

    public void setProfileId(UUID profileId) {
        this.profileId = profileId;
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
