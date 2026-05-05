package com.kplian.msaccess.api.resource;

import com.kplian.msaccess.api.dto.request.RoleResourceRequestDTO;
import com.kplian.msaccess.api.dto.response.BulkRoleResourceResponseDTO;
import com.kplian.msaccess.api.dto.response.ResourceResponseDTO;
import com.kplian.msaccess.api.dto.response.RoleResourceResponseDTO;
import com.kplian.msaccess.api.mapper.RoleResourceMapper;
import com.kplian.msaccess.api.mapper.ResourceMapper;
import com.kplian.msaccess.domain.model.ResourceEntity;
import com.kplian.msaccess.domain.model.RoleResource;
import com.kplian.msaccess.domain.service.RoleResourceService;
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
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/role-resources")
@ApplicationScoped
@Tag(name = "Role Resources", description = "Role resource management API")
public class RoleResourceLinkResource {

    @Inject
    RoleResourceService roleResourceService;

    @Inject
    RoleResourceMapper roleResourceMapper;

    @Inject
    ResourceMapper resourceMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all role resources")
    public Response getAll() {
        List<RoleResource> roleResources = roleResourceService.findAll();
        List<RoleResourceResponseDTO> dtos = roleResourceMapper.toResponseDTOs(roleResources);
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get role resource by ID")
    public Response getById(
        @Parameter(description = "Role resource ID", required = true)
        @PathParam("id") UUID id
    ) {
        RoleResource roleResource = roleResourceService.findById(id);
        RoleResourceResponseDTO dto = roleResourceMapper.toResponseDTO(roleResource);
        return Response.ok(dto).build();
    }

    @GET
    @Path("/by-role/{roleId}/resources")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get resources by role ID")
    public Response getResourcesByRoleId(
        @Parameter(description = "Role ID", required = true)
        @PathParam("roleId") UUID roleId
    ) {
        List<ResourceEntity> resources = roleResourceService.findResourcesByRoleId(roleId);
        java.util.Map<String, java.util.Map<String, Object>> translations =
            roleResourceService.getTranslationsForResources(resources);
        List<ResourceResponseDTO> dtos = resourceMapper.toResponseDTOs(resources, translations);
        return Response.ok(dtos).build();
    }

    @POST
    @Path("/by-role/{roleId}/resources/{resourceId}/bulk")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Bulk create role resources by role and root resource")
    public Response bulkCreateRoleResources(
        @Parameter(description = "Role ID", required = true)
        @PathParam("roleId") UUID roleId,
        @Parameter(description = "Root Resource ID", required = true)
        @PathParam("resourceId") UUID resourceId,
        @Parameter(description = "Recursive (0=false,1=true)", required = false)
        @QueryParam("recursive") Integer recursive
    ) {
        boolean isRecursive = recursive == null || recursive == 1;
        int insertedCount = isRecursive
            ? roleResourceService.createRoleResourcesRecursive(roleId, resourceId)
            : roleResourceService.createRoleResourcesWithParents(roleId, resourceId);
        BulkRoleResourceResponseDTO responseDTO = new BulkRoleResourceResponseDTO(roleId, resourceId, insertedCount);
        return Response.status(Response.Status.CREATED).entity(responseDTO).build();
    }

    @DELETE
    @Path("/by-role/{roleId}/resources/{resourceId}/bulk")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Bulk delete role resources by role and root resource")
    public Response bulkDeleteRoleResources(
        @Parameter(description = "Role ID", required = true)
        @PathParam("roleId") UUID roleId,
        @Parameter(description = "Root Resource ID", required = true)
        @PathParam("resourceId") UUID resourceId
    ) {
        int deletedCount = roleResourceService.deleteRoleResourcesRecursive(roleId, resourceId);
        BulkRoleResourceResponseDTO responseDTO = new BulkRoleResourceResponseDTO(roleId, resourceId, deletedCount);
        return Response.ok(responseDTO).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create a new role resource")
    public Response create(@Valid RoleResourceRequestDTO requestDTO) {
        RoleResource roleResource = roleResourceMapper.toEntity(requestDTO);
        RoleResource created = roleResourceService.create(roleResource);
        RoleResourceResponseDTO responseDTO = roleResourceMapper.toResponseDTO(created);
        return Response.status(Response.Status.CREATED).entity(responseDTO).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update a role resource")
    public Response update(
        @Parameter(description = "Role resource ID", required = true)
        @PathParam("id") UUID id,
        @Valid RoleResourceRequestDTO requestDTO
    ) {
        RoleResource roleResource = roleResourceMapper.toEntity(requestDTO);
        RoleResource updated = roleResourceService.update(id, roleResource);
        RoleResourceResponseDTO responseDTO = roleResourceMapper.toResponseDTO(updated);
        return Response.ok(responseDTO).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a role resource")
    public Response delete(
        @Parameter(description = "Role resource ID", required = true)
        @PathParam("id") UUID id
    ) {
        roleResourceService.delete(id);
        return Response.noContent().build();
    }
}
