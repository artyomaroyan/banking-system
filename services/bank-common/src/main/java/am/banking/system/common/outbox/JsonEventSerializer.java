package am.banking.system.common.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Author: Artyom Aroyan
 * Date: 09.09.25
 * Time: 18:24:23
 */
public final class JsonEventSerializer {
    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    private JsonEventSerializer() {
    }

    public static String serialize(Object event) {
        try {
            return MAPPER.writeValueAsString(event);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Failed to serialize events", ex);
        }
    }
}