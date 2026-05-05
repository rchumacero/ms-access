package com.kplian.msaccess.api.resource;

import com.kplian.msaccess.api.dto.request.LocalTranslationRequestDTO;
import com.kplian.msaccess.api.dto.response.LocalTranslationResponseDTO;
import com.kplian.msaccess.api.mapper.LocalTranslationMapper;
import com.kplian.msaccess.domain.model.LocalTranslation;
import com.kplian.msaccess.domain.model.LocalTranslationId;
import com.kplian.msaccess.domain.service.LocalTranslationService;
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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/local-translations")
@ApplicationScoped
@Tag(name = "Local Translations", description = "Local translation management API")
public class LocalTranslationResource {

    @Inject
    LocalTranslationService localTranslationService;

    @Inject
    LocalTranslationMapper localTranslationMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all local translations")
    public Response getAll() {
        List<LocalTranslation> translations = localTranslationService.findAll();
        List<LocalTranslationResponseDTO> dtos = localTranslationMapper.toResponseDTOs(translations);
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/{domain}/{entity}/{entityId}/{languageCode}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get local translation by ID")
    public Response getById(
        @Parameter(description = "Domain", required = true) @PathParam("domain") String domain,
        @Parameter(description = "Entity", required = true) @PathParam("entity") String entity,
        @Parameter(description = "Entity ID", required = true) @PathParam("entityId") String entityId,
        @Parameter(description = "Language code", required = true) @PathParam("languageCode") String languageCode
    ) {
        LocalTranslationId id = new LocalTranslationId(domain, entity, entityId, languageCode);
        LocalTranslation translation = localTranslationService.findById(id);
        LocalTranslationResponseDTO dto = localTranslationMapper.toResponseDTO(translation);
        return Response.ok(dto).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create a local translation")
    public Response create(@Valid LocalTranslationRequestDTO requestDTO) {
        LocalTranslation translation = localTranslationMapper.toEntity(requestDTO);
        LocalTranslation created = localTranslationService.create(translation);
        LocalTranslationResponseDTO responseDTO = localTranslationMapper.toResponseDTO(created);
        return Response.status(Response.Status.CREATED).entity(responseDTO).build();
    }

    @PUT
    @Path("/{domain}/{entity}/{entityId}/{languageCode}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update a local translation")
    public Response update(
        @Parameter(description = "Domain", required = true) @PathParam("domain") String domain,
        @Parameter(description = "Entity", required = true) @PathParam("entity") String entity,
        @Parameter(description = "Entity ID", required = true) @PathParam("entityId") String entityId,
        @Parameter(description = "Language code", required = true) @PathParam("languageCode") String languageCode,
        @Valid LocalTranslationRequestDTO requestDTO
    ) {
        LocalTranslationId id = new LocalTranslationId(domain, entity, entityId, languageCode);
        LocalTranslation translation = localTranslationMapper.toEntity(requestDTO, id);
        LocalTranslation updated = localTranslationService.update(id, translation);
        LocalTranslationResponseDTO responseDTO = localTranslationMapper.toResponseDTO(updated);
        return Response.ok(responseDTO).build();
    }

    @DELETE
    @Path("/{domain}/{entity}/{entityId}/{languageCode}")
    @Operation(summary = "Delete a local translation")
    public Response delete(
        @Parameter(description = "Domain", required = true) @PathParam("domain") String domain,
        @Parameter(description = "Entity", required = true) @PathParam("entity") String entity,
        @Parameter(description = "Entity ID", required = true) @PathParam("entityId") String entityId,
        @Parameter(description = "Language code", required = true) @PathParam("languageCode") String languageCode
    ) {
        LocalTranslationId id = new LocalTranslationId(domain, entity, entityId, languageCode);
        localTranslationService.delete(id);
        return Response.noContent().build();
    }
}
