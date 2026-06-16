package com.kplian.msaccess.api.resource;

import com.kplian.msaccess.api.dto.response.ResourceTreeSlimResponseDTO;
import com.kplian.msaccess.api.mapper.ResourceMapper;
import com.kplian.msaccess.domain.model.Resource;
import com.kplian.msaccess.domain.service.AccessMenuService;
import com.kplian.msaccess.domain.service.ResourceService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/access")
@ApplicationScoped
@Tag(name = "Access Menu", description = "Access menu API")
public class AccessMenuResource {

    private static final String TRANSLATION_DOMAIN = "access";
    private static final String TRANSLATION_ENTITY = "resource";

    @Inject
    AccessMenuService accessMenuService;

    @Inject
    ResourceService resourceService;

    @Inject
    ResourceMapper resourceMapper;

    @GET
    @Path("/menu/by-user/{userCode}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get access menu tree by user code")
    public Response getMenuByUserCode(
        @Parameter(description = "User code", required = true)
        @PathParam("userCode") String userCode
    ) {
        List<Resource> allResources = accessMenuService.findAllResources();
        Set<UUID> allowedIds = accessMenuService.findAllowedResourceIdsByUserCode(userCode);
        java.util.Map<String, java.util.Map<String, Object>> translations =
            resourceService.getTranslationsForResources(allResources, TRANSLATION_DOMAIN, TRANSLATION_ENTITY);
        List<ResourceTreeSlimResponseDTO> tree =
            resourceMapper.toSlimTreeForest(allResources, translations, allowedIds);
        return Response.ok(tree).build();
    }

    @GET
    @Path("/menu/by-user/{userCode}/{menuCode}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get access menu tree by user code and menu code")
    public Response getMenuByUserCodeAndMenuCode(
        @Parameter(description = "User code", required = true)
        @PathParam("userCode") String userCode,
        @Parameter(description = "Menu code", required = true)
        @PathParam("menuCode") String menuCode
    ) {
        List<Resource> filteredResources = accessMenuService.findResourcesByMenuCode(menuCode);
        Set<UUID> allowedIds = accessMenuService.findAllowedResourceIdsByUserCode(userCode);
        java.util.Map<String, java.util.Map<String, Object>> translations =
            resourceService.getTranslationsForResources(filteredResources, TRANSLATION_DOMAIN, TRANSLATION_ENTITY);
        List<ResourceTreeSlimResponseDTO> tree =
            resourceMapper.toSlimTreeForest(filteredResources, translations, allowedIds);
        return Response.ok(tree).build();
    }
}
