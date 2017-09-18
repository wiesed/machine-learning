package com.bm.common;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Klasse mit einigen basis hilfsmethoden.
 * 
 * @author Daniel Wiese
 * @since 26.11.2005
 */
public final class BmGlobal {

    private static DecimalFormat decFormat = null;

    private BmGlobal() {
        // intentionally left blank
    }

    /**
     * Formattiret eine Zahl.
     * 
     * @author Daniel Wiese
     * @since 26.11.2005
     * @param zahl -
     *            die Zahl zu formattiren
     * @return - die formattierte Zahl
     */
    public static String format(double zahl) {
        initFormat();
        return decFormat.format(zahl);
    }

    /**
     * Formattiert eine Zahl als Wert in EUR.
     * 
     * @author Daniel Wiese
     * @since 26.11.2005
     * @param zahl -
     *            die Zahl zu formattiren
     * @return - Zahl als Wert in EUR
     */
    public static String formatEuro(double zahl) {
        initFormat();
        return decFormat.format(zahl) + " EUR";
    }

    private static void initFormat() {
        if (decFormat == null) {
            final DecimalFormatSymbols symbol = new DecimalFormatSymbols();
            symbol.setDecimalSeparator('.');
            decFormat = new DecimalFormat("##,####,####0.00");
            decFormat.setDecimalFormatSymbols(symbol);
        }
    }
}
