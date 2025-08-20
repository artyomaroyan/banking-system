package am.banking.system.common.shared.enums;

import lombok.Getter;

/**
 * Author: Artyom Aroyan
 * Date: 22.07.25
 * Time: 14:06:55
 */
@Getter
public enum Currency {
    AMD("00"),
    USD("01"),
    EUR("02"),
    GBP("03");

    private final String suffix;

    Currency(String suffix) {
        this.suffix = suffix;
    }

    public static String fromCurrency(String currencyCode) {
        try {
            return Currency.valueOf(currencyCode.toUpperCase()).getSuffix();
        } catch (IllegalArgumentException _) {
            return "99"; // Default suffix for unknown currencies
        }
    }
}