package com.kplian.msaccess.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RoleRequestDTO {

    @NotBlank(message = "{dto.role.code.required}")
    @Size(min = 3, max = 50, message = "{dto.role.code.size}")
    private String code;

    @NotBlank(message = "{dto.role.name.required}")
    @Size(min = 3, max = 255, message = "{dto.role.name.size}")
    private String name;

    @NotBlank(message = "{dto.role.module_code.required}")
    @Size(min = 2, max = 50, message = "{dto.role.module_code.size}")
    private String moduleCode;

    private String vendorCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }
}
