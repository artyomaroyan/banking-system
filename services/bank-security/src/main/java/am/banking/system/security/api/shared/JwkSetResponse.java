package am.banking.system.security.api.shared;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Author: Artyom Aroyan
 * Date: 24.06.25
 * Time: 00:04:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwkSetResponse {
    private List<Map<String, Object>> keys;
}