package com.kplian.msaccess.infrastructure.messaging;

import com.kplian.msaccess.domain.model.LocalTranslationId;
import com.kplian.msaccess.domain.service.LocalTranslationService;
import io.smallrye.common.annotation.Blocking;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class TranslationKafkaConsumer {

    private static final String ACTION_ACCESS_CREATED = "translation.access.created";
    private static final String ACTION_ACCESS_UPDATED = "translation.access.updated";
    private static final String ACTION_PARAMETER_CREATED = "translation.parameter.created";
    private static final String ACTION_PARAMETER_UPDATED = "translation.parameter.updated";

    @Inject
    LocalTranslationService localTranslationService;

    @Incoming("translation-events")
    @Blocking
    @Transactional
    public void onMessage(String message) {
        if (message == null || message.isBlank()) {
            return;
        }
        JsonObject payload = new JsonObject(message);
        String action = payload.getString("action");
        if (action != null
            && !ACTION_ACCESS_CREATED.equals(action)
            && !ACTION_ACCESS_UPDATED.equals(action)
            && !ACTION_PARAMETER_CREATED.equals(action)
            && !ACTION_PARAMETER_UPDATED.equals(action)) {
            return;
        }
        LocalTranslationId id = buildId(payload);
        String text = parseText(payload);
        if (id == null || text == null) {
            return;
        }
        localTranslationService.upsert(id, text);
    }

    private LocalTranslationId buildId(JsonObject payload) {
        String domain = payload.getString("domain");
        String entity = payload.getString("entity");
        String entityId = parseEntityId(payload);
        String languageCode = payload.getString("languageCode");
        if (domain == null || entity == null || entityId == null || languageCode == null) {
            return null;
        }
        return new LocalTranslationId(domain, entity, entityId, languageCode);
    }

    private String parseEntityId(JsonObject payload) {
        Object raw = payload.getValue("entityId");
        if (raw == null) {
            return null;
        }
        if (raw instanceof Number) {
            return String.valueOf(((Number) raw).longValue());
        }
        if (raw instanceof String) {
            String value = ((String) raw).trim();
            return value.isEmpty() ? null : value;
        }
        return raw.toString();
    }

    private String parseText(JsonObject payload) {
        Object raw = payload.getValue("text");
        if (raw == null) {
            return null;
        }
        if (raw instanceof String) {
            return (String) raw;
        }
        if (raw instanceof JsonObject) {
            return ((JsonObject) raw).encode();
        }
        if (raw instanceof java.util.Map<?, ?> map) {
            JsonObject obj = new JsonObject();
            map.forEach((key, value) -> {
                if (key != null) {
                    obj.put(key.toString(), value);
                }
            });
            return obj.encode();
        }
        return raw.toString();
    }
}
