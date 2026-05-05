# Postman Collection (Examples)

Archivo listo para importar en Postman: `POSTMAN_COLLECTION.json`

Base URL: `http://localhost:8080/api/v1`

## Access Menu

- `GET /access/menu/by-user/{userCode}`

## Profiles

- `GET /profiles`
- `GET /profiles/{id}`
- `POST /profiles`
- `PUT /profiles/{id}`
- `DELETE /profiles/{id}`

## Roles

- `GET /roles`
- `GET /roles/{id}`
- `POST /roles`
- `PUT /roles/{id}`
- `DELETE /roles/{id}`

## Resources

- `GET /resources`
- `GET /resources/{id}`
- `GET /resources/{id}/tree`
- `GET /resources/{id}/children`
- `POST /resources`
- `PUT /resources/{id}`
- `DELETE /resources/{id}`

## Local Translations

- `GET /local-translations`
- `GET /local-translations/{domain}/{entity}/{entityId}/{languageCode}`
- `POST /local-translations`
- `PUT /local-translations/{domain}/{entity}/{entityId}/{languageCode}`
- `DELETE /local-translations/{domain}/{entity}/{entityId}/{languageCode}`

## Interins

- `GET /interins`
- `GET /interins/{id}`
- `POST /interins`
- `PUT /interins/{id}`
- `DELETE /interins/{id}`

## User Profiles

- `GET /user-profiles`
- `GET /user-profiles/{id}`
- `GET /user-profiles/by-user/{userCode}/profiles`
- `POST /user-profiles`
- `PUT /user-profiles/{id}`
- `DELETE /user-profiles/{id}`

## Profile Roles

- `GET /profile-roles`
- `GET /profile-roles/{id}`
- `GET /profile-roles/by-profile/{profileId}/roles`
- `POST /profile-roles`
- `PUT /profile-roles/{id}`
- `DELETE /profile-roles/{id}`

## Role Resources

- `GET /role-resources`
- `GET /role-resources/{id}`
- `GET /role-resources/by-role/{roleId}/resources`
- `POST /role-resources/by-role/{roleId}/resources/{resourceId}/bulk?recursive=1`
- `DELETE /role-resources/by-role/{roleId}/resources/{resourceId}/bulk`
- `POST /role-resources`
- `PUT /role-resources/{id}`
- `DELETE /role-resources/{id}`
