package com.kplian.msaccess.api.resource;

import com.kplian.msaccess.api.dto.request.AppRequestDTO;
import com.kplian.msaccess.api.dto.response.AppResponseDTO;
import com.kplian.msaccess.api.mapper.AppMapper;
import com.kplian.msaccess.domain.model.App;
import com.kplian.msaccess.domain.service.AppService;
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

@Path("/apps")
@ApplicationScoped
@Tag(name = "Apps", description = "App management API")
public class AppResource {

    @Inject
    AppService appService;

    @Inject
    AppMapper appMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all apps")
    public Response getAll() {
        List<App> apps = appService.findAll();
        List<AppResponseDTO> dtos = appMapper.toResponseDTOs(apps);
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get app by ID")
    public Response getById(
        @Parameter(description = "App ID", required = true)
        @PathParam("id") UUID id
    ) {
        App app = appService.findById(id);
        AppResponseDTO dto = appMapper.toResponseDTO(app);
        return Response.ok(dto).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create a new app")
    public Response create(@Valid AppRequestDTO requestDTO) {
        App app = appMapper.toEntity(requestDTO);
        App created = appService.create(app);
        AppResponseDTO responseDTO = appMapper.toResponseDTO(created);
        return Response.status(Response.Status.CREATED).entity(responseDTO).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update an app")
    public Response update(
        @Parameter(description = "App ID", required = true)
        @PathParam("id") UUID id,
        @Valid AppRequestDTO requestDTO
    ) {
        App app = appMapper.toEntity(requestDTO);
        App updated = appService.update(id, app);
        AppResponseDTO responseDTO = appMapper.toResponseDTO(updated);
        return Response.ok(responseDTO).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete an app")
    public Response delete(
        @Parameter(description = "App ID", required = true)
        @PathParam("id") UUID id
    ) {
        appService.delete(id);
        return Response.noContent().build();
    }
}
