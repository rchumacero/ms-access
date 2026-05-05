package com.kplian.msaccess.infrastructure.persistence.repository;

import com.kplian.msaccess.domain.model.LocalTranslation;
import com.kplian.msaccess.domain.model.LocalTranslationId;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class LocalTranslationRepository implements PanacheRepository<LocalTranslation> {

    public Optional<LocalTranslation> findByIdOptional(LocalTranslationId id) {
        return find("id = ?1", id).firstResultOptional();
    }

    public List<LocalTranslation> findByDomainEntityLanguageAndEntityIds(
        String domain,
        String entity,
        String languageCode,
        List<String> entityIds
    ) {
        return find(
            "id.domain = ?1 and id.entityName = ?2 and id.languageCode = ?3 and id.entityId in ?4",
            domain,
            entity,
            languageCode,
            entityIds
        ).list();
    }
}
