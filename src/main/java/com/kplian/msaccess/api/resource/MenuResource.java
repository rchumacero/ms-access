package com.kplian.msaccess.api.resource;

import com.kplian.msaccess.api.dto.request.MenuRequestDTO;
import com.kplian.msaccess.api.dto.response.MenuResponseDTO;
import com.kplian.msaccess.api.mapper.MenuMapper;
import com.kplian.msaccess.domain.model.Menu;
import com.kplian.msaccess.domain.service.MenuService;
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

@Path("/menus")
@ApplicationScoped
@Tag(name = "Menus", description = "Menu management API")
public class MenuResource {

    @Inject
    MenuService menuService;

    @Inject
    MenuMapper menuMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all menus")
    public Response getAll() {
        List<Menu> menus = menuService.findAll();
        List<MenuResponseDTO> dtos = menuMapper.toResponseDTOs(menus);
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get menu by ID")
    public Response getById(
        @Parameter(description = "Menu ID", required = true)
        @PathParam("id") UUID id
    ) {
        Menu menu = menuService.findById(id);
        MenuResponseDTO dto = menuMapper.toResponseDTO(menu);
        return Response.ok(dto).build();
    }

    @GET
    @Path("/by-app/{appId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get menus by App ID")
    public Response getByAppId(
        @Parameter(description = "App ID", required = true)
        @PathParam("appId") UUID appId
    ) {
        List<Menu> menus = menuService.findByAppId(appId);
        List<MenuResponseDTO> dtos = menuMapper.toResponseDTOs(menus);
        return Response.ok(dtos).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create a new menu")
    public Response create(@Valid MenuRequestDTO requestDTO) {
        Menu menu = menuMapper.toEntity(requestDTO);
        Menu created = menuService.create(requestDTO.getAppId(), menu);
        MenuResponseDTO responseDTO = menuMapper.toResponseDTO(created);
        return Response.status(Response.Status.CREATED).entity(responseDTO).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update a menu")
    public Response update(
        @Parameter(description = "Menu ID", required = true)
        @PathParam("id") UUID id,
        @Valid MenuRequestDTO requestDTO
    ) {
        Menu menu = menuMapper.toEntity(requestDTO);
        Menu updated = menuService.update(id, menu);
        MenuResponseDTO responseDTO = menuMapper.toResponseDTO(updated);
        return Response.ok(responseDTO).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a menu")
    public Response delete(
        @Parameter(description = "Menu ID", required = true)
        @PathParam("id") UUID id
    ) {
        menuService.delete(id);
        return Response.noContent().build();
    }
}
