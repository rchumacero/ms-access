package com.kplian.msaccess.api.resource;

import com.kplian.msaccess.api.dto.request.RoleRequestDTO;
import com.kplian.msaccess.api.dto.response.RoleResponseDTO;
import com.kplian.msaccess.api.mapper.RoleMapper;
import com.kplian.msaccess.domain.model.Role;
import com.kplian.msaccess.domain.service.RoleService;
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

@Path("/roles")
@ApplicationScoped
@Tag(name = "Roles", description = "Role management API")
public class RoleResource {

    @Inject
    RoleService roleService;

    @Inject
    RoleMapper roleMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all roles")
    public Response getAll() {
        List<Role> roles = roleService.findAll();
        List<RoleResponseDTO> dtos = roleMapper.toResponseDTOs(roles);
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get role by ID")
    public Response getById(
        @Parameter(description = "Role ID", required = true)
        @PathParam("id") UUID id
    ) {
        Role role = roleService.findById(id);
        RoleResponseDTO dto = roleMapper.toResponseDTO(role);
        return Response.ok(dto).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create a new role")
    public Response create(@Valid RoleRequestDTO requestDTO) {
        Role role = roleMapper.toEntity(requestDTO);
        Role created = roleService.create(role);
        RoleResponseDTO responseDTO = roleMapper.toResponseDTO(created);
        return Response.status(Response.Status.CREATED).entity(responseDTO).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update a role")
    public Response update(
        @Parameter(description = "Role ID", required = true)
        @PathParam("id") UUID id,
        @Valid RoleRequestDTO requestDTO
    ) {
        Role role = roleMapper.toEntity(requestDTO);
        Role updated = roleService.update(id, role);
        RoleResponseDTO responseDTO = roleMapper.toResponseDTO(updated);
        return Response.ok(responseDTO).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a role")
    public Response delete(
        @Parameter(description = "Role ID", required = true)
        @PathParam("id") UUID id
    ) {
        roleService.delete(id);
        return Response.noContent().build();
    }
}
