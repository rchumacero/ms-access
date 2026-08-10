package com.kplian.msaccess.api.resource;

import com.kplian.msaccess.api.dto.request.ResourceRequestDTO;
import com.kplian.msaccess.api.dto.request.CloneResourceRequestDTO;
import com.kplian.msaccess.api.dto.response.ResourceResponseDTO;
import com.kplian.msaccess.api.dto.response.ResourceTreeSlimResponseDTO;
import com.kplian.msaccess.api.mapper.ResourceMapper;
import com.kplian.msaccess.domain.model.Resource;
import com.kplian.msaccess.domain.service.ResourceService;
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

@Path("/resources")
@ApplicationScoped
@Tag(name = "Resources", description = "Resource management API")
public class ResourceResource {

    private static final String TRANSLATION_DOMAIN = "access";
    private static final String TRANSLATION_ENTITY = "resource";

    @Inject
    ResourceService resourceService;

    @Inject
    ResourceMapper resourceMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all resources")
    public Response getAll() {
        List<Resource> resources = resourceService.findAll();
        java.util.Map<String, java.util.Map<String, Object>> translations = resourceService
                .getTranslationsForResources(resources, TRANSLATION_DOMAIN, TRANSLATION_ENTITY);
        List<ResourceResponseDTO> dtos = resourceMapper.toResponseDTOs(resources, translations);
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get resource by ID")
    public Response getById(
            @Parameter(description = "Resource ID", required = true) @PathParam("id") UUID id) {
        Resource resource = resourceService.findById(id);
        java.util.Map<String, java.util.Map<String, Object>> translations = resourceService
                .getTranslationsForResources(List.of(resource), TRANSLATION_DOMAIN, TRANSLATION_ENTITY);
        java.util.Map<String, Object> translation = translations.get(resource.getId().toString());
        ResourceResponseDTO dto = resourceMapper.toResponseDTO(resource, translation);
        return Response.ok(dto).build();
    }

    @GET
    @Path("/{id}/tree")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get resource tree by ID")
    public Response getTreeById(
            @Parameter(description = "Resource ID", required = true) @PathParam("id") UUID id) {
        Resource root = resourceService.findById(id);
        List<Resource> allResources = resourceService.findAll();
        java.util.Map<String, java.util.Map<String, Object>> translations = resourceService
                .getTranslationsForResources(allResources, TRANSLATION_DOMAIN, TRANSLATION_ENTITY);
        ResourceTreeSlimResponseDTO tree = resourceMapper.toSlimTree(root, allResources, translations);
        return Response.ok(tree).build();
    }

    @GET
    @Path("/{id}/children")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get resource children by ID")
    public Response getChildrenById(
            @Parameter(description = "Resource ID", required = true) @PathParam("id") UUID id) {
        List<Resource> children = resourceService.findChildren(id);
        java.util.Map<String, java.util.Map<String, Object>> translations = resourceService
                .getTranslationsForResources(children, TRANSLATION_DOMAIN, TRANSLATION_ENTITY);
        List<ResourceResponseDTO> dtos = resourceMapper.toResponseDTOs(children, translations);
        return Response.ok(dtos).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create a new resource")
    public Response create(@Valid ResourceRequestDTO requestDTO) {
        Resource resource = resourceMapper.toEntity(requestDTO);
        Resource created = resourceService.create(resource, requestDTO.getMenuId());
        ResourceResponseDTO responseDTO = resourceMapper.toResponseDTO(created);
        return Response.status(Response.Status.CREATED).entity(responseDTO).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update a resource")
    public Response update(
            @Parameter(description = "Resource ID", required = true) @PathParam("id") UUID id,
            @Valid ResourceRequestDTO requestDTO) {
        Resource resource = resourceMapper.toEntity(requestDTO);
        Resource updated = resourceService.update(id, resource, requestDTO.getMenuId());
        ResourceResponseDTO responseDTO = resourceMapper.toResponseDTO(updated);
        return Response.ok(responseDTO).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a resource")
    public Response delete(
            @Parameter(description = "Resource ID", required = true) @PathParam("id") UUID id) {
        resourceService.delete(id);
        return Response.noContent().build();
    }

    @POST
    @Path("/{resourceId}/clone")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Clone recursively menu structure from resourceId")
    public Response clone(
            @Parameter(description = "Resource ID to clone", required = true) @PathParam("resourceId") UUID resourceId,
            @Valid CloneResourceRequestDTO requestDTO) {
        Resource clonedRoot = resourceService.cloneHierarchy(resourceId, requestDTO.getMenuId());
        ResourceResponseDTO responseDTO = resourceMapper.toResponseDTO(clonedRoot);
        return Response.status(Response.Status.CREATED).entity(responseDTO).build();
    }
}
