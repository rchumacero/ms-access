package com.kplian.msaccess.api.resource;

import com.kplian.msaccess.api.dto.request.UserProfileArrayRequestDTO;
import com.kplian.msaccess.api.dto.request.UserProfileRequestDTO;
import com.kplian.msaccess.api.dto.response.ProfileResponseDTO;
import com.kplian.msaccess.api.dto.response.UserProfileResponseDTO;
import com.kplian.msaccess.api.mapper.ProfileMapper;
import com.kplian.msaccess.api.mapper.UserProfileMapper;
import com.kplian.msaccess.domain.model.Profile;
import com.kplian.msaccess.domain.model.UserProfile;
import com.kplian.msaccess.domain.service.UserProfileService;
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

@Path("/user-profiles")
@ApplicationScoped
@Tag(name = "User Profiles", description = "User profile management API")
public class UserProfileResource {

    @Inject
    UserProfileService userProfileService;

    @Inject
    UserProfileMapper userProfileMapper;

    @Inject
    ProfileMapper profileMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all user profiles")
    public Response getAll() {
        List<UserProfile> userProfiles = userProfileService.findAll();
        List<UserProfileResponseDTO> dtos = userProfileMapper.toResponseDTOs(userProfiles);
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get user profile by ID")
    public Response getById(
        @Parameter(description = "User profile ID", required = true)
        @PathParam("id") UUID id
    ) {
        UserProfile userProfile = userProfileService.findById(id);
        UserProfileResponseDTO dto = userProfileMapper.toResponseDTO(userProfile);
        return Response.ok(dto).build();
    }

    @GET
    @Path("/by-user/{userCode}/profiles")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get profiles by user code")
    public Response getProfilesByUserCode(
        @Parameter(description = "User code", required = true)
        @PathParam("userCode") String userCode
    ) {
        List<Profile> profiles = userProfileService.findProfilesByUserCode(userCode);
        List<ProfileResponseDTO> dtos = profileMapper.toResponseDTOs(profiles);
        return Response.ok(dtos).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create a new user profile")
    public Response create(@Valid UserProfileRequestDTO requestDTO) {
        UserProfile userProfile = userProfileMapper.toEntity(requestDTO);
        UserProfile created = userProfileService.create(userProfile);
        UserProfileResponseDTO responseDTO = userProfileMapper.toResponseDTO(created);
        return Response.status(Response.Status.CREATED).entity(responseDTO).build();
    }

    @POST
    @Path("/create-by-array")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create user profiles by array of codes")
    public Response createByArray(@Valid UserProfileArrayRequestDTO requestDTO) {
        userProfileService.createByArray(requestDTO);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Update a user profile")
    public Response update(
        @Parameter(description = "User profile ID", required = true)
        @PathParam("id") UUID id,
        @Valid UserProfileRequestDTO requestDTO
    ) {
        UserProfile userProfile = userProfileMapper.toEntity(requestDTO);
        UserProfile updated = userProfileService.update(id, userProfile);
        UserProfileResponseDTO responseDTO = userProfileMapper.toResponseDTO(updated);
        return Response.ok(responseDTO).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a user profile")
    public Response delete(
        @Parameter(description = "User profile ID", required = true)
        @PathParam("id") UUID id
    ) {
        userProfileService.delete(id);
        return Response.noContent().build();
    }
}
