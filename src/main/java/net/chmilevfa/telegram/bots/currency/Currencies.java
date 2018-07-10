package net.chmilevfa.telegram.bots.currency;

/**
 * Supported currencies.
 *
 * @author chmilevfa
 * @since 07.07.18
 */
public enum Currencies {

    EUR,
    USD,
    PLN,
    RUB,
    CZK;

    public static boolean containsByName(String name) {
        for (Currencies currencyEnum : Currencies.values()) {
            if (currencyEnum.name().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
}
