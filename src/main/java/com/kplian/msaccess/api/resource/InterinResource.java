package com.kplian.msaccess.api.resource;

import com.kplian.msaccess.api.dto.request.InterinRequestDTO;
import com.kplian.msaccess.api.dto.response.InterinResponseDTO;
import com.kplian.msaccess.api.mapper.InterinMapper;
import com.kplian.msaccess.domain.model.Interin;
import com.kplian.msaccess.domain.service.InterinService;
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

@Path("/interins")
@ApplicationScoped
@Tag(name = "Interins", description = "Interin management API")
public class InterinResource {

    @Inject
    InterinService interinService;

    @Inject
    InterinMapper interinMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all interins")
    public Response getAll() {
        List<Interin> interins = interinService.findAll();
        List<InterinResponseDTO> dtos = interinMapper.toResponseDTOs(interins);
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get interin by ID")
    public Response getById(
        @Parameter(description = "Interin ID", required = true)
        @PathParam("id") UUID id
    ) {
        Interin interin = interinService.findById(id);
        InterinResponseDTO dto = interinMapper.toResponseDTO(interin);
        return Response.ok(dto).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create a new interin")
    public Response create(@Valid InterinRequestDTO requestDTO) {
        Interin interin = interinMapper.toEntity(requestDTO);
        Interin created = interinService.create(interin);
        InterinResponseDTO responseDTO = interinMapper.toResponseDTO(created);
        return Response.status(Response.Status.CREATED).entity(responseDTO).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update an interin")
    public Response update(
        @Parameter(description = "Interin ID", required = true)
        @PathParam("id") UUID id,
        @Valid InterinRequestDTO requestDTO
    ) {
        Interin interin = interinMapper.toEntity(requestDTO);
        Interin updated = interinService.update(id, interin);
        InterinResponseDTO responseDTO = interinMapper.toResponseDTO(updated);
        return Response.ok(responseDTO).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete an interin")
    public Response delete(
        @Parameter(description = "Interin ID", required = true)
        @PathParam("id") UUID id
    ) {
        interinService.delete(id);
        return Response.noContent().build();
    }
}
