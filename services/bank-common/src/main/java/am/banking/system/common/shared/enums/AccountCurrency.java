package am.banking.system.common.shared.enums;

import lombok.Getter;

/**
 * Author: Artyom Aroyan
 * Date: 22.07.25
 * Time: 14:06:55
 */
@Getter
public enum AccountCurrency {
    AMD("00"),
    USD("01"),
    EUR("02"),
    GBP("03");

    private final String suffix;

    AccountCurrency(String suffix) {
        this.suffix = suffix;
    }

    public static String fromCurrency(String currencyCode) {
        try {
            return AccountCurrency.valueOf(currencyCode.toUpperCase()).getSuffix();
        } catch (IllegalArgumentException _) {
            return "99"; // Default suffix for unknown currencies
        }
    }
}