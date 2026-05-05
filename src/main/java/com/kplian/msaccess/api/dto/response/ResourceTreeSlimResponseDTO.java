package com.kplian.msaccess.api.dto.response;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ResourceTreeSlimResponseDTO {
    private UUID id;
    private String code;
    private String description;
    private String type;
    private String name;
    private Boolean restricted;
    private String endpoint;
    private List<ResourceTreeSlimResponseDTO> children = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getRestricted() {
        return restricted;
    }

    public void setRestricted(Boolean restricted) {
        this.restricted = restricted;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public List<ResourceTreeSlimResponseDTO> getChildren() {
        return children;
    }

    public void setChildren(List<ResourceTreeSlimResponseDTO> children) {
        this.children = children;
    }
}
