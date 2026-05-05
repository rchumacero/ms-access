package com.kplian.msaccess.api.resource;

import com.kplian.msaccess.api.dto.request.ProfileRoleRequestDTO;
import com.kplian.msaccess.api.dto.response.ProfileRoleResponseDTO;
import com.kplian.msaccess.api.dto.response.RoleResponseDTO;
import com.kplian.msaccess.api.mapper.ProfileRoleMapper;
import com.kplian.msaccess.api.mapper.RoleMapper;
import com.kplian.msaccess.domain.model.ProfileRole;
import com.kplian.msaccess.domain.model.Role;
import com.kplian.msaccess.domain.service.ProfileRoleService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/profile-roles")
@ApplicationScoped
@Tag(name = "Profile Roles", description = "Profile role management API")
public class ProfileRoleResource {

    @Inject
    ProfileRoleService profileRoleService;

    @Inject
    ProfileRoleMapper profileRoleMapper;

    @Inject
    RoleMapper roleMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all profile roles")
    public Response getAll() {
        List<ProfileRole> profileRoles = profileRoleService.findAll();
        List<ProfileRoleResponseDTO> dtos = profileRoleMapper.toResponseDTOs(profileRoles);
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get profile role by ID")
    public Response getById(
        @Parameter(description = "Profile role ID", required = true)
        @PathParam("id") UUID id
    ) {
        ProfileRole profileRole = profileRoleService.findById(id);
        ProfileRoleResponseDTO dto = profileRoleMapper.toResponseDTO(profileRole);
        return Response.ok(dto).build();
    }

    @GET
    @Path("/by-profile/{profileId}/roles")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get roles by profile ID")
    public Response getRolesByProfileId(
        @Parameter(description = "Profile ID", required = true)
        @PathParam("profileId") UUID profileId
    ) {
        List<Role> roles = profileRoleService.findRolesByProfileId(profileId);
        List<RoleResponseDTO> dtos = roleMapper.toResponseDTOs(roles);
        return Response.ok(dtos).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create a new profile role")
    public Response create(@Valid ProfileRoleRequestDTO requestDTO) {
        ProfileRole profileRole = profileRoleMapper.toEntity(requestDTO);
        ProfileRole created = profileRoleService.create(profileRole);
        ProfileRoleResponseDTO responseDTO = profileRoleMapper.toResponseDTO(created);
        return Response.status(Response.Status.CREATED).entity(responseDTO).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update a profile role")
    public Response update(
        @Parameter(description = "Profile role ID", required = true)
        @PathParam("id") UUID id,
        @Valid ProfileRoleRequestDTO requestDTO
    ) {
        ProfileRole profileRole = profileRoleMapper.toEntity(requestDTO);
        ProfileRole updated = profileRoleService.update(id, profileRole);
        ProfileRoleResponseDTO responseDTO = profileRoleMapper.toResponseDTO(updated);
        return Response.ok(responseDTO).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a profile role")
    public Response delete(
        @Parameter(description = "Profile role ID", required = true)
        @PathParam("id") UUID id
    ) {
        profileRoleService.delete(id);
        return Response.noContent().build();
    }
}
