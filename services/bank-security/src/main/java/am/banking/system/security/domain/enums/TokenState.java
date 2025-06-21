package am.banking.system.security.domain.enums;

/**
 * Author: Artyom Aroyan
 * Date: 16.04.25
 * Time: 23:27:26
 */
public enum TokenState {
    PENDING,
    VERIFIED,
    FAILED,
    EXPIRED,
    FORCIBLY_EXPIRED
}