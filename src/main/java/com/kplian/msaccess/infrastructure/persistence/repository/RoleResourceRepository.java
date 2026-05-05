package com.kplian.msaccess.infrastructure.persistence.repository;

import com.kplian.msaccess.domain.model.RoleResource;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class RoleResourceRepository implements PanacheRepository<RoleResource> {

    @Inject
    EntityManager entityManager;

    public Optional<RoleResource> findByIdOptional(UUID id) {
        return find("id = ?1 and deletedAt is null", id).firstResultOptional();
    }

    public List<RoleResource> findByRoleId(UUID roleId) {
        return find("roleId = ?1 and deletedAt is null", roleId).list();
    }

    public int insertRoleResourcesRecursive(UUID roleId, UUID rootResourceId, String createdBy) {
        String sql = """
            WITH RECURSIVE resource_tree_down AS (
                SELECT id, resource_id
                FROM tresource
                WHERE id = :rootResourceId
                  AND deleted_at IS NULL
                UNION ALL
                SELECT r.id, r.resource_id
                FROM tresource r
                JOIN resource_tree_down rt ON r.resource_id = rt.id
                WHERE r.deleted_at IS NULL
            ),
            resource_tree_up AS (
                SELECT id, resource_id
                FROM tresource
                WHERE id = :rootResourceId
                  AND deleted_at IS NULL
                UNION ALL
                SELECT r.id, r.resource_id
                FROM tresource r
                JOIN resource_tree_up rt ON rt.resource_id = r.id
                WHERE r.deleted_at IS NULL
            ),
            resource_tree AS (
                SELECT id FROM resource_tree_down
                UNION
                SELECT id FROM resource_tree_up
            )
            INSERT INTO trole_resource (id, role_id, resource_id, created_at, created_by, status)
            SELECT gen_random_uuid(), :roleId, rt.id, NOW(), :createdBy, 'ACTIVE'
            FROM resource_tree rt
            WHERE NOT EXISTS (
                SELECT 1
                FROM trole_resource rr
                WHERE rr.role_id = :roleId
                  AND rr.resource_id = rt.id
                  AND rr.deleted_at IS NULL
            )
            """;
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("roleId", roleId);
        query.setParameter("rootResourceId", rootResourceId);
        query.setParameter("createdBy", createdBy);
        return query.executeUpdate();
    }

    public int deleteRoleResourcesRecursive(UUID roleId, UUID rootResourceId, String deletedBy) {
        String sql = """
            WITH RECURSIVE resource_tree AS (
                SELECT id
                FROM tresource
                WHERE id = :rootResourceId
                  AND deleted_at IS NULL
                UNION ALL
                SELECT r.id
                FROM tresource r
                JOIN resource_tree rt ON r.resource_id = rt.id
                WHERE r.deleted_at IS NULL
            )
            UPDATE trole_resource rr
            SET deleted_at = NOW(),
                deleted_by = :deletedBy,
                status = 'DELETED'
            FROM resource_tree rt
            WHERE rr.role_id = :roleId
              AND rr.resource_id = rt.id
              AND rr.deleted_at IS NULL
            """;
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("roleId", roleId);
        query.setParameter("rootResourceId", rootResourceId);
        query.setParameter("deletedBy", deletedBy);
        return query.executeUpdate();
    }

    public int insertRoleResourcesWithParents(UUID roleId, UUID rootResourceId, String createdBy) {
        String sql = """
            WITH RECURSIVE resource_tree_up AS (
                SELECT id, resource_id
                FROM tresource
                WHERE id = :rootResourceId
                  AND deleted_at IS NULL
                UNION ALL
                SELECT r.id, r.resource_id
                FROM tresource r
                JOIN resource_tree_up rt ON rt.resource_id = r.id
                WHERE r.deleted_at IS NULL
            )
            INSERT INTO trole_resource (id, role_id, resource_id, created_at, created_by, status)
            SELECT gen_random_uuid(), :roleId, rt.id, NOW(), :createdBy, 'ACTIVE'
            FROM resource_tree_up rt
            WHERE NOT EXISTS (
                SELECT 1
                FROM trole_resource rr
                WHERE rr.role_id = :roleId
                  AND rr.resource_id = rt.id
                  AND rr.deleted_at IS NULL
            )
            """;
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("roleId", roleId);
        query.setParameter("rootResourceId", rootResourceId);
        query.setParameter("createdBy", createdBy);
        return query.executeUpdate();
    }

    public List<UUID> findResourceIdsByUserCode(String userCode) {
        String sql = """
            SELECT DISTINCT rr.resource_id
            FROM tuser_profile up
            JOIN tprofile_role pr
              ON pr.profile_id = up.profile_id
             AND pr.deleted_at IS NULL
            JOIN trole_resource rr
              ON rr.role_id = pr.role_id
             AND rr.deleted_at IS NULL
            WHERE up.user_code = CAST(:userCode AS varchar)
              AND up.deleted_at IS NULL
            """;
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("userCode", userCode);
        List<?> results = query.getResultList();
        List<UUID> resourceIds = new java.util.ArrayList<>();
        for (Object value : results) {
            if (value == null) {
                continue;
            }
            if (value instanceof UUID uuid) {
                resourceIds.add(uuid);
            } else {
                resourceIds.add(UUID.fromString(value.toString()));
            }
        }
        return resourceIds;
    }
}
