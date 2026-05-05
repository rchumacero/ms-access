package com.kplian.msaccess.api.resource;

import com.kplian.msaccess.api.dto.request.ProfileRequestDTO;
import com.kplian.msaccess.api.dto.response.ProfileResponseDTO;
import com.kplian.msaccess.api.mapper.ProfileMapper;
import com.kplian.msaccess.domain.model.Profile;
import com.kplian.msaccess.domain.service.ProfileService;
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

@Path("/profiles")
@ApplicationScoped
@Tag(name = "Profiles", description = "Profile management API")
public class ProfileResource {

    @Inject
    ProfileService profileService;

    @Inject
    ProfileMapper profileMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all profiles")
    public Response getAll() {
        List<Profile> profiles = profileService.findAll();
        List<ProfileResponseDTO> dtos = profileMapper.toResponseDTOs(profiles);
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get profile by ID")
    public Response getById(
        @Parameter(description = "Profile ID", required = true)
        @PathParam("id") UUID id
    ) {
        Profile profile = profileService.findById(id);
        ProfileResponseDTO dto = profileMapper.toResponseDTO(profile);
        return Response.ok(dto).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create a new profile")
    public Response create(@Valid ProfileRequestDTO requestDTO) {
        Profile profile = profileMapper.toEntity(requestDTO);
        Profile created = profileService.create(profile);
        ProfileResponseDTO responseDTO = profileMapper.toResponseDTO(created);
        return Response.status(Response.Status.CREATED).entity(responseDTO).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update a profile")
    public Response update(
        @Parameter(description = "Profile ID", required = true)
        @PathParam("id") UUID id,
        @Valid ProfileRequestDTO requestDTO
    ) {
        Profile profile = profileMapper.toEntity(requestDTO);
        Profile updated = profileService.update(id, profile);
        ProfileResponseDTO responseDTO = profileMapper.toResponseDTO(updated);
        return Response.ok(responseDTO).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a profile")
    public Response delete(
        @Parameter(description = "Profile ID", required = true)
        @PathParam("id") UUID id
    ) {
        profileService.delete(id);
        return Response.noContent().build();
    }
}
