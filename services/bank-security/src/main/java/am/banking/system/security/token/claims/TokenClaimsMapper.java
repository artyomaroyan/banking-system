package am.banking.system.security.token.claims;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Author: Artyom Aroyan
 * Date: 17.04.25
 * Time: 01:36:54
 */
@Component
public class TokenClaimsMapper {

    public Map<String, Object> mapTokenClaims(TokenClaimsDto claims) {
        return convertClaimsToMap(claims, Map.of(
                TokenClaimsConstant.USER_ID, TokenClaimsDto::userId,
                TokenClaimsConstant.USERNAME, TokenClaimsDto::username,
                TokenClaimsConstant.EMAIL, TokenClaimsDto::email,
                TokenClaimsConstant.USER_STATE, TokenClaimsDto::accountState,
                TokenClaimsConstant.USER_ROLES, TokenClaimsDto::roles,
                TokenClaimsConstant.USER_PERMISSIONS, TokenClaimsDto::permissions,
                TokenClaimsConstant.TOKEN_PURPOSE, TokenClaimsDto::tokenPurpose,
                TokenClaimsConstant.TOKEN_STATE, TokenClaimsDto::tokenState
        ));
    }

    private <T> Map<String, Object> convertClaimsToMap(T claims, Map<String, Function<T, Object>> claimsExtractor) {
        Map<String, Object> claimsMap = new HashMap<>();
        claimsExtractor.forEach((key, extractor) ->
                Optional.ofNullable(extractor.apply(claims))
                        .ifPresent(value -> claimsMap.put(key, value)));
        return claimsMap;
    }
}