package am.banking.system.common.shared.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Author: Artyom Aroyan
 * Date: 09.09.25
 * Time: 18:24:23
 */
public class JsonEventSerializer {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String serialize(Object event) {
        try {
            return mapper.writeValueAsString(event);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Failed to serialize event", ex);
        }
    }
}