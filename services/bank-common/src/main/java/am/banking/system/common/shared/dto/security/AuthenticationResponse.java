package am.banking.system.common.shared.dto.security;

import java.math.BigDecimal;

/**
 * Author: Artyom Aroyan
 * Date: 08.08.25
 * Time: 23:30:13
 */
public record AuthenticationResponse(String username, String accountNumber, BigDecimal balance) {
}