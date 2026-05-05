package com.kplian.msaccess.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class UserProfileArrayRequestDTO {

    @NotBlank(message = "{dto.user_profile.user_code.required}")
    private String userCode;

    @NotEmpty(message = "{dto.user_profile.profiles.required}")
    private List<String> profiles;

    @NotNull(message = "{dto.user_profile.valid_from.required}")
    private LocalDate validFrom;

    private LocalDate validTo;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public List<String> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<String> profiles) {
        this.profiles = profiles;
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
